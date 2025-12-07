package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.response.RestaurantResponseClientDto;
import com.pragma.powerup.domain.model.Restaurant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IRestaurantResponseMapper {

    default List<RestaurantResponseClientDto> toResponseList(List<Restaurant> restaurantList) {
        return restaurantList.stream()
                .map(restaurant -> {
                    RestaurantResponseClientDto restaurantResponseClientDto = new RestaurantResponseClientDto();
                    restaurantResponseClientDto.setName(restaurant.getName());
                    restaurantResponseClientDto.setUrlLogo(restaurant.getUrlLogo());
                    return restaurantResponseClientDto;
                }).collect(Collectors.toList());
    }
}
