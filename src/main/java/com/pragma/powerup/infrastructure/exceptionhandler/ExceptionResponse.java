package com.pragma.powerup.infrastructure.exceptionhandler;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    USER_NOT_OWNER_RESTAURANT("El usuario autenticado no es el propietario de este restaurante."),
    USER_NOT_ASSIGNED("El usuario no tiene asignado un restaurante."),
    USER_HAS_ACTIVE_ORDER("El cliente ya tiene un pedido activo."),
    USER_NOT_BELONG_RESTAURANT("El empleado no está asociado a este restaurante."),
    USER_IS_ALREADY_EMPLOYEE("El empleado ya fue asignado a un restaurante"),

    RESTAURANT_ALREADY_EXIST("Ya existe un restaurante registrado con el NIT proporcionado."),
    RESTAURANT_NOT_EXIST("El restaurante con el ID solicitado no existe."),

    PLATE_ALREADY_EXIST("Ya existe un plato registrado con ese nombre en este restaurante."),
    PLATE_NOT_FOUND("El plato solicitado no fue encontrado."),
    PLATE_NOT_AVAILABLE("Uno o más platos no están disponibles o no pertenecen al restaurante."),

    ORDER_NOT_FOUND("El pedido solicitado no existe"),
    ORDER_NOT_PENDING_STATE("El pedido debe estar en estado PENDIENTE para esta operación."),
    ORDER_NOT_PREPARATION_STATE("El pedido debe estar en estado EN PREPARACION para esta operación."),
    ORDER_NOT_READY_STATE("El pedido debe estar en estado LISTO para esta operación."),
    ORDER_INCORRECT_PIN("El PIN de seguridad proporcionado para la entrega es incorrecto"),
    CLIENT_NOT_OWNER("Solo el propietario del pedido tiene permitido realizar esta operación."),

    CATEGORY_NOT_FOUND("La categoria solicitada no fue encontrada."),

    STATUS_NOT_VALID("Estado de pedido no existe, los valores permitidos son: PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO, CANCELADO."),

    FAILED_CONNECTION_TRACE("Fallo crítico al comunicarse con el servicio de Trazabilidad"),

    INVALID_DATA_EXCEPTION("Los datos enviados al servicio externo son inválidos o mal formados."),
    USER_AUTHENTICATION_EXCEPTION("Acceso no autorizado. Se requiere autenticación válida."),
    ACTION_FORBIDDEN_EXCEPTION("Permisos denegados para realizar esta acción en el servicio externo."),
    RESOURCE_NOT_FOUND_EXCEPTION("El recurso solicitado (ej., usuario, dato) no fue encontrado en el servicio externo."),
    EXTERNAL_SERVICE_FAILURE_EXCEPTION("Ocurrió un error interno (500) en el servicio externo que impidió la operación."),
    EXTERNAL_SERVICE_UNAVAILABLE_EXCEPTION("El servicio externo no está disponible o no pudo procesar la solicitud."),
    UNEXPECTED_ERROR_EXCEPTION("Ocurrió un error inesperado al comunicarse con el servicio externo.");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}