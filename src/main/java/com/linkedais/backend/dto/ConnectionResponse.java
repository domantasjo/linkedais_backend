package com.linkedais.backend.dto;

public class ConnectionResponse {
    private Long id;
    private Long requesterId;
    private String requesterName;
    private Long receiverId;
    private String receiverName;
    private String senderName;
    private String status;

    public ConnectionResponse(Long id, Long requesterId, String requesterName, Long receiverId, String receiverName, String status) {
        this.id = id;
        this.requesterId = requesterId;
        this.requesterName = requesterName;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.senderName = requesterName; // backward compat
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getRequesterId() { return requesterId; }
    public void setRequesterId(Long requesterId) { this.requesterId = requesterId; }

    public String getRequesterName() { return requesterName; }
    public void setRequesterName(String requesterName) { this.requesterName = requesterName; }

    public Long getReceiverId() { return receiverId; }
    public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }

    public String getReceiverName() { return receiverName; }
    public void setReceiverName(String receiverName) { this.receiverName = receiverName; }

    public String getSenderName() { return senderName; }
    public void setSenderName(String senderName) { this.senderName = senderName; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
