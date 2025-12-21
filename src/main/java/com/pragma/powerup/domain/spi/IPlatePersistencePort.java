package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Plate;

import java.util.List;
import java.util.Optional;

public interface IPlatePersistencePort {
    void savePlate(Plate plate);
    void updatePlate(Plate plate);
    boolean existsPlateByName(String name);
    Optional<Plate> getPlateById(Long id);
    List<Plate> getPlatesByRestaurant(Long idRestaurant, int page, int size, String category);
    List<Long> getPlatesIdsByRestaurant(Long idRestaurant);
}
