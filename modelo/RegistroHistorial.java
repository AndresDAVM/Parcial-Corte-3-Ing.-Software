package com.notificaciones.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa una entrada en el historial de envíos de la plataforma.
 */
public class RegistroHistorial {
    private static int contador = 1;

    private int id;
    private LocalDateTime fechaHora;
    private String destinatario;
    private CanalTipo canal;
    private String asunto;
    private String contenidoEnviado;
    private boolean exitoso;
    private String detalleEstado;

    public RegistroHistorial(String destinatario, CanalTipo canal, String asunto, 
                             String contenidoEnviado, boolean exitoso, String detalleEstado) {
        this.id = contador++;
        this.fechaHora = LocalDateTime.now();
        this.destinatario = destinatario;
        this.canal = canal;
        this.asunto = asunto;
        this.contenidoEnviado = contenidoEnviado;
        this.exitoso = exitoso;
        this.detalleEstado = detalleEstado;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public String getFechaHoraFormateada() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fechaHora.format(fmt);
    }

    public String getDestinatario() {
        return destinatario;
    }

    public CanalTipo getCanal() {
        return canal;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getContenidoEnviado() {
        return contenidoEnviado;
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getDetalleEstado() {
        return detalleEstado;
    }

    @Override
    public String toString() {
        return String.format("[%s] ID:%d | %s | Canal: %s | Estado: %s | %s",
                getFechaHoraFormateada(), id, destinatario, canal.getNombreLegible(),
                (exitoso ? "EXITOSO" : "FALLIDO"), detalleEstado);
    }
}
