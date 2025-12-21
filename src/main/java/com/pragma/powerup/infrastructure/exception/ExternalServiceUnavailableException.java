package com.pragma.powerup.infrastructure.exception;

public class ExternalServiceUnavailableException extends RuntimeException {
    public ExternalServiceUnavailableException() {
        super();
    }
    public ExternalServiceUnavailableException(String message) {
        super(message);
    }
}
