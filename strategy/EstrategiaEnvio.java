package com.notificaciones.strategy;

import com.notificaciones.adapter.CanalNotificacion;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.Usuario;
import java.util.List;

public interface EstrategiaEnvio {
    String getNombre();
    ResultadoDespacho despachar(Mensaje mensaje, Usuario usuario, List<CanalNotificacion> canales);
}
