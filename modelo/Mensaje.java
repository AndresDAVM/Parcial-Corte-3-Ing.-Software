package com.notificaciones.modelo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Representa el contenido del mensaje y permite personalizar plantillas.
 */
public class Mensaje {
    private String asunto;
    private String plantillaCuerpo;
    private LocalDateTime fechaCreacion;

    public Mensaje(String asunto, String plantillaCuerpo) {
        this.asunto = asunto;
        this.plantillaCuerpo = plantillaCuerpo;
        this.fechaCreacion = LocalDateTime.now();
    }

    public String getAsunto() {
        return asunto;
    }

    public String getPlantillaCuerpo() {
        return plantillaCuerpo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Personaliza el texto sustituyendo variables como {nombre}, {correo}, {telefono} y {fecha}.
     */
    public String obtenerTextoPersonalizado(Usuario usuario) {
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String texto = plantillaCuerpo;
        texto = texto.replace("{nombre}", usuario.getNombre());
        texto = texto.replace("{correo}", usuario.getCorreo());
        texto = texto.replace("{telefono}", usuario.getTelefono());
        texto = texto.replace("{fecha}", fechaCreacion.format(formateador));
        return texto;
    }
}
