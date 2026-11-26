package com.notificaciones.modelo;

/**
 * Representa las políticas o reglas de envío que el usuario puede elegir.
 */
public enum TipoEnvio {
    URGENTE("Urgente (Todos los canales permitidos)"),
    INFORMATIVO("Informativo (Solo por el canal preferido)"),
    CON_REINTENTO("Con Reintento (Si falla el principal, intenta con otro)");

    private final String descripcion;

    TipoEnvio(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    @Override
    public String toString() {
        return descripcion;
    }
}
