package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.RestaurantEmployee;
import com.pragma.powerup.infrastructure.out.jpa.entity.RestaurantEmployeeEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantEmployeeEntityMapper {
//    @Mapping(source = "idRestaurant", target = "restaurant.id")
//    @Mapping(source = "idRestaurant", target = "user.id")
    RestaurantEmployeeEntity toEntity(RestaurantEmployee restaurantEmployee);
}
