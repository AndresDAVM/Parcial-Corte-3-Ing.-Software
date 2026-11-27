package com.notificaciones.ui;

import com.notificaciones.facade.NotificadorFacade;
import com.notificaciones.model.RegistroNotificacion;
import com.notificaciones.model.TipoCanal;
import com.notificaciones.model.Usuario;
import com.notificaciones.strategy.ResultadoDespacho;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class VentanaPrincipal extends JFrame {
    private NotificadorFacade facade;

    private JComboBox<UsuarioItem> comboUsuarios;
    private JComboBox<String> comboReglas;
    private JTextField txtAsunto;
    private JTextArea txtCuerpo;
    private JCheckBox chkFalloEmail;
    private JCheckBox chkFalloSMS;
    private JCheckBox chkFalloWhatsApp;
    private DefaultTableModel modeloTabla;
    private JTable tablaHistorial;

    public VentanaPrincipal(NotificadorFacade facade) {
        super("Sistema de Notificaciones Multicanal");
        this.facade = facade;

        initUI();
        cargarUsuarios();
        actualizarTablaHistorial();
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 640);
        setLocationRelativeTo(null);

        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel panelHeader = new JPanel(new BorderLayout());
        panelHeader.setBackground(new Color(40, 70, 110));
        JLabel lblTitulo = new JLabel("  Sistema de Notificaciones Multicanal", JLabel.LEFT);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setPreferredSize(new Dimension(400, 40));
        panelHeader.add(lblTitulo, BorderLayout.WEST);
        panelPrincipal.add(panelHeader, BorderLayout.NORTH);

        JPanel panelCentro = new JPanel(new BorderLayout(10, 10));
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Envio de Mensaje"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        panelForm.add(new JLabel("Destinatario:"), gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.8;
        comboUsuarios = new JComboBox<>();
        panelForm.add(comboUsuarios, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        panelForm.add(new JLabel("Regla de Envio:"), gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.8;
        comboReglas = new JComboBox<>(new String[]{
                "URGENTE (Todos los canales)",
                "NORMAL (Canal preferido)",
                "REINTENTO (Con respaldo)"
        });
        panelForm.add(comboReglas, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        panelForm.add(new JLabel("Asunto:"), gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.8;
        txtAsunto = new JTextField("Aviso de saldo");
        panelForm.add(txtAsunto, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.2;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        panelForm.add(new JLabel("Mensaje / Plantilla:"), gbc);
        gbc.gridx = 1; gbc.gridy = 3; gbc.weightx = 0.8;
        txtCuerpo = new JTextArea(3, 25);
        txtCuerpo.setText("Hola {nombre}, te recordamos tu pago de la fecha {fecha}.");
        txtCuerpo.setLineWrap(true);
        txtCuerpo.setWrapStyleWord(true);
        panelForm.add(new JScrollPane(txtCuerpo), gbc);

        gbc.gridx = 0; gbc.gridy = 4; gbc.weightx = 0.2;
        panelForm.add(new JLabel("Simular Caida:"), gbc);
        gbc.gridx = 1; gbc.gridy = 4; gbc.weightx = 0.8;
        JPanel panelChecks = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        chkFalloEmail = new JCheckBox("Fallo Email");
        chkFalloSMS = new JCheckBox("Fallo SMS");
        chkFalloWhatsApp = new JCheckBox("Fallo WhatsApp");

        chkFalloEmail.addActionListener(e -> facade.configurarFalloCanal(TipoCanal.EMAIL, chkFalloEmail.isSelected()));
        chkFalloSMS.addActionListener(e -> facade.configurarFalloCanal(TipoCanal.SMS, chkFalloSMS.isSelected()));
        chkFalloWhatsApp.addActionListener(e -> facade.configurarFalloCanal(TipoCanal.WHATSAPP, chkFalloWhatsApp.isSelected()));

        panelChecks.add(chkFalloEmail);
        panelChecks.add(chkFalloSMS);
        panelChecks.add(chkFalloWhatsApp);
        panelForm.add(panelChecks, gbc);

        gbc.gridx = 1; gbc.gridy = 5; gbc.weightx = 0.8;
        JButton btnEnviar = new JButton("Enviar Notificacion");
        btnEnviar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEnviar.setBackground(new Color(40, 70, 110));
        btnEnviar.setForeground(Color.WHITE);
        btnEnviar.addActionListener(e -> enviar());
        panelForm.add(btnEnviar, gbc);

        panelCentro.add(panelForm, BorderLayout.NORTH);

        JPanel panelTabla = new JPanel(new BorderLayout());
        panelTabla.setBorder(BorderFactory.createTitledBorder("Historial de Envios"));

        String[] cols = {"ID", "Fecha/Hora", "Destinatario", "Canal", "Estado", "Detalle"};
        modeloTabla = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(40);
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(130);
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(110);
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(80);
        tablaHistorial.getColumnModel().getColumn(4).setPreferredWidth(70);
        tablaHistorial.getColumnModel().getColumn(5).setPreferredWidth(300);

        panelTabla.add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);
        panelCentro.add(panelTabla, BorderLayout.CENTER);

        panelPrincipal.add(panelCentro, BorderLayout.CENTER);
        setContentPane(panelPrincipal);
    }

    private void cargarUsuarios() {
        comboUsuarios.removeAllItems();
        for (Usuario u : facade.getUsuarios()) {
            comboUsuarios.addItem(new UsuarioItem(u));
        }
    }

    private void enviar() {
        UsuarioItem item = (UsuarioItem) comboUsuarios.getSelectedItem();
        if (item == null) return;

        String asunto = txtAsunto.getText().trim();
        String cuerpo = txtCuerpo.getText().trim();
        if (asunto.isEmpty() || cuerpo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Completa los campos.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int index = comboReglas.getSelectedIndex();
        String regla = "NORMAL";
        if (index == 0) regla = "URGENTE";
        else if (index == 2) regla = "REINTENTO";

        ResultadoDespacho res = facade.enviarNotificacion(item.usuario.getId(), asunto, cuerpo, regla);
        actualizarTablaHistorial();

        JOptionPane.showMessageDialog(this, res.getMensajeResumen(), 
                res.isExitoso() ? "Exito" : "Fallo", 
                res.isExitoso() ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
    }

    private void actualizarTablaHistorial() {
        modeloTabla.setRowCount(0);
        for (RegistroNotificacion r : facade.getHistorial()) {
            modeloTabla.addRow(new Object[]{
                    r.getId(),
                    r.getFechaHoraFormateada(),
                    r.getNombreDestinatario(),
                    r.getCanal(),
                    r.getEstado(),
                    r.getDetalle()
            });
        }
    }

    private static class UsuarioItem {
        final Usuario usuario;

        UsuarioItem(Usuario usuario) {
            this.usuario = usuario;
        }

        @Override
        public String toString() {
            return usuario.getNombre() + " (" + usuario.getCanalPreferido() + ")";
        }
    }
}
