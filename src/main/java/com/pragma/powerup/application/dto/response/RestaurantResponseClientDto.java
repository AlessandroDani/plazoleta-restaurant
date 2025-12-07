package com.pragma.powerup.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseClientDto {

    @Schema(description = "Nombre del restaurante", example = "Subway")
    @JsonProperty("nombre")
    private String name;

    @Schema(description = "Url del logo del restaurante", example = "https://img.mi-dominio.com/logo/example.png")
    @JsonProperty("urlLogo")
    private String urlLogo;
}
