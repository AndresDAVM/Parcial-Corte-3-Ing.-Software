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
 * ESTRATEGIA CONCRETA: Envío con Reintento (Fallback).
 * Intenta primero por el canal preferido. Si ese canal falla, intenta automáticamente
 * por los demás canales que el usuario tenga permitidos hasta que uno responda con éxito.
 */
public class EnvioConReintento implements EstrategiaEnvio {

    @Override
    public TipoEnvio getTipo() {
        return TipoEnvio.CON_REINTENTO;
    }

    @Override
    public boolean despachar(Usuario usuario, Mensaje mensaje, 
                             Map<CanalTipo, CanalNotificacion> canales, 
                             GestorHistorial historial) {
        System.out.println("\n>>> [ESTRATEGIA REINTENTO / FALLBACK] Despacho tolerante a fallos para: " + usuario.getNombre());
        List<CanalTipo> permitidos = usuario.getCanalesPermitidos();

        if (permitidos.isEmpty()) {
            System.out.println("[REINTENTO] El usuario no tiene canales permitidos.");
            return false;
        }

        String cuerpo = mensaje.obtenerTextoPersonalizado(usuario);

        // 1. Intentar primero con el canal preferido si está permitido
        CanalTipo preferido = usuario.getCanalPreferido();
        if (usuario.aceptaCanal(preferido) && canales.containsKey(preferido)) {
            System.out.println("-> [Intento 1] Probando canal principal: " + preferido.getNombreLegible());
            CanalNotificacion canalPrincipal = canales.get(preferido);
            boolean ok = canalPrincipal.enviar(usuario, mensaje);

            if (ok) {
                historial.registrar(usuario.getNombre(), preferido, mensaje.getAsunto(), cuerpo, true, "Entregado en primer intento (Canal Principal)");
                System.out.println("<<< [REINTENTO] Envío exitoso en el canal principal.\n");
                return true;
            } else {
                System.out.println("(!) Falló el canal principal (" + preferido.getNombreLegible() + "). Iniciando reintentos por canales alternativos...");
                historial.registrar(usuario.getNombre(), preferido, mensaje.getAsunto(), cuerpo, false, "Fallo inicial en canal principal");
            }
        }

        // 2. Si falló el preferido o no estaba, probar los demás canales permitidos
        int intento = 2;
        for (CanalTipo alternativo : permitidos) {
            if (alternativo.equals(preferido)) {
                continue; // Ya se intentó arriba
            }

            CanalNotificacion canalAlt = canales.get(alternativo);
            if (canalAlt != null) {
                System.out.println("-> [Intento " + intento + "] Reintentando por: " + alternativo.getNombreLegible());
                boolean ok = canalAlt.enviar(usuario, mensaje);

                if (ok) {
                    historial.registrar(usuario.getNombre(), alternativo, mensaje.getAsunto(), cuerpo, true, 
                                        "Recuperado exitosamente en intento " + intento + " vía " + alternativo.getNombreLegible());
                    System.out.println("<<< [REINTENTO] Mensaje entregado con éxito por canal alternativo.\n");
                    return true;
                } else {
                    System.out.println("(!) Canal alternativo " + alternativo.getNombreLegible() + " también falló.");
                    historial.registrar(usuario.getNombre(), alternativo, mensaje.getAsunto(), cuerpo, false, 
                                        "Fallo en intento " + intento + " vía " + alternativo.getNombreLegible());
                }
                intento++;
            }
        }

        System.out.println("<<< [REINTENTO] Todos los canales configurados fallaron para este usuario.\n");
        return false;
    }
}
