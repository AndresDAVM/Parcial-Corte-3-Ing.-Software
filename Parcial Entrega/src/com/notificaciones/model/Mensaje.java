package com.notificaciones.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensaje {
    private String asunto;
    private String cuerpo;
    private LocalDateTime fechaCreacion;

    public Mensaje(String asunto, String cuerpo) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Mensaje(String asunto, String cuerpo, LocalDateTime fechaCreacion) {
        this.asunto = asunto;
        this.cuerpo = cuerpo;
        this.fechaCreacion = fechaCreacion;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getCuerpo() {
        return cuerpo;
    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Mensaje personalizarPara(Usuario usuario) {
        String texto = this.cuerpo;
        if (usuario != null) {
            texto = texto.replace("{nombre}", usuario.getNombre());
            texto = texto.replace("{email}", usuario.getEmail());
            texto = texto.replace("{telefono}", usuario.getTelefono());
        }
        
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        texto = texto.replace("{fecha}", LocalDateTime.now().format(fmt));

        return new Mensaje(this.asunto, texto, this.fechaCreacion);
    }

    @Override
    public String toString() {
        return "[" + asunto + "] " + cuerpo;
    }
}
