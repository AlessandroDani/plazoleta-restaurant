package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseClientDto;
import com.pragma.powerup.application.handler.impl.RestaurantHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
public class RestaurantRestController {

    private final RestaurantHandler restaurantHandler;

    @Operation(summary = "Agregar un nuevo restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. validación fallida)", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content),
            @ApiResponse(responseCode = "404", description = "El ID del usuario no existe", content = @Content),
            @ApiResponse(responseCode = "409", description = "Restaurante ya existe", content = @Content),
            @ApiResponse(responseCode = "503", description = "Error de comunicación o servicio de Usuarios no disponible", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Void> saveRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.save(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los restaurantes paginados y ordenados por nombre")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes obtenidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron restaurantes para los criterios de búsqueda", content = @Content),
    })
    @GetMapping
    public ResponseEntity<List<RestaurantResponseClientDto>> getAllRestaurant(@Parameter(description = "Número de página a buscar (inicia en 0)", example = "0") @RequestParam(defaultValue = "0") int page,
                                                                              @Parameter(description = "Número de elementos por página", example = "5") @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(restaurantHandler.getAllRestaurant(page, size));
    }


}
