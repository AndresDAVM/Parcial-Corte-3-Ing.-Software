package com.notificaciones.canales;

import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.Usuario;
import com.notificaciones.servicios_externos.ServicioWhatsAppExterno;

/**
 * ADAPTADOR (Adapter) para WhatsApp.
 * Adapta la clase externa 'ServicioWhatsAppExterno' a la interfaz 'CanalNotificacion'.
 */
public class WhatsAppAdapter implements CanalNotificacion {

    // Referencia al servicio adaptado (Adaptee)
    private ServicioWhatsAppExterno servicioWhatsApp;

    public WhatsAppAdapter(ServicioWhatsAppExterno servicioWhatsApp) {
        this.servicioWhatsApp = servicioWhatsApp;
    }

    @Override
    public CanalTipo getTipo() {
        return CanalTipo.WHATSAPP;
    }

    @Override
    public boolean enviar(Usuario usuario, Mensaje mensaje) {
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            System.out.println("[WhatsAppAdapter] Error: El usuario " + usuario.getNombre() + " no tiene teléfono para WhatsApp.");
            return false;
        }

        String cuerpo = mensaje.obtenerTextoPersonalizado(usuario);
        String numero = usuario.getTelefono().replace(" ", "").replace("-", "");

        return servicioWhatsApp.enviarMensajeWhatsApp(numero, mensaje.getAsunto(), cuerpo);
    }

    @Override
    public void setSimularFallo(boolean fallar) {
        servicioWhatsApp.setSimularCaida(fallar);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioWhatsApp.isSimularCaida();
    }
}
