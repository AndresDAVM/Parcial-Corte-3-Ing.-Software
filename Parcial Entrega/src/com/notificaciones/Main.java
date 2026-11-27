package com.notificaciones;

import com.notificaciones.facade.NotificadorFacade;
import com.notificaciones.ui.MenuConsola;
import com.notificaciones.ui.VentanaPrincipal;

import javax.swing.SwingUtilities;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        NotificadorFacade facade = new NotificadorFacade();
        facade.cargarDatosDePrueba();

        if (args.length > 0) {
            if ("--gui".equalsIgnoreCase(args[0])) {
                lanzarGUI(facade);
                return;
            } else if ("--consola".equalsIgnoreCase(args[0])) {
                lanzarConsola(facade);
                return;
            }
        }

        System.out.println("SISTEMA DE NOTIFICACIONES MULTICANAL");
        System.out.println("1. Modo Consola");
        System.out.println("2. Modo Interfaz Grafica (Swing)");
        System.out.print("Elige opcion (1 o 2) [Enter = 1]: ");

        Scanner sc = new Scanner(System.in);
        String op = sc.nextLine().trim();

        if ("2".equals(op)) {
            lanzarGUI(facade);
        } else {
            lanzarConsola(facade);
        }
    }

    private static void lanzarConsola(NotificadorFacade facade) {
        MenuConsola menu = new MenuConsola(facade);
        menu.iniciar();
    }

    private static void lanzarGUI(NotificadorFacade facade) {
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal v = new VentanaPrincipal(facade);
            v.setVisible(true);
        });
    }
}
