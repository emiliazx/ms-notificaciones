package com.costuras.notificaciones.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailService {

    private final Resend resend;

    @Value("${resend.from}")
    private String fromEmail;

    public EmailService(@Value("${resend.api-key}") String apiKey) {
        this.resend = new Resend(apiKey);
    }

    
   
    
    public void enviarConfirmacionVenta(
            String emailDestino,
            String nombreCliente,
            String idVenta,
            String total,
            String estado
    ) {
        String asunto = "Confirmación de tu compra - " + idVenta;
        String html = buildHtmlVenta(nombreCliente, idVenta, total, estado);
        enviar(emailDestino, asunto, html);
    }

    
  
     
    public void enviar(String para, String asunto, String html) {
       log.info("Enviando email a: '{}' desde: '{}'", para, fromEmail);
        try {
            CreateEmailOptions options = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(para)
                    .subject(asunto)
                    .html(html)
                    .build();

            CreateEmailResponse response = resend.emails().send(options);
            log.info("Email enviado correctamente. ID Resend: {}", response.getId());

        } catch (ResendException e) {
            log.error("Error al enviar email a {}: {}", para, e.getMessage());
           
        }
    }

 

    private String buildHtmlVenta(String nombre, String idVenta, String total, String estado) {
        return """
                <!DOCTYPE html>
                <html>
                <body style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
                  <h2 style="color: #6a1b9a;">¡Gracias por tu compra, %s!</h2>
                  <p>Tu pedido ha sido registrado correctamente.</p>
                  <table style="border-collapse: collapse; width: 100%%;">
                    <tr>
                      <td style="padding: 8px; border: 1px solid #ddd;"><strong>N° de venta</strong></td>
                      <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                    </tr>
                    <tr>
                      <td style="padding: 8px; border: 1px solid #ddd;"><strong>Total</strong></td>
                      <td style="padding: 8px; border: 1px solid #ddd;">$%s</td>
                    </tr>
                    <tr>
                      <td style="padding: 8px; border: 1px solid #ddd;"><strong>Estado</strong></td>
                      <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                    </tr>
                  </table>
                  <p style="margin-top: 20px;">Si tienes dudas contáctanos.</p>
                  <p><em>Equipo Costuras</em></p>
                </body>
                </html>
                """.formatted(nombre, idVenta, total, estado);

    }

    public void enviarConfirmacionCita(String emailDestino, String nombreCliente,
        String idReserva, String fecha, String horaInicio,
        String horaFin, String descripcion) {
    String asunto = "Tu cita ha sido confirmada - " + fecha;
    String html = buildHtmlCitaConfirmada(nombreCliente, idReserva, fecha, horaInicio, horaFin, descripcion);
    enviar(emailDestino, asunto, html);
}

public void enviarCancelacionCita(String emailDestino, String nombreCliente,
        String idReserva, String fecha, String horaInicio, String motivo) {
    String asunto = "Tu cita ha sido cancelada - " + fecha;
    String html = buildHtmlCitaCancelada(nombreCliente, idReserva, fecha, horaInicio, motivo);
    enviar(emailDestino, asunto, html);
}

private String buildHtmlCitaConfirmada(String nombre, String idReserva, String fecha,
                                        String horaInicio, String horaFin, String descripcion) {
    return """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
              <h2 style="color: #6a1b9a;">¡Tu cita está confirmada, %s!</h2>
              <p>Hemos registrado tu cita correctamente.</p>
              <table style="border-collapse: collapse; width: 100%%;">
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>N° de reserva</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Fecha</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Horario</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s - %s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Descripción</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
              </table>
              <p style="margin-top: 20px;">Si necesitas cancelar, puedes hacerlo desde tu cuenta.</p>
              <p><em>Equipo Costuras</em></p>
            </body>
            </html>
            """.formatted(nombre, idReserva, fecha, horaInicio, horaFin,
                          descripcion != null ? descripcion : "Sin descripción");
}

private String buildHtmlCitaCancelada(String nombre, String idReserva, String fecha,
                                       String horaInicio, String motivo) {
    return """
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; padding: 20px; color: #333;">
              <h2 style="color: #c62828;">Tu cita ha sido cancelada, %s</h2>
              <p>Te informamos que la siguiente cita ha sido cancelada.</p>
              <table style="border-collapse: collapse; width: 100%%;">
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>N° de reserva</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Fecha</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Hora</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
                <tr>
                  <td style="padding: 8px; border: 1px solid #ddd;"><strong>Motivo</strong></td>
                  <td style="padding: 8px; border: 1px solid #ddd;">%s</td>
                </tr>
              </table>
              <p style="margin-top: 20px;">Puedes agendar una nueva cita cuando gustes.</p>
              <p><em>Equipo Costuras</em></p>
            </body>
            </html>
            """.formatted(nombre, idReserva, fecha, horaInicio,
                          motivo != null ? motivo : "Sin motivo especificado");
}
}
