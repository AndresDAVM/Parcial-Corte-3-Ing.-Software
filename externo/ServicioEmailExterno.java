package com.notificaciones.externo;

public class ServicioEmailExterno {
    private boolean simularCaida = false;

    public void setSimularCaida(boolean simularCaida) {
        this.simularCaida = simularCaida;
    }

    public boolean isSimularCaida() {
        return simularCaida;
    }

    public boolean enviarCorreoElectronico(String emailDestino, String encabezado, String cuerpo) {
        if (simularCaida) {
            System.out.println("  [Error Email] Servidor de correo caido.");
            return false;
        }
        System.out.println("  [Email] Para: " + emailDestino + " | Asunto: " + encabezado + " | Msg: " + cuerpo);
        return true;
    }
}
