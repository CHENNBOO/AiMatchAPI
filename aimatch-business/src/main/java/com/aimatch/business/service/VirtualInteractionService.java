package com.aimatch.business.service;

import com.aimatch.business.dto.VirtualInteractionResponse;

public interface VirtualInteractionService {
    VirtualInteractionResponse interact(String message, String personality);
} 