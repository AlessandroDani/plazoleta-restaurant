package com.pragma.powerup.domain.model;

import java.time.LocalDateTime;

public class Traceability {
    private final String id;
    private final Long restaurantId;
    private final Long orderId;
    private final Long clientId;
    private final String clientEmail;
    private final LocalDateTime date;
    private final String lastStatus;
    private final String newStatus;
    private final Long employeeId;
    private final String employeeEmail;

    private Traceability(Builder builder) {
        this.id = builder.id;
        this.restaurantId = builder.restaurantId;
        this.orderId = builder.orderId;
        this.clientId = builder.clientId;
        this.clientEmail = builder.clientEmail;
        this.date = builder.date;
        this.lastStatus = builder.lastStatus;
        this.newStatus = builder.newStatus;
        this.employeeId = builder.employeeId;
        this.employeeEmail = builder.employeeEmail;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String id;
        private Long restaurantId;
        private Long orderId;
        private Long clientId;
        private String clientEmail;
        private LocalDateTime date;
        private String lastStatus;
        private String newStatus;
        private Long employeeId;
        private String employeeEmail;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder restaurantId(Long restaurantId) {
            this.restaurantId = restaurantId;
            return this;
        }

        public Builder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public Builder clientId(Long clientId) {
            this.clientId = clientId;
            return this;
        }

        public Builder clientEmail(String clientEmail) {
            this.clientEmail = clientEmail;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder lastStatus(String lastStatus) {
            this.lastStatus = lastStatus;
            return this;
        }

        public Builder newStatus(String newStatus) {
            this.newStatus = newStatus;
            return this;
        }

        public Builder employeeId(Long employeeId) {
            this.employeeId = employeeId;
            return this;
        }

        public Builder employeeEmail(String employeeEmail) {
            this.employeeEmail = employeeEmail;
            return this;
        }

        public Traceability build() {
            return new Traceability(this);
        }
    }


    public String getId() {
        return id;
    }

    public Long getRestaurantId(){return  restaurantId;}

    public Long getOrderId() {
        return orderId;
    }

    public Long getClientId() {
        return clientId;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public String getLastStatus() {
        return lastStatus;
    }

    public String getNewStatus() {
        return newStatus;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }
}