package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PlateRequestDto {

    @JsonProperty("id_usuario_solicitante")
    private Long idRequestUser;

    @Schema(description = "Nombre del plato", example = "Pabellon")
    @NotBlank(message = "El nombre es obligatorio")
    @JsonProperty("nombre")
    private String name;

    @Schema(description = "Id de la categoria la cual pertenece el plato", example = "2")
    @NotNull(message = "La categoria es obligatoria")
    @JsonProperty("id_categoria")
    private Long idCategory;

    @Schema(description = "Descripcion del plato", example = "Plato tipico venezolano que lleva carne mechada...")
    @NotBlank(message = "La descripción es obligatorio")
    @JsonProperty("descripcion")
    private String description;

    @Schema(description = "Precio del plato", example = "26000")
    @Min(value = 1, message = "El precio debe ser numero entero positivo mayor a 0")
    @NotNull(message = "El precio es obligatorio")
    @JsonProperty("precio")
    private Long price;

    @Schema(description = "Id del restaurante al cual pertenece el plato", example = "1")
    @NotNull(message = "La id del restaurante es obligatorio")
    @JsonProperty("id_restaurante")
    private Long idRestaurant;

    @Schema(description = "Url de la imagen del plato", example = "https://img.mi-dominio.com/logo/example.png")
    @NotBlank(message = "La url de la imagen es obligatorio")
    @JsonProperty("url_imagen")
    private String urlImagen;
}
