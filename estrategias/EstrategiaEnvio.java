package com.notificaciones.estrategias;

import java.util.Map;
import com.notificaciones.canales.CanalNotificacion;
import com.notificaciones.historial.GestorHistorial;
import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.TipoEnvio;
import com.notificaciones.modelo.Usuario;

/**
 * INTERFAZ ESTRATEGIA (Strategy).
 * Declara el método común para ejecutar una regla de envío de notificaciones.
 */
public interface EstrategiaEnvio {

    /**
     * Identificador del tipo de envío.
     */
    TipoEnvio getTipo();

    /**
     * Ejecuta el despacho de la notificación según la política implementada.
     * @param usuario Destinatario con sus preferencias
     * @param mensaje Mensaje a enviar
     * @param canales Mapa con los canales registrados en el sistema
     * @param historial Gestor para auditar cada intento
     * @return true si la estrategia considera el envío exitoso, false en caso contrario
     */
    boolean despachar(Usuario usuario, Mensaje mensaje, 
                      Map<CanalTipo, CanalNotificacion> canales, 
                      GestorHistorial historial);
}
