package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.application.mapper.IRestaurantRequestMapper;
import com.pragma.powerup.application.mapper.IRestaurantResponseMapper;
import com.pragma.powerup.domain.api.IRestaurantServicePort;
import com.pragma.powerup.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantHandler implements IRestaurantHandler{

    private final IRestaurantRequestMapper restaurantRequestMapper;
    private final IRestaurantResponseMapper  restaurantResponseMapper;
    private final IRestaurantServicePort restaurantServicePort;

    @Override
    public void save(RestaurantRequestDto restaurantRequestDto) {
        Restaurant restaurant = restaurantRequestMapper.toRestaurant(restaurantRequestDto);
        restaurantServicePort.saveRestaurant(restaurant);
    }

    @Override
    public RestaurantResponseDto getRestaurantByNit(String nit) {
        Restaurant restaurant = restaurantServicePort.getRestaurantByNit(nit);
        return restaurantResponseMapper.toResponse(restaurant);
    }

    @Override
    public RestaurantResponseDto getRestaurantById(Long id) {
        Restaurant restaurant = restaurantServicePort.getRestaurantById(id);
        return restaurantResponseMapper.toResponse(restaurant);
    }
}
