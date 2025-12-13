package com.pragma.powerup.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    NO_DATA_FOUND("El usuario con el ID especificado no fue encontrado."),
    USER_NOT_OWNER_RESTAURANT("El usuario autenticado no es el propietario de este restaurante."),
    USER_DENIED_PERMISSION("El rol del usuario no está autorizado para esta operación."),
    USER_HAS_ACTIVE_ORDER("El cliente ya tiene un pedido activo."),
    SERVICE_UNAVAILABLE("El servicio de usuarios no está disponible en este momento."),
    EMPLOYEE_NOT_VALID("El usuario no está asociado al restaurante."),
    USER_IS_ALREADY_EMPLOYEE("El usuario ya fue asignado a un restaurante"),

    RESTAURANT_ALREADY_EXIST("Ya existe un restaurante registrado con el NIT proporcionado."),
    RESTAURANT_NOT_EXIST("El restaurante con el ID solicitado no existe."),
    RESTAURANT_NOT_FOUND("No se encontraron restaurantes para los criterios de búsqueda."),

    PLATE_ALREADY_EXIST("Ya existe un plato registrado con ese nombre en este restaurante."),
    PLATE_NOT_FOUND("El plato solicitado no fue encontrado."),
    PLATE_BELONGS_ANOTHER_RESTAURANT("Uno o más platos pertenecen a otro restaurante."),


    CATEGORY_NOT_FOUND("La categoria solicitada no fue encontrada."),


    STATUS_NOT_VALID("Estado de pedido no existe, los valores permitidos son: PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO.");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}