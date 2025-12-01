package com.pragma.powerup.domain.exception;

public class DomainValidateException extends RuntimeException {

    public DomainValidateException(String message) {
        super(message);
    }
}
