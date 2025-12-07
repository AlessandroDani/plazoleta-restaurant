package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.exception.RestaurantNotFoundException;
import com.pragma.powerup.domain.model.Restaurant;
import com.pragma.powerup.domain.spi.IRestaurantPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;

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

    public Restaurant getRestaurantById(Long id) {
        return restaurantEntityMapper.toRestaurant(restaurantRepository.getRestaurantById(id));
    }

    @Override
    public List<Restaurant> getAllRestaurant(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("name").ascending());

        Page<RestaurantEntity> restaurantPage = restaurantRepository.findAll(pageable);

        List<RestaurantEntity> restaurantEntityList = restaurantPage.getContent();
        if(restaurantEntityList.isEmpty()){
            throw new RestaurantNotFoundException();
        }
        return restaurantEntityMapper.toRestaurantList(restaurantEntityList);
    }
}
