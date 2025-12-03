package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.PlateRequestDto;
import com.pragma.powerup.application.handler.IPlateHandler;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
