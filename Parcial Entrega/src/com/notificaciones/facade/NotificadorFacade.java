package com.notificaciones.facade;

import com.notificaciones.adapter.CanalNotificacion;
import com.notificaciones.adapter.EmailAdapter;
import com.notificaciones.adapter.SmsAdapter;
import com.notificaciones.adapter.WhatsAppAdapter;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import com.notificaciones.strategy.EstrategiaConReintento;
import com.notificaciones.strategy.EstrategiaEnvio;
import com.notificaciones.strategy.EstrategiaNormal;
import com.notificaciones.strategy.EstrategiaUrgente;
import com.notificaciones.strategy.ResultadoDespacho;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificadorFacade {
    private Map<String, Usuario> usuarios;
    private List<CanalNotificacion> canales;
    private Map<String, EstrategiaEnvio> estrategias;
    private List<RegistroNotificacion> historial;

    public NotificadorFacade() {
        this.usuarios = new HashMap<>();
        this.canales = new ArrayList<>();
        this.estrategias = new HashMap<>();
        this.historial = new ArrayList<>();

        iniciarCanalesYEstrategias();
    }

    private void iniciarCanalesYEstrategias() {
        canales.add(new EmailAdapter());
        canales.add(new SmsAdapter());
        canales.add(new WhatsAppAdapter());

        estrategias.put("URGENTE", new EstrategiaUrgente());
        estrategias.put("NORMAL", new EstrategiaNormal());
        estrategias.put("REINTENTO", new EstrategiaConReintento());
    }

    public void cargarDatosDePrueba() {
        Usuario u1 = new Usuario("USR-01", "Carlos Gomez", "carlos@correo.com", "3104567890", TipoCanal.EMAIL);
        u1.habilitarCanal(TipoCanal.SMS);
        u1.habilitarCanal(TipoCanal.WHATSAPP);

        Usuario u2 = new Usuario("USR-02", "Laura Restrepo", "laura@correo.com", "3158901234", TipoCanal.WHATSAPP);
        u2.habilitarCanal(TipoCanal.SMS);

        Usuario u3 = new Usuario("USR-03", "David Silva", "david@correo.com", "3007654321", TipoCanal.SMS);
        u3.habilitarCanal(TipoCanal.EMAIL);

        registrarUsuario(u1);
        registrarUsuario(u2);
        registrarUsuario(u3);
    }

    public void registrarUsuario(Usuario usuario) {
        if (usuario != null && usuario.getId() != null) {
            usuarios.put(usuario.getId(), usuario);
        }
    }

    public Usuario buscarUsuario(String id) {
        return usuarios.get(id);
    }

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios.values());
    }

    public List<CanalNotificacion> getCanales() {
        return Collections.unmodifiableList(canales);
    }

    public CanalNotificacion buscarCanal(TipoCanal tipo) {
        for (CanalNotificacion c : canales) {
            if (c.getTipo() == tipo) return c;
        }
        return null;
    }

    public void configurarFalloCanal(TipoCanal tipo, boolean simularFallo) {
        CanalNotificacion canal = buscarCanal(tipo);
        if (canal != null) {
            canal.setSimularFallo(simularFallo);
            System.out.println("[Config] Simular fallo en " + tipo + ": " + simularFallo);
        }
    }

    public boolean isCanalConFallo(TipoCanal tipo) {
        CanalNotificacion canal = buscarCanal(tipo);
        return canal != null && canal.isSimularFallo();
    }

    public ResultadoDespacho enviarNotificacion(String idUsuario, String asunto, String plantillaCuerpo, String tipoRegla) {
        Usuario destinatario = buscarUsuario(idUsuario);
        if (destinatario == null) {
            return new ResultadoDespacho(false, "Usuario no encontrado: " + idUsuario);
        }

        EstrategiaEnvio estrategia = estrategias.get(tipoRegla.toUpperCase());
        if (estrategia == null) {
            return new ResultadoDespacho(false, "Regla invalida: " + tipoRegla);
        }

        Mensaje mensajeBase = new Mensaje(asunto, plantillaCuerpo);
        Mensaje mensajePersonalizado = mensajeBase.personalizarPara(destinatario);

        System.out.println("\n---------------------------------------------------");
        System.out.println("Despachando a " + destinatario.getNombre() + " | Regla: " + tipoRegla);
        System.out.println("Mensaje: " + mensajePersonalizado.getCuerpo());
        System.out.println("---------------------------------------------------");

        ResultadoDespacho resultado = estrategia.despachar(mensajePersonalizado, destinatario, canales);

        for (RegistroNotificacion r : resultado.getRegistros()) {
            historial.add(r);
        }

        return resultado;
    }

    public List<RegistroNotificacion> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}
