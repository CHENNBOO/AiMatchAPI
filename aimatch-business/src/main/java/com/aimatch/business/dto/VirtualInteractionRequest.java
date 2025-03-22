package com.aimatch.business.dto;

import lombok.Data;

@Data
public class VirtualInteractionRequest {
    private String message;
    private String personality;
    private String userId;
} 