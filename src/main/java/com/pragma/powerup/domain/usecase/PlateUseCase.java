package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IPlateServicePort;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Plate;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IPlatePersistencePort;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;

import java.util.Objects;


public class PlateUseCase implements IPlateServicePort {

    private final IPlatePersistencePort platePersistencePort;
    private final IRestaurantPersistencePort restaurantPersistencePort;

    public PlateUseCase(IPlatePersistencePort platePersistencePort,  IRestaurantPersistencePort restaurantPersistencePort) {
        this.platePersistencePort = platePersistencePort;
        this.restaurantPersistencePort = restaurantPersistencePort;
    }

    @Override
    public void savePlate(Plate plate, Long idOwnerRequest) {
        Restaurant restaurant = restaurantPersistencePort.getRestaurantById(plate.getIdRestaurant());
        if (!Objects.equals(restaurant.getIdOwner(), idOwnerRequest)){
            throw new UserIsNotOwnerRestaurantException();
        }
        plate.setActive(true);
        platePersistencePort.savePlate(plate);
    }
}
