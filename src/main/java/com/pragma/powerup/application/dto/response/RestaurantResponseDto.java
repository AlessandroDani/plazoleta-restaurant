package com.pragma.powerup.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDto {
    @Schema(description = "Nombre del restaurante", example = "Subway")
    @JsonProperty("nombre")
    private String name;

    @Schema(description = "NIT del restaurante (solo digitos)", example = "123456789")
    @JsonProperty("nit")
    private String nit;

    @Schema(description = "Dirección del restaurante", example = "Calle 10 # 50 - 20")
    @JsonProperty("direccion")
    private String address;

    @Schema(description = "Numero de telefono (puede contener al principio '+') (máximo 13 caracteres)", example = "+573012343234")
    @JsonProperty("telefono")
    private String phoneNumber;


    @Schema(description = "Url del logo del restaurante", example = "https://img.mi-dominio.com/logo/example.png")
    @JsonProperty("urlLogo")
    private String urlLogo;

    @Schema(description = "Id del propietario del restaurante", example = "PROPIETARIO")
    @JsonProperty("id_propietario")
    private Long idOwner;
}
