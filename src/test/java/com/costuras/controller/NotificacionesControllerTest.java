package com.costuras.controller; 
import com.costuras.notificaciones.NotificacionesApplication; 
import com.costuras.notificaciones.controller.NotificacionesController;
import com.costuras.notificaciones.dto.NotificacionCitaRequest;
import com.costuras.notificaciones.dto.NotificacionVentaRequest;
import com.costuras.notificaciones.service.NotificacionesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration; // <-- IMPORTANTE
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings("null")
@WebMvcTest(NotificacionesController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = NotificacionesApplication.class) // <-- 2. SOLUCIÓN: Apunta a tu main class del módulo
class NotificacionesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NotificacionesService notificacionesService;

@Test
    void notificarVenta_datosValidos_retorna200() throws Exception {
        NotificacionVentaRequest request = new NotificacionVentaRequest();
        request.setIdVenta("venta_001");
        request.setIdCliente(123); 
        request.setEmailCliente("cliente@mail.com");
        request.setNombreCliente("Juan");
        
        // Campos opcionales (por si acaso)
        request.setTotal(new java.math.BigDecimal("15000"));
        request.setEstado("COMPRADO");

        doNothing().when(notificacionesService).notificarVenta(any(NotificacionVentaRequest.class));

        mockMvc.perform(post("/notificaciones/venta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
       
    }


    @Test
    void notificarVenta_emailNulo_retorna400() throws Exception {
        NotificacionVentaRequest request = new NotificacionVentaRequest();
        

        doThrow(new RuntimeException("Email requerido"))
                .when(notificacionesService).notificarVenta(request);

        mockMvc.perform(post("/notificaciones/venta")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    
    }


   @Test
    void notificarCita_datosValidos_retorna200() throws Exception {
        NotificacionCitaRequest request = new NotificacionCitaRequest();
        
       
        request.setIdReserva("reserva_abc123");
        request.setIdCliente(456);
        request.setEmailCliente("cliente@mail.com");
        request.setNombreCliente("Carlos");
        request.setFecha(java.time.LocalDate.now());            
        request.setHoraInicio(java.time.LocalTime.of(10, 0));  
        request.setTipoNotificacion("CONFIRMACION");           

       
        doNothing().when(notificacionesService).notificarCita(any(NotificacionCitaRequest.class));

       
        mockMvc.perform(post("/notificaciones/cita")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }

@Test
    void notificarCita_servicioFalla_retorna500() throws Exception {
        NotificacionCitaRequest request = new NotificacionCitaRequest();
        
        
        request.setIdReserva("reserva_abc123");
        request.setIdCliente(456);
        request.setEmailCliente("cliente@mail.com");
        request.setNombreCliente("Carlos");
        request.setFecha(java.time.LocalDate.now());
        request.setHoraInicio(java.time.LocalTime.of(10, 0));
        request.setTipoNotificacion("CONFIRMACION");

       
        doThrow(new RuntimeException("Error interno al enviar correo"))
                .when(notificacionesService).notificarCita(any(NotificacionCitaRequest.class));

        
        mockMvc.perform(post("/notificaciones/cita")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is5xxServerError());
    }
}



