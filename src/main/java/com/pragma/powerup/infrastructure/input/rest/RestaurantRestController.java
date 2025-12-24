package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.RestaurantEmployeeRequestDto;
import com.pragma.powerup.application.dto.request.RestaurantRequestDto;
import com.pragma.powerup.application.dto.response.RestaurantResponseClientDto;
import com.pragma.powerup.application.handler.IRestaurantEmployeeHandler;
import com.pragma.powerup.application.handler.IRestaurantHandler;
import com.pragma.powerup.infrastructure.exceptionhandler.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/restaurantes")
@RequiredArgsConstructor
@Tag(name = "Restaurantes", description = "Operaciones para la gestión de restaurantes y empleados")
public class RestaurantRestController {

    private final IRestaurantHandler restaurantHandler;
    private final IRestaurantEmployeeHandler restaurantEmployeeHandler;

    @Operation(summary = "Listar todos los restaurantes paginados y ordenados por nombre",
            description = "Permite a los CLIENTES y otros usuarios listar los restaurantes, mostrando solo información pública.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Restaurantes obtenidos", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = RestaurantResponseClientDto.class)))),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<RestaurantResponseClientDto>> getAllRestaurant(
            @Parameter(description = "Número de página a buscar (inicia en 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página", example = "5") @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(restaurantHandler.getAllRestaurant(page, size));
    }

    @Operation(summary = "Agregar un nuevo restaurante",
            description = "Permite a un usuario con rol PROPIETARIO crear un nuevo restaurante en el sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Restaurante creado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos (ej. validación fallida)",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El ID del usuario no existe",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Restaurante ya existe",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "503", description = "Error de comunicación o servicio de Usuarios no disponible",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<Void> saveRestaurant(@Valid @RequestBody RestaurantRequestDto restaurantRequestDto) {
        restaurantHandler.save(restaurantRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Insertar un nuevo empleado a un restaurante")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empleado ingresado con exito", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El usuario con el ID especificado no fue encontrado",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PostMapping("/{id}/empleados")
    public ResponseEntity<Void> saveEmployee(
            @Parameter(description = "id del restaurante", example = "1") @PathVariable Long id,
            @Valid @RequestBody RestaurantEmployeeRequestDto restaurantEmployeeRequestDto) {
        restaurantEmployeeHandler.saveEmployee(restaurantEmployeeRequestDto, id);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
