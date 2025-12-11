package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
public class OrderRequestDto {

    @Schema(description = "Id del restaurante", example = "1")
    @NotNull(message = "El id es obligatorio")
    @JsonProperty("id_restaurante")
    private Long idRestaurant;

    @Schema(description = "Lista de los platos escogidos", example = "{\n" +
            "  \"id_restaurante\": 1,\n" +
            "  \"platos\": [\n" +
            "    { \"id_plato\": 10, \"cantidad\": 2 },\n" +
            "    { \"id_plato\": 15, \"cantidad\": 1 }\n" +
            "  ]\n" +
            "}")
    @NotNull(message = "La lista de platos es obligatorio")
    @Valid
    @JsonProperty("platos")
    private List<OrderPlateRequestDto> plates;

}
