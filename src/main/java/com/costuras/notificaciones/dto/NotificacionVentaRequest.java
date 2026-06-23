package com.costuras.notificaciones.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacionVentaRequest {

    @NotBlank(message = "El idVenta es obligatorio")
    private String idVenta;

    @NotNull(message = "El idCliente es obligatorio")
    private Integer idCliente;

    @NotBlank(message = "El email del cliente es obligatorio")
    private String emailCliente;

    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;

    private BigDecimal total;
    private String estado;
}
