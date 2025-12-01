package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;

public interface IRestaurantHandler {

    void save(RestaurantRequestDto restaurantRequestDto);
}
