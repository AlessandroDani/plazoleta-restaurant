package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/plate")
@RequiredArgsConstructor
public class PlateRestController {
    private final  IPlateHandler plateHandler;


    @Operation(summary = "Agregar un nuevo plato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Plato creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado o rol inválido."),
            @ApiResponse(responseCode = "404", description = "El restaurante asociado no existe."),
            @ApiResponse(responseCode = "503", description = "Servicio de usuarios no disponible.")
    })
    @PostMapping
    public ResponseEntity<Void> savePlate(@Valid @RequestBody PlateRequestDto plateRequestDto) {
        plateHandler.savePlate(plateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar un plato (descripcion y precio)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Plato actualizado con éxito."),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos."),
            @ApiResponse(responseCode = "403", description = "El usuario no es propietario del restaurante."),
            @ApiResponse(responseCode = "404", description = "El plato no existe."),
            @ApiResponse(responseCode = "503", description = "Servicio de usuarios no disponible.")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlate(@PathVariable Long id, @Valid @RequestBody PlateUpdateRequestDto plateUpdateRequestDto) {
        plateHandler.updatePlate(plateUpdateRequestDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
