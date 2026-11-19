package com.notificaciones.strategy;

import com.notificaciones.adapter.CanalNotificacion;
import com.notificaciones.model.EstadoEnvio;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import java.util.List;

public class EstrategiaNormal implements EstrategiaEnvio {

    @Override
    public String getNombre() {
        return "NORMAL";
    }

    @Override
    public ResultadoDespacho despachar(Mensaje mensaje, Usuario usuario, List<CanalNotificacion> canales) {
        TipoCanal preferido = usuario.getCanalPreferido();
        System.out.println("[Estrategia Normal] Enviando a canal preferido (" + preferido + ") de " + usuario.getNombre());

        CanalNotificacion canalSeleccionado = null;
        for (CanalNotificacion c : canales) {
            if (c.getTipo() == preferido && usuario.tieneCanalHabilitado(c.getTipo())) {
                canalSeleccionado = c;
                break;
            }
        }

        if (canalSeleccionado == null) {
            String error = "Canal preferido " + preferido + " no disponible o no habilitado.";
            ResultadoDespacho resError = new ResultadoDespacho(false, error);
            resError.agregarRegistro(new RegistroNotificacion(
                    usuario.getId(),
                    usuario.getNombre(),
                    preferido,
                    EstadoEnvio.FALLIDO,
                    error
            ));
            return resError;
        }

        boolean ok = canalSeleccionado.enviar(mensaje, usuario);
        EstadoEnvio estado = ok ? EstadoEnvio.EXITOSO : EstadoEnvio.FALLIDO;
        String detalle = ok ? "Entregado por " + preferido : "Fallo en canal " + preferido;

        ResultadoDespacho res = new ResultadoDespacho(ok, detalle);
        res.agregarRegistro(new RegistroNotificacion(
                usuario.getId(),
                usuario.getNombre(),
                preferido,
                estado,
                detalle
        ));
        return res;
    }
}
