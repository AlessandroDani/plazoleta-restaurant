package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.exception.RestaurantAlreadyExistException;
import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.domain.spi.IUserGatewayPort;

import java.util.List;


public class RestaurantUseCase implements IRestaurantServicePort {
    private final IRestaurantPersistencePort restaurantPersistence;
    private final IUserGatewayPort  userGateway;

    public RestaurantUseCase(IRestaurantPersistencePort restaurantPersistence, IUserGatewayPort userGateway) {
        this.restaurantPersistence = restaurantPersistence;
        this.userGateway = userGateway;
    }

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        if (restaurantPersistence.existsRestaurantByNit(restaurant.getNit())) {
            throw new RestaurantAlreadyExistException();
        }
        userGateway.isUserOwner(restaurant.getIdOwner());
        restaurantPersistence.saveRestaurant(restaurant);
    }

    @Override
    public List<Restaurant> getAllRestaurant(int page, int size) {
        return restaurantPersistence.getAllRestaurant(page, size)
                .orElseThrow(RestaurantNotFoundException::new);
    }
}
