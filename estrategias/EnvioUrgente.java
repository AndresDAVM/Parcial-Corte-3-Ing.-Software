package com.notificaciones.estrategias;

import java.util.List;
import java.util.Map;
import com.notificaciones.canales.CanalNotificacion;
import com.notificaciones.historial.GestorHistorial;
import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.TipoEnvio;
import com.notificaciones.modelo.Usuario;

/**
 * ESTRATEGIA CONCRETA: Envío Urgente.
 * Envía el mensaje simultáneamente por TODOS los canales que el usuario tenga permitidos.
 */
public class EnvioUrgente implements EstrategiaEnvio {

    @Override
    public TipoEnvio getTipo() {
        return TipoEnvio.URGENTE;
    }

    @Override
    public boolean despachar(Usuario usuario, Mensaje mensaje, 
                             Map<CanalTipo, CanalNotificacion> canales, 
                             GestorHistorial historial) {
        System.out.println("\n>>> [ESTRATEGIA URGENTE] Iniciando despacho multicanal para: " + usuario.getNombre());
        List<CanalTipo> permitidos = usuario.getCanalesPermitidos();

        if (permitidos.isEmpty()) {
            System.out.println("[URGENTE] El usuario no tiene ningún canal permitido configurado.");
            return false;
        }

        int exitosos = 0;
        int intentados = 0;
        String cuerpo = mensaje.obtenerTextoPersonalizado(usuario);

        for (CanalTipo tipo : permitidos) {
            CanalNotificacion canal = canales.get(tipo);
            if (canal != null) {
                intentados++;
                System.out.println("-> Despachando por canal: " + tipo.getNombreLegible());
                boolean ok = canal.enviar(usuario, mensaje);

                if (ok) {
                    exitosos++;
                    historial.registrar(usuario.getNombre(), tipo, mensaje.getAsunto(), cuerpo, true, "Envío urgente completado con éxito");
                } else {
                    historial.registrar(usuario.getNombre(), tipo, mensaje.getAsunto(), cuerpo, false, "Fallo en canal durante despacho urgente");
                }
            }
        }

        System.out.println("<<< [URGENTE] Resultado: " + exitosos + " de " + intentados + " canales entregaron el mensaje.\n");
        return exitosos > 0;
    }
}
