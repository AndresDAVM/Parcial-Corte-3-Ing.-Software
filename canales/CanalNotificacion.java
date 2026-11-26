package com.notificaciones.canales;

import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.Usuario;

/**
 * INTERFAZ OBJETIVO (Target) del Patrón ADAPTER.
 * Define la operación estándar 'enviar' que el resto del sistema conoce y utiliza,
 * independientemente de cómo se conecte cada servicio externo.
 */
public interface CanalNotificacion {
    
    /**
     * Retorna el tipo de canal que representa esta implementación.
     */
    CanalTipo getTipo();

    /**
     * Envía la notificación al usuario de forma estandarizada.
     * @param usuario Destinatario
     * @param mensaje Objeto mensaje con asunto y plantilla
     * @return true si el envío fue exitoso, false si falló
     */
    boolean enviar(Usuario usuario, Mensaje mensaje);

    /**
     * Permite simular una caída intencional del canal para pruebas.
     */
    void setSimularFallo(boolean fallar);

    /**
     * Indica si el canal está configurado para fallar.
     */
    boolean isSimularFallo();
}
