package com.notificaciones.test;

import com.notificaciones.adapter.EmailAdapter;
import com.notificaciones.adapter.SmsAdapter;
import com.notificaciones.adapter.WhatsAppAdapter;
import com.notificaciones.facade.NotificadorFacade;
import com.notificaciones.model.EstadoEnvio;
import com.notificaciones.model.Mensaje;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import com.notificaciones.strategy.EstrategiaConReintento;
import com.notificaciones.strategy.EstrategiaNormal;
import com.notificaciones.strategy.EstrategiaUrgente;
import com.notificaciones.strategy.ResultadoDespacho;

import java.util.List;

public class PruebasPatronesTest {

    private static int exitosas = 0;
    private static int totales = 0;

    public static void main(String[] args) {
        System.out.println("=== Pruebas Unitarias ===");

        correr("Adapter - Envio por cada canal", () -> testAdapter());
        correr("Strategy - Regla Urgente", () -> testUrgente());
        correr("Strategy - Regla Normal", () -> testNormal());
        correr("Strategy - Regla con Reintento", () -> testReintento());
        correr("Facade - Plantillas e Historial", () -> testFacade());

        System.out.println("\nResultado: " + exitosas + "/" + totales + " pruebas pasadas.");
        if (exitosas == totales) {
            System.out.println("TODO OK");
        }
    }

    private static void testAdapter() {
        Usuario u = new Usuario("T1", "Ana", "ana@test.com", "3001112233", TipoCanal.EMAIL);
        Mensaje m = new Mensaje("Test", "Hola");

        EmailAdapter email = new EmailAdapter();
        SmsAdapter sms = new SmsAdapter();
        WhatsAppAdapter wa = new WhatsAppAdapter();

        afirmar(email.enviar(m, u), "Fallo email");
        afirmar(sms.enviar(m, u), "Fallo sms");
        afirmar(wa.enviar(m, u), "Fallo whatsapp");

        email.setSimularFallo(true);
        afirmar(!email.enviar(m, u), "Debia fallar email");
        email.setSimularFallo(false);
    }

    private static void testUrgente() {
        Usuario u = new Usuario("T2", "Beto", "beto@test.com", "3004445566", TipoCanal.EMAIL);
        u.habilitarCanal(TipoCanal.SMS);
        u.habilitarCanal(TipoCanal.WHATSAPP);

        Mensaje m = new Mensaje("Urgente", "Alerta");
        NotificadorFacade facade = new NotificadorFacade();

        EstrategiaUrgente urgente = new EstrategiaUrgente();
        ResultadoDespacho res = urgente.despachar(m, u, facade.getCanales());

        afirmar(res.isExitoso(), "Fallo urgente");
        afirmar(res.getRegistros().size() == 3, "Debio enviar por 3 canales");
    }

    private static void testNormal() {
        Usuario u = new Usuario("T3", "Clara", "clara@test.com", "3007778899", TipoCanal.WHATSAPP);
        u.habilitarCanal(TipoCanal.EMAIL);

        Mensaje m = new Mensaje("Info", "Hola");
        NotificadorFacade facade = new NotificadorFacade();

        EstrategiaNormal normal = new EstrategiaNormal();
        ResultadoDespacho res = normal.despachar(m, u, facade.getCanales());

        afirmar(res.isExitoso(), "Fallo normal");
        afirmar(res.getRegistros().size() == 1, "Solo debia ser 1 canal");
        afirmar(res.getRegistros().get(0).getCanal() == TipoCanal.WHATSAPP, "Debia ser WhatsApp");
    }

    private static void testReintento() {
        Usuario u = new Usuario("T4", "Daniel", "daniel@test.com", "3000001122", TipoCanal.EMAIL);
        u.habilitarCanal(TipoCanal.SMS);

        Mensaje m = new Mensaje("Aviso", "Reintento");
        NotificadorFacade facade = new NotificadorFacade();

        facade.configurarFalloCanal(TipoCanal.EMAIL, true);

        EstrategiaConReintento reintento = new EstrategiaConReintento();
        ResultadoDespacho res = reintento.despachar(m, u, facade.getCanales());

        facade.configurarFalloCanal(TipoCanal.EMAIL, false);

        afirmar(res.isExitoso(), "Debio tener exito por respaldo");
        afirmar(res.getRegistros().size() == 2, "Debieron ser 2 intentos");
        afirmar(res.getRegistros().get(0).getEstado() == EstadoEnvio.FALLIDO, "El primero debia fallar");
        afirmar(res.getRegistros().get(1).getEstado() == EstadoEnvio.EXITOSO, "El segundo debia pasar");
    }

    private static void testFacade() {
        NotificadorFacade facade = new NotificadorFacade();
        Usuario u = new Usuario("T5", "Elena", "elena@test.com", "3119998877", TipoCanal.SMS);
        facade.registrarUsuario(u);

        ResultadoDespacho res = facade.enviarNotificacion("T5", "Hola", "Bienvenido {nombre}", "NORMAL");
        afirmar(res.isExitoso(), "Fallo envio facade");

        List<RegistroNotificacion> hist = facade.getHistorial();
        afirmar(!hist.isEmpty(), "Historial no debe estar vacio");
    }

    private static void correr(String nombre, Runnable prueba) {
        totales++;
        try {
            prueba.run();
            exitosas++;
            System.out.println("  [OK] " + nombre);
        } catch (Throwable t) {
            System.out.println("  [FALLO] " + nombre + ": " + t.getMessage());
        }
    }

    private static void afirmar(boolean condicion, String error) {
        if (!condicion) {
            throw new AssertionError(error);
        }
    }
}
