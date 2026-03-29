package com.linkedais.backend.controller;

import com.linkedais.backend.service.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/connections")
public class ConnectionController {
    @Autowired
    private ConnectionService connectionService;

    @PostMapping("/send/{receiverId}")
    public ResponseEntity<Void> sendRequest(@PathVariable Long receiverId, Principal principal) {
        connectionService.sendRequest(principal.getName(), receiverId);
        return ResponseEntity.status(201).build();
    }
    @PutMapping("/accept/{connectionId}")
    public ResponseEntity<Void> acceptRequest(@PathVariable Long connectionId, Principal principal) {
        connectionService.acceptRequest(connectionId, principal.getName());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/reject/{connectionId}")
    public ResponseEntity<Void> rejectRequest(@PathVariable Long connectionId, Principal principal) {
        connectionService.rejectRequest(connectionId, principal.getName());
        return ResponseEntity.ok().build();
    }
    @GetMapping("/status/{receiverId}")
    public ResponseEntity<String> getStatus(@PathVariable Long receiverId, Principal principal) {
        String status = connectionService.getConnectionStatus(principal.getName(), receiverId);
        return ResponseEntity.ok(status);
    }
}
