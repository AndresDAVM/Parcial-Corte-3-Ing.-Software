package com.notificaciones.servicios_externos;

/**
 * Simula una pasarela o gateway de SMS de terceros (ej. Twilio o Sinch).
 * Exige prefijo de país y valida longitud de caracteres.
 */
public class ServicioSmsExterno {
    private String apiKey;
    private boolean simularCaida = false;

    public ServicioSmsExterno(String apiKey) {
        this.apiKey = apiKey;
    }

    public void setSimularCaida(boolean simularCaida) {
        this.simularCaida = simularCaida;
    }

    public boolean isSimularCaida() {
        return simularCaida;
    }

    /**
     * Método propio de la API de SMS.
     */
    public boolean transmitirSmsGateway(String numeroConPrefijo, String textoCorto) {
        if (simularCaida) {
            System.out.println("[API SMS Gateway] ERROR: Conexión rechazada con apiKey " + apiKey + " (Simulación de caída).");
            return false;
        }

        // Simulación de recorte si excede 160 caracteres típicos de SMS
        String textoAjustado = textoCorto.length() > 160 ? textoCorto.substring(0, 157) + "..." : textoCorto;

        System.out.println("[API SMS Gateway] Autenticado con API Key [" + apiKey + "]");
        System.out.println("                  Destino: " + numeroConPrefijo);
        System.out.println("                  SMS: " + textoAjustado);
        return true;
    }
}
