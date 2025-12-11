package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;

import java.util.List;

public class OrderJpaAdapter implements IOrderPersistencePort {

    private final IOrderRepository orderRepository;
    private final IOrderEntityMapper orderEntityMapper;

    public OrderJpaAdapter(IOrderRepository orderRepository, IOrderEntityMapper orderEntityMapper) {
        this.orderRepository = orderRepository;
        this.orderEntityMapper = orderEntityMapper;
    }

    @Override
    public void saveOrder(Order order) {
        OrderEntity orderEntity = orderEntityMapper.toEntity(order);

        orderEntity.getPlates().forEach(plate -> plate.setOrder(orderEntity));

        orderRepository.save(orderEntity);

    }

    @Override
    public boolean hasActiveOrder(Long userId) {
        List<OrderStatus> activeStatus = List.of(OrderStatus.PENDING,
                OrderStatus.IN_PREPARATION,
                OrderStatus.READY);
        return orderRepository.existsByIdClientAndStatusIn(userId, activeStatus);
    }
}
