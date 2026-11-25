package com.notificaciones.strategy;

import com.notificaciones.adapter.CanalNotificacion;
import com.notificaciones.model.EstadoEnvio;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.Usuario;
import java.util.List;

public class EstrategiaUrgente implements EstrategiaEnvio {

    @Override
    public String getNombre() {
        return "URGENTE";
    }

    @Override
    public ResultadoDespacho despachar(Mensaje mensaje, Usuario usuario, List<CanalNotificacion> canales) {
        System.out.println("[Estrategia Urgente] Enviando por todos los canales a: " + usuario.getNombre());
        
        int exitos = 0;
        int total = 0;
        ResultadoDespacho res = new ResultadoDespacho(false, "");

        for (CanalNotificacion canal : canales) {
            if (usuario.tieneCanalHabilitado(canal.getTipo())) {
                total++;
                boolean ok = canal.enviar(mensaje, usuario);
                EstadoEnvio estado = ok ? EstadoEnvio.EXITOSO : EstadoEnvio.FALLIDO;
                String detalle = ok ? "Entregado por " + canal.getTipo() : "Fallo en canal " + canal.getTipo();

                if (ok) exitos++;

                res.agregarRegistro(new RegistroNotificacion(
                        usuario.getId(),
                        usuario.getNombre(),
                        canal.getTipo(),
                        estado,
                        detalle
                ));
            }
        }

        if (total == 0) {
            return new ResultadoDespacho(false, "El usuario no tiene canales habilitados.");
        }

        boolean logroEnvio = exitos > 0;
        String resumen = "Urgente: entregado en " + exitos + " de " + total + " canales.";
        
        ResultadoDespacho finalRes = new ResultadoDespacho(logroEnvio, resumen);
        for (RegistroNotificacion r : res.getRegistros()) {
            finalRes.agregarRegistro(r);
        }
        return finalRes;
    }
}
