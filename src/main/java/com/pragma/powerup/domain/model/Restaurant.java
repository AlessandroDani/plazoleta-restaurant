package com.pragma.powerup.domain.model;


import com.pragma.powerup.domain.exception.PlateBelongsToAnotherRestaurantException;
import com.pragma.powerup.domain.exception.PlateNotFoundException;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;

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

    public Restaurant(){}

    public Restaurant(Long id, String name, String nit, String address, String phoneNumber, String urlLogo, Long idOwner) {
        this.id = id;
        this.name = name;
        this.nit = nit;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.urlLogo = urlLogo;
        this.idOwner = idOwner;
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

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUrlLogo() {
        return urlLogo;
    }

    public void setUrlLogo(String urlLogo) {
        this.urlLogo = urlLogo;
    }

    public Long getIdOwner() {
        return idOwner;
    }

    public void setIdOwner(Long idOwner) {
        this.idOwner = idOwner;
    }

    public void validatePlateList(List<OrderPlate> plates, Long restaurantId, IPlatePersistencePort platePersistencePort){
        for(OrderPlate orderPlate : plates){
            Plate plate = platePersistencePort.getPlateById(orderPlate.getIdPlate());
            if(plate == null){
                throw new PlateNotFoundException();
            }
            if(!Objects.equals(restaurantId, plate.getIdRestaurant())){
                throw new PlateBelongsToAnotherRestaurantException();
            }
        }
    }
}
