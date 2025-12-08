package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
public class OrderRestController {
    private final IOrderHandler orderHandler;


    @Operation(summary = "Agregar un nuevo pedido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios"),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado."),
            @ApiResponse(responseCode = "404", description = "El restaurante no existe con ese identificador."),
            @ApiResponse(responseCode = "409", description = "Plato no pertenece a ese restaurante"),
    })
    @PostMapping()
    public ResponseEntity<Void> saveOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
