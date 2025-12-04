package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class PlateUpdateRequestDto {
    @Schema(description = "Precio del plato, debe ser mayor a cero", example = "15000")
//    @NotNull(message = "El precio es obligatorio")
    @Min(value = 1, message = "El precio debe ser numero entero positivo mayor a 0")
    @JsonProperty("precio")
    private Long price;

    @Schema(description = "Descripción detallada del plato", example = "Carne de res, queso, lechuga y salsa.")
//    @NotBlank(message = "La descripción es obligatoria")
    @JsonProperty("descripcion")
    private String description;
}
