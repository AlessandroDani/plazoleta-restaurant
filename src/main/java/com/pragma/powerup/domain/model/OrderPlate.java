package com.pragma.powerup.domain.model;

public class OrderPlate {
    private Long id;
    private Long idOrder;
    private Long idPlate;
    private Integer quantity;

    public OrderPlate(Long id, Long idOrder, Long idPlate, Integer quantity) {
        this.id = id;
        this.idOrder = idOrder;
        this.idPlate = idPlate;
        this.quantity = quantity;
    }

    public OrderPlate() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Long idOrder) {
        this.idOrder = idOrder;
    }

    public Long getIdPlate() {
        return idPlate;
    }

    public void setIdPlate(Long idPlate) {
        this.idPlate = idPlate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
