package com.linkedais.backend.service;

import com.linkedais.backend.dto.ScheduleDTO;
import com.linkedais.backend.model.ScheduleEntry;
import com.linkedais.backend.model.User;
import com.linkedais.backend.repository.ScheduleRepository;
import com.linkedais.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ScheduleDTO> getScheduleByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ScheduleEntry> entries = scheduleRepository.findByUserOrderByDayOfWeekAscStartTimeAsc(user);

        return entries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ScheduleDTO> getScheduleByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<ScheduleEntry> entries = scheduleRepository.findByUserOrderByDayOfWeekAscStartTimeAsc(user);

        return entries.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ScheduleDTO convertToDTO(ScheduleEntry entry) {
        return new ScheduleDTO(
                entry.getId(),
                entry.getCourse().getId(),
                entry.getCourse().getName(),
                entry.getDayOfWeek(),
                entry.getStartTime(),
                entry.getEndTime(),
                entry.getLocation(),
                entry.getType(),
                entry.getRecurrence(),
                entry.getSpecificDate()
        );
    }
}
