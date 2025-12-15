package com.pragma.powerup.infrastructure.out.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TraceabilityResponseDto {

    @Schema(description = "ID único del registro de trazabilidad", example = "657a7b8e1f23456789abcdef")
    private String id;

    @Schema(description = "Id del pedido", example = "24")
    @JsonProperty("id_pedido")
    private Long orderId;

    @Schema(description = "Fecha y hora del cambio de estado", example = "2025-12-12 16:46:53.400378")
    @JsonProperty("fecha")
    private LocalDateTime date;

    @Schema(description = "Estado anterior del pedido", example = "Pendiente")
    @JsonProperty("estado_anterior")
    private String lastStatus;

    @Schema(description = "Estado nuevo del pedido", example = "En_preparacion")
    @JsonProperty("estado_nuevo")
    private String newStatus;

    @Schema(description = "Id del empleado responsable del cambio de estado", example = "1")
    @JsonProperty("id_empleado")
    private Long employeeId;
}
