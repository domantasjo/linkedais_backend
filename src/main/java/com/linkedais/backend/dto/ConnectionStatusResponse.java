package com.linkedais.backend.dto;

public class ConnectionStatusResponse {
    private String status;
    private Long connectionId;

    public ConnectionStatusResponse(String status, Long connectionId) {
        this.status = status;
        this.connectionId = connectionId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public Long getConnectionId() {
        return connectionId;
    }
    public void setConnectionId(Long connectionId) {
        this.connectionId = connectionId;
    }
}
