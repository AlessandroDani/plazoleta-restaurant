package com.pragma.powerup.infrastructure.out.http;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private Long id;

    @JsonProperty("rol")
    private RoleResponse role;

    @Getter
    @Setter
    public static class RoleResponse {
        @JsonProperty("nombre")
        private String name;
    }
}
