package com.pragma.powerup.infrastructure.out.jpa.converter;

import com.pragma.powerup.domain.model.OrderStatus;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrderStatusStringConverter implements Converter<String, OrderStatus> {

    @Override
    public OrderStatus convert(String source) {
        return source.trim().isEmpty() ? null : OrderStatus.fromDbValue(source.toUpperCase());
    }
}