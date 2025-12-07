package com.pragma.powerup.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlateResponseDto {
    @Schema(description = "Nombre del plato", example = "Pabellon")
    @JsonProperty("nombre")
    private String name;

    @Schema(description = "Id de la categoria la cual pertenece el plato", example = "2")
    @JsonProperty("id_categoria")
    private Long idCategory;

    @Schema(description = "Descripcion del plato", example = "Plato tipico venezolano que lleva carne mechada...")
    @JsonProperty("descripcion")
    private String description;

    @Schema(description = "Precio del plato", example = "26000")
    @JsonProperty("precio")
    private Long price;

    @Schema(description = "Id del restaurante al cual pertenece el plato", example = "1")
    @JsonProperty("id_restaurante")
    private Long idRestaurante;

    @Schema(description = "Url de la imagen del plato", example = "https://img.mi-dominio.com/logo/example.png")
    @JsonProperty("url_imagen")
    private String urlImagen;

    @Schema(description = "El estado del plato", example = "true")
    @JsonProperty("activo")
    private boolean active;
}
