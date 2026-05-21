package com.linkedais.backend.service;

import com.linkedais.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class UserStatusService {

    private final UserRepository userRepository;

    public UserStatusService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void processHeartbeat(Long userId) {
        userRepository.updateLastSeen(userId, LocalDateTime.now());
    }

    public String getStatus(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getLastSeen() == null) return "Offline";

                    LocalDateTime now = LocalDateTime.now();
                    boolean isOnline = user.getLastSeen().isAfter(now.minusMinutes(5));
                    if (isOnline) return "Online";

                    boolean isMoreThan24h = user.getLastSeen().isBefore(now.minusHours(24));
                    if (isMoreThan24h) return "Offline";

                    long hoursAgo = java.time.Duration.between(user.getLastSeen(), now).toHours();
                    long minutesAgo = java.time.Duration.between(user.getLastSeen(), now).toMinutes();

                    if (hoursAgo == 0) {
                        return "Paskutinį kartą matyta: prieš " + minutesAgo + " min.";
                    }
                    return "Paskutinį kartą matyta: prieš " + hoursAgo + " val.";
                })
                .orElse("Offline");
    }
}
