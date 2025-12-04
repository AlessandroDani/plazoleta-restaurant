package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.dto.request.PlateUpdateRequestDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/plate")
@RequiredArgsConstructor
public class PlateRestController {
    private final  IPlateHandler plateHandler;


    @Operation(summary = "Agregar un nuevo plato")
    @PostMapping
    public ResponseEntity<Void> savePlate(@Valid @RequestBody PlateRequestDto plateRequestDto) {
        plateHandler.savePlate(plateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Modificar un plato")
    @PutMapping("/{id}")
    public ResponseEntity<Void> updatePlate(@PathVariable Long id, @Valid @RequestBody PlateUpdateRequestDto plateUpdateRequestDto) {
        Long idOwnerRequest = 1L;
        plateHandler.updatePlate(id, plateUpdateRequestDto, idOwnerRequest);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
