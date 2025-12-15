package com.pragma.powerup.domain.model;

public class OrderEfficiency {
    private Long orderId;
    private Double durationInMinutes;

    public OrderEfficiency(Long orderId, Double durationInMinutes) {
        this.orderId = orderId;
        this.durationInMinutes = durationInMinutes;
    }

    public OrderEfficiency() {
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Double getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(Double durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
