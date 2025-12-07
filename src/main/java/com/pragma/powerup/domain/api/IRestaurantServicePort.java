package com.pragma.powerup.domain.api;
import com.pragma.powerup.domain.model.Restaurant;

import java.util.List;

public interface IRestaurantServicePort {
    void saveRestaurant(Restaurant restaurant);
    List<Restaurant> getAllRestaurant(int page, int size);
}
