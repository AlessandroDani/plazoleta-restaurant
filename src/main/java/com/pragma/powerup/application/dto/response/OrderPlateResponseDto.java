package com.pragma.powerup.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderPlateResponseDto {

    @Schema(description = "id del registro del plato en la orden", example = "14")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "id del plato", example = "1")
    @JsonProperty("id_plato")
    private Long idPlate;

    @Schema(description = "cantidad del plato", example = "2")
    @JsonProperty("cantidad")
    private Integer quantity;
}