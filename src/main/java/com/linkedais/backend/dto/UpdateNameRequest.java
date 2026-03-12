package com.linkedais.backend.dto;

public class UpdateNameRequest{

    private String name;

    // Default constructor (required for Jackson/JSON deserialization)
    public UpdateNameRequest() {}

    // Constructor with parameters
    public UpdateNameRequest(String name) {
        this.name = name;
    }

    // Getter
    public String getName() {
        return name;
    }

    // Setter
    public void setName(String name) {
        this.name = name;
    }
}
