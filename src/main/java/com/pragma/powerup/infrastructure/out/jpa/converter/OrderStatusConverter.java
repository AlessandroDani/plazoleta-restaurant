package com.pragma.powerup.infrastructure.out.jpa.converter;

import com.pragma.powerup.domain.model.OrderStatus;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {

    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        return status == null ? null : status.getDbValue();
    }

    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;

        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDbValue().equals(dbData)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Estado de DB desconocido: " + dbData);
    }
}