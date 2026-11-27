package com.notificaciones.adapter;

import com.notificaciones.model.Mensaje;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;

public interface CanalNotificacion {
    TipoCanal getTipo();
    boolean enviar(Mensaje mensaje, Usuario usuario);
    void setSimularFallo(boolean simular);
    boolean isSimularFallo();
}
