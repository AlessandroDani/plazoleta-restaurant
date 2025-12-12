package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RestaurantEmployeeRequestDto {

    @Schema(description = "Id del empleado", example = "15000")
    @NotNull(message = "El id del empleado es obligatorio")
    @JsonProperty("id_empleado")
    private Long idEmployee;

    @Schema(description = "Id del restaurante", example = "1")
    @NotNull(message = "El id del restaurante es obligatorio")
    @JsonProperty("id_restaurante")
    private Long idRestaurant;



}
