package com.pragma.powerup.infrastructure.out.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequestDto {

    @Schema(description = "Numero de telefono del usuario a enviar el mensaje", example = "+573013232456")
    @JsonProperty("numero_telefono")
    private String phoneNumber;

    @Schema(description = "El mensaje a enviar al usuario", example = "Hola...")
    @JsonProperty("mensaje")
    private String message;
}
