package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RestaurantRequestDto {
    @JsonProperty("nombre")
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @JsonProperty("nit")
    @NotNull(message = "El NIT es obligatorio")
    private String nit;

    @JsonProperty("direccion")
    @NotBlank(message = "La dirección obligatorio")
    private String address;

    @JsonProperty("telefono")
    @NotBlank(message = "El numero es obligatorio")
    private String phoneNumber;

    @NotBlank(message = "La url del logo es obligatorio")
    private String urlLogo;
    //private User idPropietario;
}
