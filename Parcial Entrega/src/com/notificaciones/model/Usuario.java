package com.notificaciones.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Usuario {
    private String id;
    private String nombre;
    private String email;
    private String telefono;
    private List<TipoCanal> canalesHabilitados;
    private TipoCanal canalPreferido;

    public Usuario(String id, String nombre, String email, String telefono, TipoCanal canalPreferido) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.canalPreferido = canalPreferido;
        this.canalesHabilitados = new ArrayList<>();
        if (canalPreferido != null) {
            this.canalesHabilitados.add(canalPreferido);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public TipoCanal getCanalPreferido() {
        return canalPreferido;
    }

    public void setCanalPreferido(TipoCanal canalPreferido) {
        this.canalPreferido = canalPreferido;
        if (canalPreferido != null && !this.canalesHabilitados.contains(canalPreferido)) {
            this.canalesHabilitados.add(canalPreferido);
        }
    }

    public List<TipoCanal> getCanalesHabilitados() {
        return Collections.unmodifiableList(canalesHabilitados);
    }

    public void habilitarCanal(TipoCanal canal) {
        if (canal != null && !canalesHabilitados.contains(canal)) {
            canalesHabilitados.add(canal);
        }
    }

    public void deshabilitarCanal(TipoCanal canal) {
        canalesHabilitados.remove(canal);
    }

    public boolean tieneCanalHabilitado(TipoCanal canal) {
        return canalesHabilitados.contains(canal);
    }

    @Override
    public String toString() {
        return nombre + " (" + id + ") - " + canalPreferido;
    }
}
