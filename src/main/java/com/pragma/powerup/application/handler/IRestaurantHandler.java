package com.pragma.powerup.application.handler;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseClientDto;

import java.util.List;

public interface IRestaurantHandler {

    void save(RestaurantRequestDto restaurantRequestDto);
    List<RestaurantResponseClientDto> getAllRestaurant(int page, int size);

}
