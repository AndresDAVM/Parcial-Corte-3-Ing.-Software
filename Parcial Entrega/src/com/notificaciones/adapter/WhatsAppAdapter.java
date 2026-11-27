package com.notificaciones.adapter;

import com.notificaciones.externo.ServicioWhatsAppExterno;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;

public class WhatsAppAdapter implements CanalNotificacion {
    private ServicioWhatsAppExterno servicioWA;

    public WhatsAppAdapter() {
        this.servicioWA = new ServicioWhatsAppExterno();
    }

    public WhatsAppAdapter(ServicioWhatsAppExterno servicioWA) {
        this.servicioWA = servicioWA;
    }

    @Override
    public TipoCanal getTipo() {
        return TipoCanal.WHATSAPP;
    }

    @Override
    public boolean enviar(Mensaje mensaje, Usuario usuario) {
        if (usuario.getTelefono() == null || usuario.getTelefono().isEmpty()) {
            System.out.println("  [Aviso] Usuario sin telefono.");
            return false;
        }

        String jsonPayload = "{\"asunto\":\"" + limpiarTexto(mensaje.getAsunto()) 
                + "\", \"mensaje\":\"" + limpiarTexto(mensaje.getCuerpo()) + "\"}";

        return servicioWA.enviarMensajeApi("57", usuario.getTelefono(), jsonPayload);
    }

    private String limpiarTexto(String s) {
        if (s == null) return "";
        return s.replace("\"", "\\\"").replace("\n", " ");
    }

    @Override
    public void setSimularFallo(boolean simular) {
        servicioWA.setSimularErrorApi(simular);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioWA.isSimularErrorApi();
    }
}
