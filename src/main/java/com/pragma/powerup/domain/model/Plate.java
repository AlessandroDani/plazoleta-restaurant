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

    private Plate(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.idCategory = builder.idCategory;
        this.description = builder.description;
        this.price = builder.price;
        this.idRestaurant = builder.idRestaurant;
        this.urlImagen = builder.urlImagen;
        this.active = builder.active;
    }

    public Plate() {
    }


    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private Long idCategory;
        private String description;
        private Long price;
        private Long idRestaurant;
        private String urlImagen;
        private boolean active;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder idCategory(Long idCategory) {
            this.idCategory = idCategory;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder price(Long price) {
            this.price = price;
            return this;
        }

        public Builder idRestaurant(Long idRestaurant) {
            this.idRestaurant = idRestaurant;
            return this;
        }

        public Builder urlImagen(String urlImagen) {
            this.urlImagen = urlImagen;
            return this;
        }

        public Builder active(boolean active) {
            this.active = active;
            return this;
        }

        public Plate build() {
            return new Plate(this);
        }
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

    public void updateDetails(Long newPrice, String newDescription) {
        if (newPrice != null) {
            this.price = newPrice;
        }
        if (newDescription != null) {
            this.description = newDescription;
        }
    }
}
