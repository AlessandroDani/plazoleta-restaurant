package com.pragma.powerup.infrastructure.out.jpa.adapter;

import com.pragma.powerup.domain.model.Order;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.domain.spi.IOrderPersistencePort;
import com.pragma.powerup.infrastructure.out.jpa.entity.OrderEntity;
import com.pragma.powerup.infrastructure.out.jpa.mapper.IOrderEntityMapper;
import com.pragma.powerup.infrastructure.out.jpa.repository.IOrderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<List<Order>> getOrdersByRestaurantAndStatus(Long restaurantId, OrderStatus status, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("name").ascending());
        Page<OrderEntity> ordersPage =
                (status == null ?
                        orderRepository.findByRestaurantId(restaurantId, pageable) :
                        orderRepository.findByRestaurantIdAndStatus(restaurantId, status, pageable));
        return orderEntityMapper.toOrderList(ordersPage.getContent());
    }
}
