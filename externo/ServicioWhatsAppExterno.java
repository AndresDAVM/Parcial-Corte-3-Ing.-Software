package com.notificaciones.externo;

public class ServicioWhatsAppExterno {
    private boolean simularErrorApi = false;

    public void setSimularErrorApi(boolean simularErrorApi) {
        this.simularErrorApi = simularErrorApi;
    }

    public boolean isSimularErrorApi() {
        return simularErrorApi;
    }

    public boolean enviarMensajeApi(String codigoPais, String numeroMovil, String jsonPayload) {
        if (simularErrorApi) {
            System.out.println("  [Error WhatsApp] API no responde (503).");
            return false;
        }

        System.out.println("  [WhatsApp] Para: +" + codigoPais + " " + numeroMovil + " | Payload: " + jsonPayload);
        return true;
    }
}
