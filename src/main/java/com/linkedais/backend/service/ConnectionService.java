package com.linkedais.backend.service;

import com.linkedais.backend.model.Connection;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.ConnectionRepository;
import com.linkedais.backend.repository.UserRepository;
import enums.ConnectionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConnectionService {
    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendRequest(String senderEmail, Long receiverId) {
        User sender = userRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("User not found"));
        User receiver = userRepository.findById(receiverId).orElseThrow(() -> new RuntimeException("User not found"));

        if (connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiver.getId()).isPresent()) {
            throw new RuntimeException("Request already sent");
        }

        Connection connection = new Connection();
        connection.setSender(sender);
        connection.setReceiver(receiver);
        connection.setStatus(ConnectionStatus.PENDING);
        connectionRepository.save(connection);
    }

    public void acceptRequest(Long connectionId, String email) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(() -> new RuntimeException("Connection not found"));
        connection.setStatus(ConnectionStatus.ACCEPTED);
        connectionRepository.save(connection);
    }

    public void rejectRequest(Long connectionId, String email) {
        Connection connection = connectionRepository.findById(connectionId).orElseThrow(() -> new RuntimeException("Connection not found"));
        connection.setStatus(ConnectionStatus.REJECTED);
        connectionRepository.save(connection);
    }

    public String getConnectionStatus(String senderEmail,  Long receiverId) {
        User sender = userRepository.findByEmail(senderEmail).orElseThrow(() -> new RuntimeException("User not found"));

        return connectionRepository.findBySenderIdAndReceiverId(sender.getId(), receiverId)
                .map(connection -> connection.getStatus().toString())
                .orElse("NONE");
    }
}
