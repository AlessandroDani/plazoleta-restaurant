package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.exception.RestaurantNotExistException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.ITokenPort;
import com.pragma.powerup.infrastructure.exception.InvalidRoleException;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final ITokenPort tokenPort;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence, ITokenPort tokenPort) {
        this.restaurantPersistence = restaurantPersistence;
        this.tokenPort = tokenPort;
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
