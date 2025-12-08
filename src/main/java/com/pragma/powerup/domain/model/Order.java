package com.pragma.powerup.domain.model;


import java.time.LocalDate;
import java.util.List;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDate date;
    private OrderStatus status ;
    private Long idChef;
    private Long idRestaurant;
    private List<OrderPlate> plates;

    public Order(Long id, Long idClient, LocalDate date, OrderStatus status, Long idChef, Long idRestaurant, List<OrderPlate> plates) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.plates = plates;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
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
}
