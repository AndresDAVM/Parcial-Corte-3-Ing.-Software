package com.notificaciones.strategy;

import com.notificaciones.adapter.CanalNotificacion;
import com.notificaciones.model.EstadoEnvio;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import java.util.ArrayList;
import java.util.List;

public class EstrategiaConReintento implements EstrategiaEnvio {

    @Override
    public String getNombre() {
        return "REINTENTO";
    }

    @Override
    public ResultadoDespacho despachar(Mensaje mensaje, Usuario usuario, List<CanalNotificacion> canales) {
        System.out.println("[Estrategia Reintento] Despachando a " + usuario.getNombre());

        List<CanalNotificacion> listaOrdenada = new ArrayList<>();
        TipoCanal preferido = usuario.getCanalPreferido();

        // Ponemos el canal preferido al inicio, luego los demas
        for (CanalNotificacion c : canales) {
            if (c.getTipo() == preferido && usuario.tieneCanalHabilitado(c.getTipo())) {
                listaOrdenada.add(0, c);
            } else if (usuario.tieneCanalHabilitado(c.getTipo())) {
                listaOrdenada.add(c);
            }
        }

        if (listaOrdenada.isEmpty()) {
            return new ResultadoDespacho(false, "Sin canales habilitados.");
        }

        ResultadoDespacho res = new ResultadoDespacho(false, "");
        boolean seEnvio = false;

        for (int i = 0; i < listaOrdenada.size(); i++) {
            CanalNotificacion canal = listaOrdenada.get(i);
            System.out.println("  Intento #" + (i + 1) + " por " + canal.getTipo() + "...");
            boolean ok = canal.enviar(mensaje, usuario);

            if (ok) {
                seEnvio = true;
                String detalle = "Entregado en intento #" + (i + 1) + " via " + canal.getTipo();
                res.agregarRegistro(new RegistroNotificacion(
                        usuario.getId(),
                        usuario.getNombre(),
                        canal.getTipo(),
                        EstadoEnvio.EXITOSO,
                        detalle
                ));
                break;
            } else {
                String detalle = "Fallo intento #" + (i + 1) + " via " + canal.getTipo() + ". Reintentando...";
                res.agregarRegistro(new RegistroNotificacion(
                        usuario.getId(),
                        usuario.getNombre(),
                        canal.getTipo(),
                        EstadoEnvio.FALLIDO,
                        detalle
                ));
            }
        }

        String msgFinal = seEnvio ? "Notificacion entregada con reintento." : "Fallo el envio en todos los canales.";
        ResultadoDespacho finalRes = new ResultadoDespacho(seEnvio, msgFinal);
        for (RegistroNotificacion r : res.getRegistros()) {
            finalRes.agregarRegistro(r);
        }
        return finalRes;
    }
}
