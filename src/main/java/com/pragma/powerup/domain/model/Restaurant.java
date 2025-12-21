package com.pragma.powerup.domain.model;

import com.pragma.powerup.domain.exception.PlateNotAvailableException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;

import java.util.List;
import java.util.Objects;

public class Restaurant {
    private Long id;
    private String name;
    private String nit;
    private String address;
    private String phoneNumber;
    private String urlLogo;
    private Long idOwner;

    private Restaurant(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.nit = builder.nit;
        this.address = builder.address;
        this.phoneNumber = builder.phoneNumber;
        this.urlLogo = builder.urlLogo;
        this.idOwner = builder.idOwner;
    }

    public Restaurant() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String name;
        private String nit;
        private String address;
        private String phoneNumber;
        private String urlLogo;
        private Long idOwner;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder nit(String nit) {
            this.nit = nit;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder urlLogo(String urlLogo) {
            this.urlLogo = urlLogo;
            return this;
        }

        public Builder idOwner(Long idOwner) {
            this.idOwner = idOwner;
            return this;
        }

        public Restaurant build() {
            return new Restaurant(this);
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getNit() { return nit; }
    public void setNit(String nit) { this.nit = nit; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getUrlLogo() { return urlLogo; }
    public void setUrlLogo(String urlLogo) { this.urlLogo = urlLogo; }

    public Long getIdOwner() { return idOwner; }
    public void setIdOwner(Long idOwner) { this.idOwner = idOwner; }

    public void validatePlateList(List<OrderPlate> plates, List<Long> listIds){
        for(OrderPlate orderPlate : plates){
            if(!listIds.contains(orderPlate.getIdPlate())){
                throw new PlateNotAvailableException();
            }
        }
    }

    public void validateOwner(Long userId) {
        if (!Objects.equals(this.idOwner, userId)) {
            throw new UserIsNotOwnerRestaurantException();
        }
    }
}
