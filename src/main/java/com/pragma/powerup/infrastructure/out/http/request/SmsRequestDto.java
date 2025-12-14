package com.pragma.powerup.infrastructure.out.http.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class SmsRequestDto {

    @Schema(description = "Numero de telefono del usuario a enviar el mensaje", example = "+573013232456")
    @NotBlank(message = "El numero de telefono es obligatorio")
    @JsonProperty("numero_telefono")
    private String phoneNumber;

    @Schema(description = "El mensaje a enviar al usuario", example = "Hola...")
    @NotBlank(message = "El mensaje es obligatorio")
    @JsonProperty("mensaje")
    private String message;
}
