package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.exception.InvalidStatusParameterException;

public enum OrderStatus {
    PENDING,
    IN_PREPARATION,
    READY,
    DELIVERED,
    CANCELED;

    public String getDbValue() {
        switch (this) {
            case PENDING: return "PENDIENTE";
            case IN_PREPARATION: return "EN_PREPARACION";
            case READY: return "LISTO";
            case DELIVERED: return "ENTREGADO";
            case CANCELED: return "CANCELADO";
            default: return "DESCONOCIDO";
        }
    }

    public static OrderStatus fromDbValue(String dbValue) {
        if (dbValue == null) return null;

        String upperDbValue = dbValue.toUpperCase();

        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDbValue().equals(upperDbValue)) {
                return status;
            }
        }
        try {
            return OrderStatus.valueOf(upperDbValue);
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusParameterException();
        }
    }
}
