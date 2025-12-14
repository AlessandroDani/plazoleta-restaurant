package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.exception.OrderNotInPendingStatusException;
import com.pragma.powerup.domain.exception.OrderNotInReadyStatusException;

import java.time.LocalDateTime;
import java.util.List;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDateTime date;
    private OrderStatus status;
    private Long idChef;
    private Long idRestaurant;
    private List<OrderPlate> plates;
    private String securityPin;

    public Order() {
    }

    public Order(Long id, Long idClient, LocalDateTime date, OrderStatus status, Long idChef, Long idRestaurant, List<OrderPlate> plates, String securityPin) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.plates = plates;
        this.securityPin = securityPin;
    }

    public void initializeNewOrder(Long userId, LocalDateTime currentDate) {
        this.date = currentDate;
        this.status = OrderStatus.PENDING;
        this.idClient = userId;
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

    public List<OrderPlate> getPlates() {
        return plates;
    }

    public void setPlates(List<OrderPlate> plates) {
        this.plates = plates;
    }

    public String getSecurityPin() {
        return securityPin;
    }

    public void setSecurityPin(String securityPin) {
        this.securityPin = securityPin;
    }

    public void assignToPreparation(Long idChef) {
        if (!this.status.equals(OrderStatus.PENDING)) {
            throw new OrderNotInPendingStatusException();
        }
        this.idChef = idChef;
        this.status = OrderStatus.IN_PREPARATION;
    }

    public void assignToReady(String securityPin) {
        if (!this.status.equals(OrderStatus.IN_PREPARATION)) {
            throw new OrderNotInReadyStatusException();
        }
        this.securityPin = securityPin;
        this.status = OrderStatus.READY;
    }
}
