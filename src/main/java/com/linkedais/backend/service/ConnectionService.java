package com.linkedais.backend.service;

import com.linkedais.backend.dto.ConnectionResponse;
import com.linkedais.backend.model.Connection;
import com.linkedais.backend.model.Notification;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.ConnectionRepository;
import com.linkedais.backend.repository.NotificationRepository;
import com.linkedais.backend.repository.UserRepository;
import enums.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    public void sendRequest(String senderEmail, Long receiverId) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<Connection> existing = connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiverId);

        if (existing.isPresent()) {
            ConnectionStatus status = existing.get().getStatus();
            if (status == ConnectionStatus.PENDING || status == ConnectionStatus.ACCEPTED) {
                throw new RuntimeException("Request already sent");
            }
            // If rejected - delete old and create new
            connectionRepository.delete(existing.get());
        }

        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);
        connectionRepository.save(connection);

        Notification notification = new Notification();
        notification.setUser(receiver);
        notification.setType("CONNECTION_REQUEST");
        notification.setMessage(sender.getName() + " nori prisijungti prie jūsų tinklo");
        notification.setConnectionId(connection.getId());
        notificationRepository.save(notification);
    }

    public void acceptRequest(Long connectionId, String email) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));
        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);

        // Notify sender
        Notification notification = new Notification();
        notification.setUser(connection.getSender());
        notification.setType("CONNECTION_ACCEPTED");
        notification.setMessage(connection.getReceiver().getName() + " priėmė jūsų prisijungimo užklausą");
        notificationRepository.save(notification);
    }

    public void rejectRequest(Long connectionId, String email) {
        Connection connection = connectionRepository.findById(connectionId)
                .orElseThrow(() -> new RuntimeException("Connection not found"));
        connection.setStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection);

        // Notify sender
        Notification notification = new Notification();
        notification.setUser(connection.getSender());
        notification.setType("CONNECTION_REJECTED");
        notification.setMessage(connection.getReceiver().getName() + " atmetė jūsų prisijungimo užklausą");
        notificationRepository.save(notification);
    }

    public String getConnectionStatus(String senderEmail, Long receiverId) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Check if current user sent a request
        Optional<Connection> sent = connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiverId);
        if (sent.isPresent()) {
            return sent.get().getStatus().toString();
        }

        // Check if current user received a request
        Optional<Connection> received = connectionRepository.findBySenderIdAndReceiverId(receiverId, sender.getId());
        if (received.isPresent()) {
            return received.get().getStatus().toString();
        }

        return "NONE";
    }

    public List<ConnectionResponse> getAcceptedConnections(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Connection> asSender = connectionRepository
                .findBySenderIdAndStatus(user.getId(), ConnectionStatus.ACCEPTED);
        List<Connection> asReceiver = connectionRepository
                .findByReceiverIdAndStatus(user.getId(), ConnectionStatus.ACCEPTED);

        List<ConnectionResponse> result = new java.util.ArrayList<>();

        asSender.forEach(c -> result.add(new ConnectionResponse(
                c.getId(),
                c.getSender().getId(),
                c.getSender().getName(),
                c.getReceiver().getId(),
                c.getReceiver().getName(),
                c.getStatus().toString()
        )));

        asReceiver.forEach(c -> result.add(new ConnectionResponse(
                c.getId(),
                c.getSender().getId(),
                c.getSender().getName(),
                c.getReceiver().getId(),
                c.getReceiver().getName(),
                c.getStatus().toString()
        )));

        return result;
    }

    public List<ConnectionResponse> getPendingRequests(String email) {
        User receiver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return connectionRepository.findByReceiverIdAndStatus(receiver.getId(), ConnectionStatus.PENDING)
                .stream()
                .map(c -> new ConnectionResponse(
                        c.getId(),
                        c.getSender().getId(),
                        c.getSender().getName(),
                        c.getReceiver().getId(),
                        c.getReceiver().getName(),
                        c.getStatus().toString()
                ))
                .collect(Collectors.toList());
    }
}
