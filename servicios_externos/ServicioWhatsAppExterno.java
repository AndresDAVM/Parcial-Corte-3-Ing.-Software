package com.notificaciones.servicios_externos;

/**
 * Simula la API de mensajería externa de WhatsApp Business (Meta Cloud API).
 * Requiere un token de autenticación y estructura tipo payload de mensajería.
 */
public class ServicioWhatsAppExterno {
    private String tokenAcceso;
    private boolean simularCaida = false;

    public ServicioWhatsAppExterno(String tokenAcceso) {
        this.tokenAcceso = tokenAcceso;
    }

    public void setSimularCaida(boolean simularCaida) {
        this.simularCaida = simularCaida;
    }

    public boolean isSimularCaida() {
        return simularCaida;
    }

    /**
     * Método propio de la API de WhatsApp.
     */
    public boolean enviarMensajeWhatsApp(String numeroWpp, String tituloPlantilla, String cuerpoMensaje) {
        if (simularCaida) {
            System.out.println("[API WhatsApp Meta] ERROR 401: Token " + tokenAcceso + " inaccesible o servicio desconectado.");
            return false;
        }

        System.out.println("[API WhatsApp Meta] Token validado.");
        System.out.println("                    Tel: " + numeroWpp);
        System.out.println("                    Plantilla: [" + tituloPlantilla + "]");
        System.out.println("                    Mensaje: " + cuerpoMensaje);
        return true;
    }
}
