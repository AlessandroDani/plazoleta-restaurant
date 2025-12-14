package com.pragma.powerup.application.handler.impl;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.application.mapper.IOrderRequestMapper;
import com.pragma.powerup.domain.api.IOrderServicePort;
import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderPlate;
import com.pragma.powerup.domain.model.OrderStatus;
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

    @Override
    public List<OrderResponseDto> getOrdersByStatus(OrderStatus orderStatus, int page, int size) {
        List<Order> orders = orderServicePort.getOrdersByStatus(orderStatus, page, size);
        return orderRequestMapper.toResponseList(orders);
    }

    @Override
    public void assignOrderAndChangeStatus(Long orderId) {
        orderServicePort.assignOrderAndChangeStatus(orderId);
    }

    @Override
    public void notifyOrderReady(Long orderId) {
        orderServicePort.notifyOrderReady(orderId);
    }

}
