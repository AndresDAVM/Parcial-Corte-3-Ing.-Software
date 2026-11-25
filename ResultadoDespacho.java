package com.notificaciones.strategy;

import com.notificaciones.model.RegistroNotificacion;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ResultadoDespacho {
    private boolean exitoso;
    private String mensajeResumen;
    private List<RegistroNotificacion> registros;

    public ResultadoDespacho(boolean exitoso, String mensajeResumen) {
        this.exitoso = exitoso;
        this.mensajeResumen = mensajeResumen;
        this.registros = new ArrayList<>();
    }

    public void agregarRegistro(RegistroNotificacion registro) {
        if (registro != null) {
            registros.add(registro);
        }
    }

    public boolean isExitoso() {
        return exitoso;
    }

    public String getMensajeResumen() {
        return mensajeResumen;
    }

    public List<RegistroNotificacion> getRegistros() {
        return Collections.unmodifiableList(registros);
    }
}
