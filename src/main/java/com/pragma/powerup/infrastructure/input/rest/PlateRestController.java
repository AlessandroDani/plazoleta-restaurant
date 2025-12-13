package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlatePathActiveRequestDto;
import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.dto.response.PlateResponseDto;
import com.pragma.powerup.application.handler.IPlateHandler;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class PlateRestController {
    private final  IPlateHandler plateHandler;


    @Operation(summary = "Agregar un nuevo plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado."),
            @ApiResponse(responseCode = "404", description = "El restaurante no existe con ese identificador."),
            @ApiResponse(responseCode = "409", description = "Ya existe un plato con ese nombre."),
            @ApiResponse(responseCode = "503", description = "Servicio de usuarios no disponible.")
    })
    @PostMapping("/platos")
    public ResponseEntity<Void> savePlate(@Valid @RequestBody PlateRequestDto plateRequestDto) {
        plateHandler.savePlate(plateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar un plato (descripcion y precio)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios"),
            @ApiResponse(responseCode = "403", description = "El usuario no autorizado."),
            @ApiResponse(responseCode = "404", description = "El plato no existe.")
    })
    @PutMapping("/platos/{id}")
    public ResponseEntity<Void> updatePlate(@PathVariable Long id, @Valid @RequestBody PlateUpdateRequestDto plateUpdateRequestDto) {
        plateHandler.updatePlate(plateUpdateRequestDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @Operation(summary = "Habilitar/Deshabilitar un plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios"),
            @ApiResponse(responseCode = "403", description = "El usuario no autorizado."),
            @ApiResponse(responseCode = "404", description = "El plato no existe."),
    })
    @PatchMapping("/platos/{id}")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @Valid @RequestBody PlatePathActiveRequestDto plateUpdateRequestDto) {
        plateHandler.updateStatusPlate(plateUpdateRequestDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Operation(summary = "Listar todos los platos de un restaurante paginados y filtrados por categoria")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Platos obtenidos", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron platos para los criterios de búsqueda", content = @Content),
    })
    @GetMapping("/restaurantes/{id}/platos")
    public ResponseEntity<List<PlateResponseDto>> getPlatesByRestaurant(
            @Parameter(description = "ID del restaurante cuyos platos se desean listar", example = "1") @PathVariable Long id,
            @Parameter(description = "Nombre de la categoría para filtrar (opcional)", example = "Entradas")  @RequestParam(required = false) String category,
            @Parameter(description = "Número de página a buscar (inicia en 0)", example = "0")  @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página", example = "5")  @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(plateHandler.getPlatesByRestaurant(id, page, size, category));
    }

}
