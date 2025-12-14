package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PinRequestDto {

    @Schema(description = "Pin de seguridad para entregar el pedido", example = "3245")
    @NotNull(message = "El pin del pedido es obligatorio")
    @JsonProperty("pin")
    private Integer securityPin;

}
