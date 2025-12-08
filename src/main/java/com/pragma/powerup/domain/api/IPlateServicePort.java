package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.Plate;

import java.util.List;

public interface IPlateServicePort {
    void savePlate(Plate plate);
    void updatePlate(Long newPrice, String newDescription, Long idPlate);
    void updateActivePlate(boolean status,  Long idPlate);
    List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category);
}
