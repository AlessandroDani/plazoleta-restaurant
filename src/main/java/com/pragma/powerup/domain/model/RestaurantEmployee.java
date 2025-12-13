package com.pragma.powerup.domain.model;

public class RestaurantEmployee {
    private Long id;
    private Long idUser;
    private Long idRestaurant;

    public RestaurantEmployee(Long id, Long idUser, Long idRestaurant) {
        this.id = id;
        this.idUser = idUser;
        this.idRestaurant = idRestaurant;
    }

    public RestaurantEmployee() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdUser() {
        return idUser;
    }

    public void setIdUser(Long idUser) {
        this.idUser = idUser;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }
}
