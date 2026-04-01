package com.linkedais.backend.dto;

public class ConnectionResponse {
    private Long id;
    private String senderName;
    private String status;

    public ConnectionResponse(Long id, String senderName, String status) {
        this.id = id;
        this.senderName = senderName;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
