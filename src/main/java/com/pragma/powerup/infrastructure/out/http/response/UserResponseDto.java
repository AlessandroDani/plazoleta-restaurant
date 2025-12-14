package com.pragma.powerup.infrastructure.out.http.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserResponseDto {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("nombre")
    private String name;

    @JsonProperty("apellido")
    private String lastName;

    @JsonProperty("numero_documento")
    private String idCardNumber;

    @JsonProperty("celular")
    private String phoneNumber;

    @JsonProperty("fecha_nacimiento")
    private LocalDate birthDate;

    @JsonProperty("correo")
    private String email;
}