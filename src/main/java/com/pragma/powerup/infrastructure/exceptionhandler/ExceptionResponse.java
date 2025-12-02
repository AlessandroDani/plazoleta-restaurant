package com.pragma.powerup.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    NO_DATA_FOUND("El ID del propietario no existe en el sistema de usuarios"),
    FORBIDDEN("El usuario no tiene rol permitido para realizar esa acción"),
    SERVICE_UNAVAILABLE("Error del servicio de Usuarios"),
    RESTAURANT_ALREADY_EXIST("Ya existe un restaurante con ese NIT en el sistema de restaurantes");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}