package com.pragma.powerup.domain.model;

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
}
