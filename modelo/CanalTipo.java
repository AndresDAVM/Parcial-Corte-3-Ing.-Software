package com.notificaciones.modelo;

/**
 * Enum que representa los tipos de canales disponibles en la plataforma.
 */
public enum CanalTipo {
    EMAIL("Correo Electrónico"),
    SMS("Mensaje de Texto (SMS)"),
    WHATSAPP("WhatsApp");

    private final String nombreLegible;

    CanalTipo(String nombreLegible) {
        this.nombreLegible = nombreLegible;
    }

    public String getNombreLegible() {
        return nombreLegible;
    }

    @Override
    public String toString() {
        return nombreLegible;
    }
}
