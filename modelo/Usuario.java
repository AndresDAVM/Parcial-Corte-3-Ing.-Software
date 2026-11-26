package com.notificaciones.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Representa a un usuario o destinatario en el sistema con sus datos de contacto
 * y sus preferencias de canales aceptados.
 */
public class Usuario {
    private String id;
    private String nombre;
    private String correo;
    private String telefono;
    private CanalTipo canalPreferido;
    private List<CanalTipo> canalesPermitidos;

    public Usuario(String id, String nombre, String correo, String telefono, CanalTipo canalPreferido) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.telefono = telefono;
        this.canalPreferido = canalPreferido;
        this.canalesPermitidos = new ArrayList<>();
        // Por defecto, el canal preferido está permitido
        this.canalesPermitidos.add(canalPreferido);
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public CanalTipo getCanalPreferido() {
        return canalPreferido;
    }

    public void setCanalPreferido(CanalTipo canalPreferido) {
        this.canalPreferido = canalPreferido;
        if (!canalesPermitidos.contains(canalPreferido)) {
            canalesPermitidos.add(canalPreferido);
        }
    }

    public List<CanalTipo> getCanalesPermitidos() {
        return Collections.unmodifiableList(canalesPermitidos);
    }

    public void habilitarCanal(CanalTipo canal) {
        if (!canalesPermitidos.contains(canal)) {
            canalesPermitidos.add(canal);
        }
    }

    public void deshabilitarCanal(CanalTipo canal) {
        canalesPermitidos.remove(canal);
    }

    public boolean aceptaCanal(CanalTipo canal) {
        return canalesPermitidos.contains(canal);
    }

    @Override
    public String toString() {
        return nombre + " (" + correo + " | " + telefono + ")";
    }
}
