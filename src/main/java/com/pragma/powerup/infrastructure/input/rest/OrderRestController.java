package com.pragma.powerup.infrastructure.input.rest;

import com.pragma.powerup.application.dto.request.OrderRequestDto;
import com.pragma.powerup.application.dto.request.PinRequestDto;
import com.pragma.powerup.application.dto.response.OrderResponseDto;
import com.pragma.powerup.application.handler.IOrderHandler;
import com.pragma.powerup.domain.model.OrderStatus;
import com.pragma.powerup.infrastructure.exceptionhandler.ErrorResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.EmployeePerformanceResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.OrderEfficiencyResponseDto;
import com.pragma.powerup.infrastructure.out.http.response.TraceabilityResponseDto;
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
@RequestMapping("/api/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos", description = "Operaciones relacionadas con la gestión, estados y consulta de pedidos y métricas de eficiencia.")
public class OrderRestController {
    private final IOrderHandler orderHandler;

    @Operation(
            summary = "Obtener historial de trazabilidad",
            description = "Consulta la trazabilidad (historial de cambios de estado) de un pedido específico desde el microservicio de Trazabilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos de trazabilidad del pedido", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TraceabilityResponseDto.class)))),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado (sólo clientes, empleados, o administradores)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontró trazabilidad para el pedido con ese ID", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/{orderId}/trazabilidad")
    public ResponseEntity<List<TraceabilityResponseDto>> getOrderTraceability(
            @Parameter(description = "id del pedido", example = "1") @PathVariable Long orderId) {
        return ResponseEntity.ok(orderHandler.getTraceability(orderId));
    }

    @Operation(
            summary = "Obtener métricas de eficiencia de pedidos",
            description = "Calcula y retorna las métricas de eficiencia de los pedidos (tiempos promedio) para un restaurante específico, consultando al microservicio de Trazabilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Métricas de eficiencia de los pedidos por estado", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = OrderEfficiencyResponseDto.class)))),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado (sólo propietarios)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado o sin datos de eficiencia", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/metricas/tiempos-promedio/{restaurantId}")
    public ResponseEntity<List<OrderEfficiencyResponseDto>> getOrderMetrics(
            @Parameter(description = "id del restaurant", example = "1") @PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderHandler.getOrderMetrics(restaurantId));
    }

    @Operation(
            summary = "Obtener ranking de desempeño de empleados",
            description = "Calcula y retorna la lista de empleados de un restaurante ordenados por su desempeño (pedidos finalizados), consultando al microservicio de Trazabilidad."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ranking de empleados (email y número de pedidos finalizados)",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = EmployeePerformanceResponseDto.class)))),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado (sólo propietarios)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Restaurante no encontrado o sin datos de desempeño", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping("/metricas/ranking-empleados/{restaurantId}")
    public ResponseEntity<List<EmployeePerformanceResponseDto>> getEmployeePerformances(
            @Parameter(description = "id del restaurant", example = "1") @PathVariable Long restaurantId) {
        return ResponseEntity.ok(orderHandler.getEmployeePerformances(restaurantId));
    }

    @Operation(
            summary = "Listar pedidos por estado",
            description = "Permite a un empleado de restaurante listar y paginar los pedidos disponibles en su restaurante, filtrando opcionalmente por estado."
    )    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos obtenidos", content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = OrderResponseDto.class)))),
            @ApiResponse(responseCode = "400", description = "Estado de pedido no existe", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "El usuario autenticado no tiene el rol permitido para realizar esa acción", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron pedidos para los criterios de búsqueda", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "El empleado no está asociado al restaurante ", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @GetMapping
    public ResponseEntity<List<OrderResponseDto>> getAllOrders(
            @Parameter(description = "Nombre del estado del pedido para filtrar (opcional)", example = "PENDIENTE") @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Número de página a buscar (inicia en 0)", example = "0") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Número de elementos por página", example = "5") @RequestParam(defaultValue = "5") int size) {
        return ResponseEntity.ok(orderHandler.getOrdersByStatus(status, page, size));
    }

    @Operation(
            summary = "Crear un nuevo pedido",
            description = "Permite a un cliente crear un pedido. La creación de un pedido fallará si el cliente ya tiene un pedido en estado PENDIENTE, EN_PREPARACION o LISTO."
    )    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado con éxito", content = @Content),
            @ApiResponse(responseCode = "400", description = "Los campos son obligatorios", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "403", description = "Usuario no autorizado.", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "El restaurante no existe con ese identificador.", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "Plato no pertenece a ese restaurante", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PostMapping
    public ResponseEntity<Void> saveOrder(
            @Valid @RequestBody OrderRequestDto orderRequestDto) {
        orderHandler.saveOrder(orderRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            summary = "Asignar pedido y cambiar a EN_PREPARACION",
            description = "Permite a un empleado tomar un pedido de la lista de PENDIENTE, asignarlo a sí mismo y cambiar su estado a EN_PREPARACION."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido asignado y en preparación", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no es un empleado o no pertenece al restaurante", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado PENDIENTE", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PutMapping("/{orderId}/en-preparacion")
    public ResponseEntity<Void> assignOrderPreparation(
            @Parameter(description = "id del pedido a asignarse", example = "1") @PathVariable Long orderId) {
        orderHandler.assignOrderAndChangeStatus(orderId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Cambiar el estado de un pedido a LISTO",
            description = "Permite al empleado asignado marcar un pedido como LISTO. Esto dispara una notificación al cliente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido listo para recoger/entregar. Se envía notificación.", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no es el empleado asignado o no pertenece al restaurante", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado EN_PREPARACION", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)) ),
    })
    @PutMapping("/{orderId}/listo")
    public ResponseEntity<Void> assignOrderReady(
            @Parameter(description = "id del pedido a listo", example = "1") @PathVariable Long orderId) {
        orderHandler.notifyOrderReady(orderId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Entregar un pedido (validación de PIN)",
            description = "Permite al empleado cambiar el estado del pedido a ENTREGADO si el PIN de seguridad proporcionado por el cliente es correcto. Esto finaliza el ciclo del pedido."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido entregado con éxito", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no es el empleado asignado o no pertenece al restaurante",content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)) ),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "El pedido no está en estado LISTO o el PIN es incorrecto", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PutMapping("/{orderId}/entregado")
    public ResponseEntity<Void> assignOrderDelivered(
            @Parameter(description = "id del pedido a entregar", example = "1") @PathVariable Long orderId,
            @Parameter(description = "pin del pedido a entregar", example = "2945")@Valid @RequestBody PinRequestDto request) {
        orderHandler.transitionToDelivered(orderId, request.getSecurityPin());
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Cancelar un pedido",
            description = "Permite a un cliente cancelar su propio pedido solo si el estado es PENDIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido cancelado con éxito", content = @Content),
            @ApiResponse(responseCode = "403", description = "El usuario no es el cliente que creó el pedido", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
            @ApiResponse(responseCode = "409", description = "El pedido no puede ser cancelado (ya no está en estado PENDIENTE)", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))),
    })
    @PutMapping("/{orderId}/cancelado")
    public ResponseEntity<Void> assignOrderCanceled(
            @Parameter(description = "id del pedido", example = "1") @PathVariable Long orderId) {
        orderHandler.transitionToCanceled(orderId);
        return ResponseEntity.ok().build();
    }
}
