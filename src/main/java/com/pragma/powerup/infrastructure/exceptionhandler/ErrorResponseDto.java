package com.pragma.powerup.infrastructure.exceptionhandler;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponseDto {
    private final String message;

    public ErrorResponseDto(String message) {
        this.message = message;
    }
}
