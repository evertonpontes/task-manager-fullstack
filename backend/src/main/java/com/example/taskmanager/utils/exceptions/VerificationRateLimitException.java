package com.example.taskmanager.utils.exceptions;

public class VerificationRateLimitException extends RuntimeException {
    public VerificationRateLimitException(String message) {
        super(message);
    }
}
