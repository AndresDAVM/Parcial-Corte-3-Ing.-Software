package com.notificaciones.servicios_externos;

/**
 * Simula una librería o API externa de un proveedor de Correo Electrónico (ej. SendGrid o SMTP).
 * Tiene su propia firma de métodos y parámetros incompatibles con el resto de la app.
 */
public class ServicioEmailExterno {
    private String servidorSmtp;
    private int puerto;
    private boolean simularCaida = false;

    public ServicioEmailExterno(String servidorSmtp, int puerto) {
        this.servidorSmtp = servidorSmtp;
        this.puerto = puerto;
    }

    public void setSimularCaida(boolean simularCaida) {
        this.simularCaida = simularCaida;
    }

    public boolean isSimularCaida() {
        return simularCaida;
    }

    /**
     * Método propio de la librería externa para despachar correos.
     */
    public boolean enviarCorreoSmtp(String destinatarioEmail, String asunto, String cuerpoTexto) {
        if (simularCaida) {
            System.out.println("[API Email Externa] ERROR 503: Servidor SMTP " + servidorSmtp + ":" + puerto + " fuera de servicio.");
            return false;
        }
        System.out.println("[API Email Externa] Conectado a " + servidorSmtp + ":" + puerto);
        System.out.println("                   Para: " + destinatarioEmail);
        System.out.println("                   Asunto: " + asunto);
        System.out.println("                   Cuerpo: " + cuerpoTexto);
        return true;
    }
}
