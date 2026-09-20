package com.upiitrade.gui;

import com.upiitrade.mercado.Mercado;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

//Ventana de registro del cliente (ingresa su nombre y su dinero inicial para poder entrar al sistema de trading.) 

public class VentanaRegistro extends JFrame {

    private JTextField campNombre;
    private JTextField campMonto;
    private JButton btnIngresar;

    public VentanaRegistro() {
        setTitle("UPIITrade - Registro de Cliente");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // centrar en pantalla
        setResizable(false);

        initComponentes();
    }

    private void initComponentes() {
        JPanel panelPrincipal = new JPanel(new GridBagLayout());
        panelPrincipal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Titulo de la ventana
        JLabel lblTitulo = new JLabel("Bienvenido a UPIITrade", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Sans Seriff", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(lblTitulo, gbc);

        // Nombre
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Nombre del cliente:"), gbc);

        campNombre = new JTextField(15);
        gbc.gridx = 1;
        panelPrincipal.add(campNombre, gbc);

        // Monto inicial
        gbc.gridy = 2;
        gbc.gridx = 0;
        panelPrincipal.add(new JLabel("Monto inicial ($):"), gbc);

        campMonto = new JTextField(15);
        gbc.gridx = 1;
        panelPrincipal.add(campMonto, gbc);

        // Boton de ingresar
        btnIngresar = new JButton("Ingresar");
        btnIngresar.setBackground(new Color(34, 139, 34)); // verde
        btnIngresar.setForeground(Color.WHITE);
        btnIngresar.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panelPrincipal.add(btnIngresar, gbc);

        // Listener al boton
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarIngreso();
            }
        });

        add(panelPrincipal);
    }

    //Metodo que se ejecuta cuando el usuario hace clic en Ingresar, valida los campos y registra al cliente en el mercado.
    private void manejarIngreso() {
        String nombre = campNombre.getText().trim();
        String montoTexto = campMonto.getText().trim();

        // Validacion de que no esten vacios los campos
        if (nombre.isEmpty() || montoTexto.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Por favor llena todos los campos antes de continuar.",
                "Campos vacios",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validacion de que el monto sea un numero valido
        double monto;
        try {
            monto = Double.parseDouble(montoTexto);
            if (monto <= 0) {
                throw new NumberFormatException("El monto debe ser mayor a cero");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "El monto debe ser un numero valido mayor a cero.\nEjemplo: 5000.00",
                "Monto invalido",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Registrar al cliente en el mercado
        Mercado.getInstancia().registrarCliente(nombre, monto);

        // Abre la ventana principal y cierra el registro
        VentanaPrincipal ventanaPrincipal = new VentanaPrincipal();
        ventanaPrincipal.setVisible(true);
        this.dispose(); // cerramos la ventana de registro
    }

    // Prueba de esta ventana
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new VentanaRegistro().setVisible(true);
        });
    }
}
