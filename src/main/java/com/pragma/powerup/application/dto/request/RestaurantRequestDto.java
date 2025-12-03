package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
public class RestaurantRequestDto {

    @Schema(description = "Nombre del restaurante", example = "Subway")
    @Pattern(regexp = "^(?!\\d+$).*$", message = "El nombre del restaurante no puede ser solo números")
    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("nombre")
    private String name;

    @Schema(description = "NIT del restaurante (solo digitos)", example = "123456789")
    @Pattern(regexp = "^\\d+$", message = "El NIT debe ser solo números")
    @NotBlank(message = "El NIT es obligatorio")
    @JsonProperty("nit")
    private String nit;

    @Schema(description = "Dirección del restaurante", example = "Calle 10 # 50 - 20")
    @NotBlank(message = "La dirección obligatorio")
    @JsonProperty("direccion")
    private String address;

    @Schema(description = "Numero de telefono (puede contener al principio '+') (máximo 13 caracteres)", example = "+573012343234")
    @NotBlank(message = "El numero es obligatorio")
    @Size(max = 13, message = "El número de teléfono no puede ser mayor de 13 caracteres")
    @Pattern(regexp = "^\\+?\\d+$", message = "Formato de teléfono no válido. Use el formato +XX...")
    @JsonProperty("telefono")
    private String phoneNumber;


    @Schema(description = "Url del logo del restaurante", example = "https://img.mi-dominio.com/logo/example.png")
    @NotBlank(message = "La url del logo es obligatorio")
    @JsonProperty("urlLogo")
    private String urlLogo;

    @Schema(description = "Id del propietario del restaurante", example = "PROPIETARIO")
    @JsonProperty("id_propietario")
    private Long idOwner;
}
