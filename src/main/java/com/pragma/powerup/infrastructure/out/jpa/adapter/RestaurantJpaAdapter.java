package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RestaurantJpaAdapter implements IRestaurantPersistencePort {

    private final IRestaurantRepository restaurantRepository;
    private final IRestaurantEntityMapper restaurantEntityMapper;

    @Override
    public void saveRestaurant(Restaurant restaurant) {
        restaurantRepository.save(restaurantEntityMapper.toEntity(restaurant));
    }

    @Override
    public Restaurant getRestaurantByNit(String nit) {
        return restaurantEntityMapper.toRestaurant(restaurantRepository.getRestaurantByNit(nit));
    }
}
