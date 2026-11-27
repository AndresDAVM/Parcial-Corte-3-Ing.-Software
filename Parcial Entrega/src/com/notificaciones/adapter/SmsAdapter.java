package com.notificaciones.adapter;

import com.notificaciones.externo.ServicioSMSExterno;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;

public class SmsAdapter implements CanalNotificacion {
    private ServicioSMSExterno servicioSMS;

    public SmsAdapter() {
        this.servicioSMS = new ServicioSMSExterno();
    }

    public SmsAdapter(ServicioSMSExterno servicioSMS) {
        this.servicioSMS = servicioSMS;
    }

    @Override
    public TipoCanal getTipo() {
        return TipoCanal.SMS;
    }

    @Override
    public boolean enviar(Mensaje mensaje, Usuario usuario) {
        if (usuario.getTelefono() == null || usuario.getTelefono().isEmpty()) {
            System.out.println("  [Aviso] Usuario sin telefono.");
            return false;
        }

        String texto = "[" + mensaje.getAsunto() + "] " + mensaje.getCuerpo();
        return servicioSMS.transmitirMensajeCorto(usuario.getTelefono(), texto);
    }

    @Override
    public void setSimularFallo(boolean simular) {
        servicioSMS.setSimularFalloRed(simular);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioSMS.isSimularFalloRed();
    }
}
