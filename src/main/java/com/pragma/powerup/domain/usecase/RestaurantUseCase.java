package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.exception.UserIsNotOwnerRestaurantException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IExternalServicesPort;

import java.util.List;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final IExternalServicesPort externalServicesPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence, IExternalServicesPort externalServicesPort) {
        this.restaurantPersistence = restaurantPersistence;
        this.externalServicesPort = externalServicesPort;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurantPersistence.existsRestaurantByNit(restaurant.getNit())) {
            throw new RestaurantAlreadyExistException();
        }
        if(Boolean.FALSE.equals(externalServicesPort.isUserOwner(restaurant.getIdOwner()))){
            throw new UserIsNotOwnerRestaurantException();
        }
        restaurantPersistence.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant(int page, int size) {
        return restaurantPersistence.getAllRestaurant(page, size);
    }
}
