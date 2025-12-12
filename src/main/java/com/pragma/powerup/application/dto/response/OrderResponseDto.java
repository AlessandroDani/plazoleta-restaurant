package com.pragma.powerup.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pragma.powerup.domain.model.OrderPlate;
import com.pragma.powerup.domain.model.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrderResponseDto {

    @Schema(description = "id del cliente", example = "1")
    @JsonProperty("id_cliente")
    private Long idClient;

    @Schema(description = "fecha de la orden", example = "2025-12-08")
    @JsonProperty("fecha")
    private LocalDate date;

    @Schema(description = "estado del pedido", example = "PENDIENTE")
    @JsonProperty("estado")
    private OrderStatus status ;

    @Schema(description = "id del chef (empleado)", example = "1")
    @JsonProperty("id_chef")
    private Long idChef;

    @Schema(description = "id del restaurante del pedido", example = "1")
    @JsonProperty("id_restaurant")
    private Long idRestaurant;

    @Schema(description = "Lista de platos de la orden")
    @JsonProperty("plates")
    private List<OrderPlate> plates;
}
