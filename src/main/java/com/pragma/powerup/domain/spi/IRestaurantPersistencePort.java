package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    Restaurant getRestaurantByNit(String nit);
    Restaurant getRestaurantById(Long id);
    List<Restaurant> getAllRestaurant(int page, int size);
}
