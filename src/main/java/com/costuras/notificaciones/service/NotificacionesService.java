package com.costuras.notificaciones.service;

import com.costuras.notificaciones.dto.NotificacionCitaRequest;
import com.costuras.notificaciones.dto.NotificacionVentaRequest;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificacionesService {

    private final EmailService emailService;

    
   
     
    public void notificarVenta(NotificacionVentaRequest request) {
        log.info("Procesando notificación de venta {} para cliente {}",
                request.getIdVenta(), request.getEmailCliente());

        emailService.enviarConfirmacionVenta(
                request.getEmailCliente(),
                request.getNombreCliente(),
                request.getIdVenta(),
                request.getTotal() != null ? request.getTotal().toPlainString() : "0",
                request.getEstado()
        );
    }
    public void notificarCita(NotificacionCitaRequest request) {
    log.info("Procesando notificación de cita {} tipo {} para cliente {}",
            request.getIdReserva(), request.getTipoNotificacion(), request.getEmailCliente());

    switch (request.getTipoNotificacion()) {
        case "AGENDADA" -> emailService.enviarConfirmacionCita(
                request.getEmailCliente(),
                request.getNombreCliente(),
                request.getIdReserva(),
                request.getFecha().toString(),
                request.getHoraInicio().toString(),
                request.getHoraFin() != null ? request.getHoraFin().toString() : "",
                request.getDescripcion()
        );
        case "CANCELADA" -> emailService.enviarCancelacionCita(
                request.getEmailCliente(),
                request.getNombreCliente(),
                request.getIdReserva(),
                request.getFecha().toString(),
                request.getHoraInicio().toString(),
                request.getMotivoCancelacion()
        );
        default -> log.warn("Tipo de notificación de cita desconocido: {}", request.getTipoNotificacion());
    }
}
}
