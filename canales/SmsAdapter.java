package com.notificaciones.canales;

import com.notificaciones.modelo.CanalTipo;
import com.notificaciones.modelo.Mensaje;
import com.notificaciones.modelo.Usuario;
import com.notificaciones.servicios_externos.ServicioSmsExterno;

/**
 * ADAPTADOR (Adapter) para el canal de SMS.
 * Adapta la clase externa 'ServicioSmsExterno' a la interfaz 'CanalNotificacion'.
 */
public class SmsAdapter implements CanalNotificacion {

    // Referencia al servicio adaptado (Adaptee)
    private ServicioSmsExterno servicioSms;

    public SmsAdapter(ServicioSmsExterno servicioSms) {
        this.servicioSms = servicioSms;
    }

    @Override
    public CanalTipo getTipo() {
        return CanalTipo.SMS;
    }

    @Override
    public boolean enviar(Usuario usuario, Mensaje mensaje) {
        if (usuario.getTelefono() == null || usuario.getTelefono().trim().isEmpty()) {
            System.out.println("[SmsAdapter] Error: El usuario " + usuario.getNombre() + " no tiene teléfono registrado.");
            return false;
        }

        // Se arma el texto con prefijo para SMS
        String texto = "[" + mensaje.getAsunto() + "] " + mensaje.obtenerTextoPersonalizado(usuario);

        // Se asegura el formato internacional requerido por el gateway (+57 si no lo tiene)
        String numero = usuario.getTelefono().trim();
        if (!numero.startsWith("+")) {
            numero = "+57 " + numero;
        }

        return servicioSms.transmitirSmsGateway(numero, texto);
    }

    @Override
    public void setSimularFallo(boolean fallar) {
        servicioSms.setSimularCaida(fallar);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioSms.isSimularCaida();
    }
}
