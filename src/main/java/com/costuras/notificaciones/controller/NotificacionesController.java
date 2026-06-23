package com.costuras.notificaciones.controller;

import com.costuras.notificaciones.dto.NotificacionCitaRequest;
import com.costuras.notificaciones.dto.NotificacionVentaRequest;
import com.costuras.notificaciones.service.NotificacionesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/notificaciones")
@RequiredArgsConstructor
@Tag(name = "Notificaciones", description = "Endpoints internos para envío de emails a clientes")
public class NotificacionesController {

    private final NotificacionesService notificacionesService;

    @Operation(summary = "Notificar venta",
               description = "Envía un email de confirmación de compra al cliente. Usado internamente por el microservicio de Ventas.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación enviada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la notificación inválidos"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/venta")
    public ResponseEntity<Map<String, String>> notificarVenta(
            @Valid @RequestBody NotificacionVentaRequest request) {
        notificacionesService.notificarVenta(request);
        return ResponseEntity.ok(Map.of("mensaje", "Notificación enviada correctamente"));
    }

    @Operation(summary = "Notificar cita",
               description = "Envía un email de confirmación o cancelación de cita. Tipos soportados: AGENDADA, CANCELADA.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Notificación de cita enviada correctamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o tipo de notificación desconocido"),
        @ApiResponse(responseCode = "401", description = "No autorizado")
    })
    @PostMapping("/cita")
    public ResponseEntity<Map<String, String>> notificarCita(
            @Valid @RequestBody NotificacionCitaRequest request) {
        notificacionesService.notificarCita(request);
        return ResponseEntity.ok(Map.of("mensaje", "Notificación de cita enviada correctamente"));
    }
}