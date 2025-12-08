package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderPlate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderHandler implements IOrderHandler {

    private final IOrderRequestMapper  orderRequestMapper;
    private final IOrderServicePort orderServicePort;


    @Override
    public void saveOrder(OrderRequestDto orderRequestDto) {
        Order order =  orderRequestMapper.toOrder(orderRequestDto);
        List<OrderPlate> list = orderRequestMapper.toOrderPlatesList(orderRequestDto.getPlates());
        order.setPlates(list);
        orderServicePort.saveOrder(order);
    }
}
