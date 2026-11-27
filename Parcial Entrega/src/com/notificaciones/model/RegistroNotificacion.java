package com.notificaciones.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroNotificacion {
    private static int contador = 1;

    private int id;
    private String idUsuario;
    private String nombreDestinatario;
    private TipoCanal canal;
    private LocalDateTime fechaHora;
    private EstadoEnvio estado;
    private String detalle;

    public RegistroNotificacion(String idUsuario, String nombreDestinatario, TipoCanal canal, EstadoEnvio estado, String detalle) {
        this.id = contador++;
        this.idUsuario = idUsuario;
        this.nombreDestinatario = nombreDestinatario;
        this.canal = canal;
        this.fechaHora = LocalDateTime.now();
        this.estado = estado;
        this.detalle = detalle;
    }

    public int getId() {
        return id;
    }

    public String getIdUsuario() {
        return idUsuario;
    }

    public String getNombreDestinatario() {
        return nombreDestinatario;
    }

    public TipoCanal getCanal() {
        return canal;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public EstadoEnvio getEstado() {
        return estado;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getFechaHoraFormateada() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return fechaHora.format(fmt);
    }

    @Override
    public String toString() {
        return String.format("#%03d | %s | %-12s | %-8s | %-7s | %s",
                id, getFechaHoraFormateada(), nombreDestinatario, canal, estado, detalle);
    }
}
