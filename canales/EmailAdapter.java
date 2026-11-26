package com.notificaciones.canales;

import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.Usuario;
import com.notificaciones.servicios_externos.ServicioEmailExterno;

/**
 * ADAPTADOR (Adapter) para el canal de Correo Electrónico.
 * Adapta la clase externa 'ServicioEmailExterno' a la interfaz 'CanalNotificacion'.
 */
public class EmailAdapter implements CanalNotificacion {
    
    // Referencia al servicio que se adapta (Adaptee)
    private ServicioEmailExterno servicioEmail;

    public EmailAdapter(ServicioEmailExterno servicioEmail) {
        this.servicioEmail = servicioEmail;
    }

    @Override
    public CanalTipo getTipo() {
        return CanalTipo.EMAIL;
    }

    @Override
    public boolean enviar(Usuario usuario, Mensaje mensaje) {
        // Validación básica
        if (usuario.getCorreo() == null || usuario.getCorreo().trim().isEmpty()) {
            System.out.println("[EmailAdapter] Error: El usuario " + usuario.getNombre() + " no tiene correo configurado.");
            return false;
        }

        // Obtener texto personalizado
        String cuerpo = mensaje.obtenerTextoPersonalizado(usuario);

        // Llamar al método específico del servicio externo
        return servicioEmail.enviarCorreoSmtp(usuario.getCorreo(), mensaje.getAsunto(), cuerpo);
    }

    @Override
    public void setSimularFallo(boolean fallar) {
        servicioEmail.setSimularCaida(fallar);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioEmail.isSimularCaida();
    }
}
