package com.pragma.powerup.infrastructure.out.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
public class TraceabilityRequestDto {
    @Schema(description = "Id del pedido", example = "24")
    @NotNull(message = "El id es obligatorio")
    @JsonProperty("id_pedido")
    private Long orderId;

    @Schema(description = "Id del cliente", example = "44")
    @NotNull(message = "El id del cliente es obligatorio")
    @JsonProperty("id_cliente")
    private Long clientId;

    @Schema(description = "Correo electronico del cliente", example = "cliente@gmail.com")
    @NotBlank(message = "El correo electronico del cliente es obligatorio")
    @Email
    @JsonProperty("correo_cliente")
    private String clientEmail;

    @Schema(description = "Fecha del pedido", example = "2025-12-12 16:46:53.400378")
    @NotNull(message = "La fecha es obligatorio")
    @JsonProperty("fecha")
    private LocalDateTime date;

    @Schema(description = "Estado anterior del pedido", example = "Pendiente")
    @NotBlank(message = "El estado anterior del pedido es obligatorio")
    @JsonProperty("estado_anterior")
    private String lastStatus;

    @Schema(description = "Estado nuevo del pedido", example = "En_preparacion")
    @NotBlank(message = "El estado nuevo del pedido es obligatorio")
    @JsonProperty("estado_nuevo")
    private String newStatus;

    @Schema(description = "Id del empleado", example = "1")
    @NotNull(message = "El id del empleado es obligatorio")
    @JsonProperty("id_empleado")
    private Long employeeId;

    @Schema(description = "Correo electronico del empleado", example = "empleado@gmail.com")
    @NotBlank(message = "El correo electronico del empleado es obligatorio")
    @Email
    @JsonProperty("correo_empleado")
    private String employeeEmail;
}
