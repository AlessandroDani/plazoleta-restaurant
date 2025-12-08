package com.pragma.powerup.infrastructure.out.jpa.mapper;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderPlate;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderPlateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;


@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderEntityMapper {
    @Mapping(source = "idPlate", target = "plate.id")
    @Mapping(target = "order", ignore = true)
    OrderPlateEntity toEntityOrderPlate(OrderPlate orderPlate);

    @Mapping(source = "plate.id", target ="idPlate")
    OrderPlate toOrderPlate(OrderPlateEntity orderPlateEntity);

    @Mapping(source=  "restaurant.id", target = "idRestaurant")
    @Mapping(target = "plates", source = "plates")
    Order toOrder(OrderEntity orderEntity);

    @Mapping(source = "idRestaurant", target = "restaurant.id")
    @Mapping(target = "idChef", source = "idChef")
    @Mapping(target = "plates", source = "plates")
    @Mapping(target = "restaurant", ignore = true)
    OrderEntity toEntity(Order order);
}
