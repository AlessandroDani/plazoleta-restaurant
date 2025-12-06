package com.pragma.powerup.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    NO_DATA_FOUND("El ID del propietario no existe en el sistema de usuarios"),
    USER_DENIED_PERMISSION("El usuario no es propietario del restaurante"),
    SERVICE_UNAVAILABLE("Error del servicio de Usuarios"),
    RESTAURANT_ALREADY_EXIST("Ya existe un restaurante con ese NIT en el sistema de restaurantes"),
    PLATE_ALREADY_EXIST("Ya existe un plato con ese nombre"),
    RESTAURANT_NOT_FOUND("El restaurante no existe con ese identificador"),
    PLATE_NOT_FOUND("El plato no existe en el sistema"),;

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}