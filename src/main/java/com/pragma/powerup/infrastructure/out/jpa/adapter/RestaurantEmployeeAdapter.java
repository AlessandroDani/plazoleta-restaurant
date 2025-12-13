package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.domain.spi.IRestaurantEmployeePersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IRestaurantEmployeeEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IRestaurantEmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class RestaurantEmployeeAdapter implements IRestaurantEmployeePersistencePort {
    private final IRestaurantEmployeeRepository restaurantEmployeeRepository;
    private final IRestaurantEmployeeEntityMapper restaurantEmployeeEntityMapper;


    @Override
    public void saveEmployee(RestaurantEmployee restaurantEmployee) {
        restaurantEmployeeRepository.save(restaurantEmployeeEntityMapper.toEntity(restaurantEmployee));
    }

    @Override
    public Optional<RestaurantEmployee> getEmployee(Long userId) {
        Optional<RestaurantEmployeeEntity> restaurantEmployeeEntity = restaurantEmployeeRepository.findByIdUser(userId);
        return restaurantEmployeeEntity.map(restaurantEmployeeEntityMapper::toRestaurantEmployee);
    }

    @Override
    public boolean existsByUserId(Long idUser) {
        return restaurantEmployeeRepository.existsByIdUser(idUser);
    }
}
