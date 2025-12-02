package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("El ID del propietario no existe en el sistema de usuarios"),
    FORBIDDEN("El usuario no tiene rol permitido para realizar esa acción"),
    SERVICE_UNAVAILABLE("Error del servicio de Usuarios");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}