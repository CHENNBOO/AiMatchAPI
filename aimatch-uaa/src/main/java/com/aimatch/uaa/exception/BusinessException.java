package com.aimatch.uaa.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final String message;
    private final Integer code;

    public BusinessException(String message) {
        super(message);
        this.message = message;
        this.code = 400;
    }

    public BusinessException(String message, Integer code) {
        super(message);
        this.message = message;
        this.code = code;
    }
} 