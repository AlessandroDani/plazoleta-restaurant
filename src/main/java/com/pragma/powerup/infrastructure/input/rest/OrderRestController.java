package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.request.PinRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.domain.model.OrderStatus;
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
    @PostMapping
    public ResponseEntity<Void> saveOrder(@Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Listar todos los pedidos de un restaurante paginados y filtrados por estado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos", content = @Content),
            @ApiResponse(responseCode = "400", description = "Estado de pedido no existe", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos para los criterios de búsqueda", content = @Content),
            @ApiResponse(responseCode = "409", description = "El empleado no está asociado al restaurante ", content = @Content),
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
            @Parameter(description = "Nombre del estado del pedido para filtrar (opcional)", example = "PENDIENTE") @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Número de página a buscar (inicia en 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página", example = "5") @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(orderHandler.getOrdersByStatus(status, page, size));
    }

    @PutMapping("/{orderId}/en-preparacion")
    public ResponseEntity<Void> assignOrderPreparation( @Parameter(description = "id del pedido a asignarse", example = "1") @PathVariable Long orderId) {
        orderHandler.assignOrderAndChangeStatus(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/listo")
    public ResponseEntity<Void> assignOrderReady( @Parameter(description = "id del pedido a asignarse", example = "1") @PathVariable Long orderId) {
        orderHandler.notifyOrderReady(orderId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{orderId}/entregado")
    public ResponseEntity<Void> assignOrderDelivered(
            @Parameter(description = "id del pedido a entregar", example = "1") @PathVariable Long orderId,
            @Parameter(description = "pin del pedido a entregar", example = "2945")@Valid @RequestBody PinRequestDto request) {
        orderHandler.transitionToDelivered(orderId, request.getSecurityPin());
        return ResponseEntity.ok().build();
    }
    @PutMapping("/{orderId}/cancelado")
    public ResponseEntity<Void> assignOrderCanceled(
            @Parameter(description = "id del pedido", example = "1") @PathVariable Long orderId) {
        orderHandler.transitionToCanceled(orderId);
        return ResponseEntity.ok().build();
    }
}
