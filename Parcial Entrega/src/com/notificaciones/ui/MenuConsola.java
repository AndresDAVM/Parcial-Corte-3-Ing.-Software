package com.notificaciones.ui;

import com.notificaciones.facade.NotificadorFacade;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import com.notificaciones.strategy.ResultadoDespacho;

import java.util.List;
import java.util.Scanner;

public class MenuConsola {
    private NotificadorFacade facade;
    private Scanner scanner;

    public MenuConsola(NotificadorFacade facade) {
        this.facade = facade;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int op = -1;
        while (op != 0) {
            mostrarMenu();
            try {
                System.out.print("Opcion: ");
                op = Integer.parseInt(scanner.nextLine().trim());
                procesarOpcion(op);
            } catch (NumberFormatException e) {
                System.out.println("Ingresa un numero valido.");
            }
            if (op != 0) {
                System.out.println("\nPresiona ENTER para continuar...");
                scanner.nextLine();
            }
        }
        System.out.println("Fin del programa.");
    }

    private void mostrarMenu() {
        System.out.println("\n========================================");
        System.out.println("     SISTEMA DE NOTIFICACIONES JAVA     ");
        System.out.println("========================================");
        System.out.println("1. Listar usuarios");
        System.out.println("2. Registrar nuevo usuario");
        System.out.println("3. Enviar notificacion");
        System.out.println("4. Simular caida de un canal");
        System.out.println("5. Ver historial de envios");
        System.out.println("0. Salir");
        System.out.println("========================================");
    }

    private void procesarOpcion(int op) {
        switch (op) {
            case 1:
                listarUsuarios();
                break;
            case 2:
                registrarNuevoUsuario();
                break;
            case 3:
                enviarNotificacion();
                break;
            case 4:
                configurarFalloCanal();
                break;
            case 5:
                verHistorial();
                break;
            case 0:
                break;
            default:
                System.out.println("Opcion no valida.");
        }
    }

    private void listarUsuarios() {
        System.out.println("\n--- Usuarios Registrados ---");
        List<Usuario> lista = facade.getUsuarios();
        if (lista.isEmpty()) {
            System.out.println("No hay usuarios.");
            return;
        }
        for (Usuario u : lista) {
            System.out.println(u.getId() + " - " + u.getNombre() + " (" + u.getEmail() + ", " + u.getTelefono() + ")");
            System.out.println("    Preferido: " + u.getCanalPreferido() + " | Habilitados: " + u.getCanalesHabilitados());
        }
    }

    private void registrarNuevoUsuario() {
        System.out.println("\n--- Nuevo Usuario ---");
        System.out.print("ID: ");
        String id = scanner.nextLine().trim();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Telefono: ");
        String tel = scanner.nextLine().trim();

        System.out.println("Canal preferido (1=EMAIL, 2=SMS, 3=WHATSAPP): ");
        TipoCanal pref = TipoCanal.EMAIL;
        String canalOp = scanner.nextLine().trim();
        if ("2".equals(canalOp)) pref = TipoCanal.SMS;
        else if ("3".equals(canalOp)) pref = TipoCanal.WHATSAPP;

        Usuario nuevo = new Usuario(id, nombre, email, tel, pref);

        System.out.print("Habilitar tambien SMS? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) nuevo.habilitarCanal(TipoCanal.SMS);

        System.out.print("Habilitar tambien WhatsApp? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) nuevo.habilitarCanal(TipoCanal.WHATSAPP);

        System.out.print("Habilitar tambien Email? (s/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("s")) nuevo.habilitarCanal(TipoCanal.EMAIL);

        facade.registrarUsuario(nuevo);
        System.out.println("Usuario guardado.");
    }

    private void enviarNotificacion() {
        System.out.println("\n--- Enviar Mensaje ---");
        listarUsuarios();
        System.out.print("\nID de destinatario: ");
        String id = scanner.nextLine().trim();

        Usuario u = facade.buscarUsuario(id);
        if (u == null) {
            System.out.println("No se encontro el usuario.");
            return;
        }

        System.out.print("Asunto: ");
        String asunto = scanner.nextLine().trim();

        System.out.print("Mensaje (usa {nombre}, {fecha}): ");
        String cuerpo = scanner.nextLine().trim();

        System.out.println("Regla: 1. URGENTE | 2. NORMAL | 3. REINTENTO");
        System.out.print("Opcion (1-3): ");
        String rOp = scanner.nextLine().trim();

        String regla = "NORMAL";
        if ("1".equals(rOp)) regla = "URGENTE";
        else if ("3".equals(rOp)) regla = "REINTENTO";

        ResultadoDespacho res = facade.enviarNotificacion(id, asunto, cuerpo, regla);

        System.out.println("\n>>> Resultado: " + (res.isExitoso() ? "EXITOSO" : "FALLIDO"));
        System.out.println(">>> " + res.getMensajeResumen());
    }

    private void configurarFalloCanal() {
        System.out.println("\n--- Estado de Canales ---");
        for (TipoCanal t : TipoCanal.values()) {
            System.out.println("  " + t + ": " + (facade.isCanalConFallo(t) ? "CAIDO" : "OK"));
        }

        System.out.println("Cambiar estado: 1. EMAIL | 2. SMS | 3. WHATSAPP");
        System.out.print("Opcion: ");
        String op = scanner.nextLine().trim();

        TipoCanal sel = null;
        if ("1".equals(op)) sel = TipoCanal.EMAIL;
        else if ("2".equals(op)) sel = TipoCanal.SMS;
        else if ("3".equals(op)) sel = TipoCanal.WHATSAPP;

        if (sel != null) {
            boolean nuevo = !facade.isCanalConFallo(sel);
            facade.configurarFalloCanal(sel, nuevo);
            System.out.println("Canal " + sel + " ahora esta: " + (nuevo ? "CAIDO" : "OK"));
        }
    }

    private void verHistorial() {
        System.out.println("\n--- Historial ---");
        List<RegistroNotificacion> historial = facade.getHistorial();
        if (historial.isEmpty()) {
            System.out.println("Historial vacio.");
            return;
        }
        for (RegistroNotificacion r : historial) {
            System.out.println(r.toString());
        }
    }
}
