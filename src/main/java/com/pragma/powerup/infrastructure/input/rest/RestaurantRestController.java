package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/restaurant")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @Operation(summary = "Agregar un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. validación fallida)", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del usuario no existe", content = @Content),
            @ApiResponse(responseCode = "503", description = "Error de comunicación o servicio de Usuarios no disponible", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurante ya existe", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> saveRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.save(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{nit}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantByNit(@PathVariable String nit) {
        RestaurantResponseDto restaurant = restaurantHandler.getRestaurantByNit(nit);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantResponseDto> getRestaurantByNit(@PathVariable Long id) {
        RestaurantResponseDto restaurant = restaurantHandler.getRestaurantById(id);
        return new ResponseEntity<>(restaurant, HttpStatus.OK);
    }
}
