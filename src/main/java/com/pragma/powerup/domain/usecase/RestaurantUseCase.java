package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence) {
        this.restaurantPersistence = restaurantPersistence;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (getRestaurantByNit(restaurant.getNit()) != null) {
            throw new RestaurantAlreadyExistException();
        }
        restaurantPersistence.saveRestaurant(restaurant);
    }

    @Override
    public Restaurant getRestaurantByNit(String nit) {
        return restaurantPersistence.getRestaurantByNit(nit);
    }

    @Override
    public Restaurant getRestaurantById(Long id) {
        Restaurant restaurant = restaurantPersistence.getRestaurantById(id);
        if(restaurant == null){
            throw new RestaurantNotExistException();
        }
        return restaurantPersistence.getRestaurantById(id);
    }
}
