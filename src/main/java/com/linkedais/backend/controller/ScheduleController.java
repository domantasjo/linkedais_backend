package com.linkedais.backend.controller;

import com.linkedais.backend.dto.ScheduleDTO;
import com.linkedais.backend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @GetMapping("/me")
    public ResponseEntity<List<ScheduleDTO>> getMySchedule(Authentication authentication) {
        String email = authentication.getName();
        List<ScheduleDTO> schedule = scheduleService.getScheduleByEmail(email);
        return ResponseEntity.ok(schedule);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<ScheduleDTO>> getUserSchedule(@PathVariable Long userId) {
        List<ScheduleDTO> schedule = scheduleService.getScheduleByUserId(userId);
        return ResponseEntity.ok(schedule);
    }
}
