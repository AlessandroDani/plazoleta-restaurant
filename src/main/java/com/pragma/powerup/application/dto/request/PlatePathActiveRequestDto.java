package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PlatePathActiveRequestDto {
    @Schema(description = "Estado del plato", example = "true")
    @NotNull(message = "El estado es obligatorio")
    @JsonProperty("activo")
    private boolean active;
}
