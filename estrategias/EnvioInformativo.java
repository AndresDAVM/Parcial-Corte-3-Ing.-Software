package com.notificaciones.estrategias;

import java.util.Map;
import com.notificaciones.canales.CanalNotificacion;
import com.notificaciones.historial.GestorHistorial;
import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.TipoEnvio;
import com.notificaciones.modelo.Usuario;

/**
 * ESTRATEGIA CONCRETA: Envío Informativo.
 * Envía el mensaje ÚNICAMENTE por el canal preferido del usuario.
 * Si el canal preferido no está permitido o no existe, toma el primer canal permitido.
 */
public class EnvioInformativo implements EstrategiaEnvio {

    @Override
    public TipoEnvio getTipo() {
        return TipoEnvio.INFORMATIVO;
    }

    @Override
    public boolean despachar(Usuario usuario, Mensaje mensaje, 
                             Map<CanalTipo, CanalNotificacion> canales, 
                             GestorHistorial historial) {
        System.out.println("\n>>> [ESTRATEGIA INFORMATIVO] Despacho único para: " + usuario.getNombre());
        CanalTipo canalElegido = usuario.getCanalPreferido();

        // Si el preferido no está en permitidos, buscar el primero permitido
        if (!usuario.aceptaCanal(canalElegido)) {
            if (!usuario.getCanalesPermitidos().isEmpty()) {
                canalElegido = usuario.getCanalesPermitidos().get(0);
                System.out.println("(!) El canal preferido no está permitido. Usando canal alternativo: " + canalElegido.getNombreLegible());
            } else {
                System.out.println("[INFORMATIVO] El usuario no tiene ningún canal habilitado.");
                return false;
            }
        }

        CanalNotificacion canal = canales.get(canalElegido);
        if (canal == null) {
            System.out.println("[INFORMATIVO] El canal " + canalElegido.getNombreLegible() + " no está disponible en la plataforma.");
            return false;
        }

        String cuerpo = mensaje.obtenerTextoPersonalizado(usuario);
        System.out.println("-> Enviando mensaje por canal preferido: " + canalElegido.getNombreLegible());
        boolean ok = canal.enviar(usuario, mensaje);

        if (ok) {
            historial.registrar(usuario.getNombre(), canalElegido, mensaje.getAsunto(), cuerpo, true, "Envío informativo entregado correctamente");
            System.out.println("<<< [INFORMATIVO] Notificación entregada con éxito.\n");
            return true;
        } else {
            historial.registrar(usuario.getNombre(), canalElegido, mensaje.getAsunto(), cuerpo, false, "Error en el canal preferido");
            System.out.println("<<< [INFORMATIVO] Falló la entrega del mensaje informativo.\n");
            return false;
        }
    }
}
