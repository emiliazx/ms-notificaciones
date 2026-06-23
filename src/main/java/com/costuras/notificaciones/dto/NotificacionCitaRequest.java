package com.costuras.notificaciones.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionCitaRequest {

    @NotBlank(message = "El idReserva es obligatorio")
    private String idReserva;

    @NotNull(message = "El idCliente es obligatorio")
    private Integer idCliente;

    @NotBlank(message = "El email del cliente es obligatorio")
    private String emailCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotNull(message = "La hora de inicio es obligatoria")
    private LocalTime horaInicio;

    private LocalTime horaFin;
    private String descripcion;

    @NotBlank(message = "El tipo de notificación es obligatorio")
    private String tipoNotificacion;

    private String motivoCancelacion;
}