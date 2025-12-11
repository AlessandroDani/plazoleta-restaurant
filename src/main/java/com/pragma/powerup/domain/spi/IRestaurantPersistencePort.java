package com.pragma.powerup.domain.spi;

import com.pragma.powerup.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface IRestaurantPersistencePort {
    void saveRestaurant(Restaurant restaurant);
    boolean existsRestaurantByNit(String nit);
    Optional<Restaurant> getRestaurantById(Long id);
    Optional<List<Restaurant>> getAllRestaurant(int page, int size);
}
