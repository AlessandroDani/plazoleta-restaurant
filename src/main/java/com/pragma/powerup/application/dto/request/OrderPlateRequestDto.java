package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderPlateRequestDto {
    @Schema(description = "Id del plato", example = "10")
    @NotNull(message = "El id del plato es obligatorio")
    @JsonProperty("id_plato")
    private Long idPlate;

    @Schema(description = "Cantidad del plato", example = "2")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad mínima debe ser 1")
    @JsonProperty("cantidad")
    private Integer quantity;
}
