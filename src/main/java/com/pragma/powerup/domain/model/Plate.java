package com.pragma.powerup.domain.model;

public class Plate {
    private Long id;
    private String name;
    private Long idCategory;
    private String description;
    private Long price;
    private Long idRestaurant;
    private String urlImagen;
    private boolean active;

    public Plate(Long id, String name, Long idCategory, String description, Long price, Long idRestaurant, String urlImagen, boolean active) {
        this.id = id;
        this.name = name;
        this.idCategory = idCategory;
        this.description = description;
        this.price = price;
        this.idRestaurant = idRestaurant;
        this.urlImagen = urlImagen;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Long idCategory) {
        this.idCategory = idCategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getIdRestaurant() {
        return idRestaurant;
    }

    public void setIdRestaurant(Long idRestaurant) {
        this.idRestaurant = idRestaurant;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
