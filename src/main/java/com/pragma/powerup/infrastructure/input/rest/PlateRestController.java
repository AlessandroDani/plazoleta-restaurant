package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlatePathActiveRequestDto;
import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
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
@RequestMapping("/api/platos")
@RequiredArgsConstructor
@Tag(name = "Platos", description = "Operaciones para la gestión de platos (creación, modificación y listado)")
public class PlateRestController {
    private final  IPlateHandler plateHandler;

    @Operation(summary = "Listar todos los platos de un restaurante paginados y filtrados por categoria",
            description = "Permite a los CLIENTES listar los platos de un restaurante, filtrados por una categoría específica y paginados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platos obtenidos",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = PlateResponseDto.class)))),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante con ese id no existe",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<PlateResponseDto>> getPlatesByRestaurant(
            @Parameter(description = "ID del restaurante cuyos platos se desean listar", example = "1") @RequestParam Long restaurantId,
            @Parameter(description = "Nombre de la categoría para filtrar (opcional)", example = "Entradas")  @RequestParam(required = false) String category,
            @Parameter(description = "Número de página a buscar (inicia en 0)", example = "0")  @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página", example = "5")  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(plateHandler.getPlatesByRestaurant(restaurantId, page, size, category));
    }

    @Operation(summary = "Agregar un nuevo plato",
            description = "Permite al PROPIETARIO crear un nuevo plato en uno de sus restaurantes.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado con éxito", content = @Content),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El restaurante no existe con ese identificador.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Ya existe un plato con ese nombre.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "503", description = "Servicio de usuarios no disponible.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PostMapping
    public ResponseEntity<Void> savePlate(@Valid @RequestBody PlateRequestDto plateRequestDto) {
        plateHandler.savePlate(plateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar un plato (descripcion y precio)",
            description = "Permite al PROPIETARIO modificar la descripción y el precio de un plato existente en su restaurante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "El usuario no autorizado.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El plato no existe.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlate(
            @PathVariable Long id, @Valid @RequestBody PlateUpdateRequestDto plateUpdateRequestDto) {
        plateHandler.updatePlate(plateUpdateRequestDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Habilitar/Deshabilitar un plato" ,
            description = "Permite al PROPIETARIO cambiar el estado (activo/inactivo) de un plato.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "El usuario no autorizado.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El plato no existe.",  content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id, @Valid @RequestBody PlatePathActiveRequestDto plateUpdateRequestDto) {
        plateHandler.updateStatusPlate(plateUpdateRequestDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
