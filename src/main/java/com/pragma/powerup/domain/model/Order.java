package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.exception.*;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private OrderStatus status;
    private Long idChef;
    private Long idRestaurant;
    private Integer securityPin;
    private List<OrderPlate> plates;

    public Order() {
    }

    private Order(Builder builder) {
        this.id = builder.id;
        this.idClient = builder.idClient;
        this.date = builder.date;
        this.status = builder.status;
        this.idChef = builder.idChef;
        this.idRestaurant = builder.idRestaurant;
        this.securityPin = builder.securityPin;
        this.plates = builder.plates;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private Long idClient;
        private LocalDateTime date;
        private OrderStatus status;
        private Long idChef;
        private Long idRestaurant;
        private Integer securityPin;
        private List<OrderPlate> plates;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder idClient(Long idClient) {
            this.idClient = idClient;
            return this;
        }

        public Builder date(LocalDateTime date) {
            this.date = date;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder idChef(Long idChef) {
            this.idChef = idChef;
            return this;
        }

        public Builder idRestaurant(Long idRestaurant) {
            this.idRestaurant = idRestaurant;
            return this;
        }

        public Builder securityPin(Integer securityPin) {
            this.securityPin = securityPin;
            return this;
        }

        public Builder plates(List<OrderPlate> plates) {
            this.plates = plates;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdClient() {
        return idClient;
    }

    public void setIdClient(Long idClient) {
        this.idClient = idClient;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getIdChef() {
        return idChef;
    }

    public void setIdChef(Long idChef) {
        this.idChef = idChef;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public Integer getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(Integer securityPin) {
        this.securityPin = securityPin;
    }

    public List<OrderPlate> getPlates() {
        return plates;
    }

    public void setPlates(List<OrderPlate> plates) {
        this.plates = plates;
    }

    public void initializeNewOrder(Long userId, LocalDateTime currentDate) {
        this.date = currentDate;
        this.status = OrderStatus.PENDING;
        this.idClient = userId;
    }

    public void isOwner(Long userId) {
        if (!this.idClient.equals(userId)) {
            throw new ClientIsNotOrderOwnerException();
        }
    }

    public void assignToPreparation(Long idChef) {
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw new OrderNotInPendingStatusException();
        }
        this.idChef = idChef;
        this.status = OrderStatus.IN_PREPARATION;
    }

    public void assignToReady(Integer securityPin) {
        if (!this.status.equals(OrderStatus.IN_PREPARATION)) {
            throw new OrderNotInPreparationStatusException();
        }
        this.securityPin = securityPin;
        this.status = OrderStatus.READY;
    }

    public void assignToDelivered(Integer securityPin) {
        if (!this.securityPin.equals(securityPin)) {
            throw new OrderHasIncorrectPinException();
        }
        if (!this.status.equals(OrderStatus.READY)) {
            throw new OrderNotInReadyStatusException();
        }
        this.status = OrderStatus.DELIVERED;
    }

    public void assignToCanceled() {
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw new OrderNotInPendingStatusException();
        }
        this.status = OrderStatus.CANCELED;
    }
}
