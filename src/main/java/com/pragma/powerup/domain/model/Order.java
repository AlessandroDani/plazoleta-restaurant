package com.pragma.powerup.domain.model;


import java.time.LocalDate;
import java.util.List;

public class Order {
    private Long id;
    private Long idClient;
    private LocalDate date;
    private String status ;
    private Long idChef;
    private Long idRestaurant;
    private List<OrderPlate> orders;

    public Order(Long id, Long idClient, LocalDate date, String status, Long idChef, Long idRestaurant, List<OrderPlate> orders) {
        this.id = id;
        this.idClient = idClient;
        this.date = date;
        this.status = status;
        this.idChef = idChef;
        this.idRestaurant = idRestaurant;
        this.orders = orders;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
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

    public List<OrderPlate> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderPlate> orders) {
        this.orders = orders;
    }
}
