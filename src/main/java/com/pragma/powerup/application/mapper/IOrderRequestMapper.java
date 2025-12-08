package com.pragma.powerup.application.mapper;

import com.pragma.powerup.application.dto.request.OrderPlateRequestDto;
import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderPlate;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IOrderRequestMapper{
    OrderPlate toOrderPlate(OrderPlateRequestDto orderRequestDto);

    List<OrderPlate> toOrderPlatesList(List<OrderPlateRequestDto> orderListDto);

    Order toOrder(OrderRequestDto orderRequestDto);
}
