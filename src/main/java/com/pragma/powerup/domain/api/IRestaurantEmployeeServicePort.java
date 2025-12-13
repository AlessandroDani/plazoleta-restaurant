package com.pragma.powerup.domain.api;

import com.pragma.powerup.domain.model.RestaurantEmployee;

public interface IRestaurantEmployeeServicePort {
    void saveEmployee(RestaurantEmployee restaurantEmployee);
}
