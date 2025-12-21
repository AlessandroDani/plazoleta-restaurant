package com.pragma.powerup.infrastructure.exception;

public class ExternalServiceFailureException extends RuntimeException {
    public ExternalServiceFailureException(String message) {
        super(message);
    }
    public ExternalServiceFailureException() {
        super();
    }
}
