package com.notificaciones.adapter;

import com.notificaciones.externo.ServicioEmailExterno;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;

public class EmailAdapter implements CanalNotificacion {
    private ServicioEmailExterno servicioEmail;

    public EmailAdapter() {
        this.servicioEmail = new ServicioEmailExterno();
    }

    public EmailAdapter(ServicioEmailExterno servicioEmail) {
        this.servicioEmail = servicioEmail;
    }

    @Override
    public TipoCanal getTipo() {
        return TipoCanal.EMAIL;
    }

    @Override
    public boolean enviar(Mensaje mensaje, Usuario usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            System.out.println("  [Aviso] Usuario sin correo.");
            return false;
        }

        return servicioEmail.enviarCorreoElectronico(
                usuario.getEmail(),
                mensaje.getAsunto(),
                mensaje.getCuerpo()
        );
    }

    @Override
    public void setSimularFallo(boolean simular) {
        servicioEmail.setSimularCaida(simular);
    }

    @Override
    public boolean isSimularFallo() {
        return servicioEmail.isSimularCaida();
    }
}
