package com.notificaciones.externo;

public class ServicioSMSExterno {
    private boolean simularFalloRed = false;

    public void setSimularFalloRed(boolean simularFalloRed) {
        this.simularFalloRed = simularFalloRed;
    }

    public boolean isSimularFalloRed() {
        return simularFalloRed;
    }

    public boolean transmitirMensajeCorto(String numeroTelefonico, String contenidoPlano) {
        if (simularFalloRed) {
            System.out.println("  [Error SMS] Fallo en la red telefonica.");
            return false;
        }

        String texto = contenidoPlano;
        if (texto.length() > 160) {
            texto = texto.substring(0, 157) + "...";
        }

        System.out.println("  [SMS] Para: " + numeroTelefonico + " | " + texto);
        return true;
    }
}
