/**
 * Ventana principal del sistema UPIITrade.
 * Muestra la tabla de activos disponibles, el saldo del usuario y permite comprar o vender activos.
 * Versión 2
 * Enrique Meneses Reyes - Disenador de GUI
 */

package com.upiitrade.gui;

import com.upiitrade.mercado.Mercado;
import com.upiitrade.mercado.Activo;

import com.upiitrade.control.SimboloNoValidoException;
import com.upiitrade.control.FondosInsuficientesException;
import com.upiitrade.control.InventarioInsuficienteException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;


public class VentanaPrincipal extends JFrame {

    // Etiquetas de informacion del usuario
    private JLabel lblNombreValor;
    private JLabel lblSaldoValor;

    // Tabla de activos
    private JTable tablaActivos;
    private DefaultTableModel modeloTabla;

    // Campo para la cantidad a comprar/vender
    private JTextField campCantidad;

    // Botones de accion
    private JButton btnComprar;
    private JButton btnVender;

    public VentanaPrincipal() {
        setTitle("UPIITrade - Panel de Trading");
        setSize(700, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(600, 420));

        initComponentes();

        // Cargar los datos iniciales al abrir
        refrescarDatos();
    }

    private void initComponentes() {
        // Panel superior: info del usuario
        JPanel panelInfo = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        panelInfo.setBackground(new Color(30, 30, 60));

        JLabel lblNombre = new JLabel("Cliente:");
        lblNombre.setForeground(Color.LIGHT_GRAY);
        lblNombre.setFont(new Font("Arial", Font.BOLD, 13));

        lblNombreValor = new JLabel("---");
        lblNombreValor.setForeground(Color.WHITE);
        lblNombreValor.setFont(new Font("Arial", Font.PLAIN, 13));

        JLabel lblSaldo = new JLabel("   Saldo disponible:");
        lblSaldo.setForeground(Color.LIGHT_GRAY);
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 13));

        lblSaldoValor = new JLabel("$0.00");
        lblSaldoValor.setForeground(new Color(100, 220, 100)); // verde para el dinero
        lblSaldoValor.setFont(new Font("Arial", Font.BOLD, 14));

        panelInfo.add(lblNombre);
        panelInfo.add(lblNombreValor);
        panelInfo.add(lblSaldo);
        panelInfo.add(lblSaldoValor);

        // Panel central - tabla de activos
        // Columnas: Simbolo, Nombre, Precio
        String[] columnas = {"Simbolo", "Nombre del Activo", "Precio ($)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            // Hacemos la tabla de solo lectura para que el usuario no edite celdas
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tablaActivos = new JTable(modeloTabla);
        tablaActivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaActivos.setRowHeight(24);
        tablaActivos.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        tablaActivos.setFont(new Font("Arial", Font.PLAIN, 12));

        // Ajustar ancho de columnas
        tablaActivos.getColumnModel().getColumn(0).setPreferredWidth(80);
        tablaActivos.getColumnModel().getColumn(1).setPreferredWidth(250);
        tablaActivos.getColumnModel().getColumn(2).setPreferredWidth(120);

        JScrollPane scrollTabla = new JScrollPane(tablaActivos);
        scrollTabla.setBorder(BorderFactory.createTitledBorder("Activos disponibles en el mercado"));

        // Panel inferior: acciones de compra/venta
        JPanel panelAcciones = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 10));
        panelAcciones.setBorder(BorderFactory.createTitledBorder("Operaciones"));

        panelAcciones.add(new JLabel("Cantidad:"));

        campCantidad = new JTextField(8);
        campCantidad.setToolTipText("Ingresa la cantidad de activos a comprar o vender");
        panelAcciones.add(campCantidad);

        btnComprar = new JButton("  Comprar  ");
        btnComprar.setBackground(new Color(34, 139, 34));
        btnComprar.setForeground(Color.WHITE);
        btnComprar.setFocusPainted(false);
        btnComprar.setFont(new Font("Arial", Font.BOLD, 12));

        btnVender = new JButton("  Vender  ");
        btnVender.setBackground(new Color(180, 30, 30));
        btnVender.setForeground(Color.WHITE);
        btnVender.setFocusPainted(false);
        btnVender.setFont(new Font("Arial", Font.BOLD, 12));

        panelAcciones.add(btnComprar);
        panelAcciones.add(btnVender);

        // ---- Listeners de los botones ----
        btnComprar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarCompra();
            }
        });

        btnVender.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                manejarVenta();
            }
        });

        // ---- Ensamblado del layout principal ----
        setLayout(new BorderLayout());
        add(panelInfo, BorderLayout.NORTH);
        add(scrollTabla, BorderLayout.CENTER);
        add(panelAcciones, BorderLayout.SOUTH);
    }

    // Refresca todos los datos de la pantalla leyendo el estado actual del Mercado.

    public void refrescarDatos() {
        SwingUtilities.invokeLater(() -> {
            Mercado mercado = Mercado.getInstancia();

            // Actualizar nombre y saldo del cliente
            lblNombreValor.setText(mercado.getNombreCliente());
            lblSaldoValor.setText(String.format("$%.2f", mercado.getSaldoCliente()));

            // Limpiar y volver a llenar la tabla con los activos actuales
            modeloTabla.setRowCount(0);

            List<Activo> activos = mercado.getActivos();
            for (Activo activo : activos) {
                Object[] fila = {
                    activo.getSimbolo(),
                    activo.getNombre(),
                    String.format("%.2f", activo.getPrecio())
                };
                modeloTabla.addRow(fila);
            }
        });
    }

    // Maneja el evento del boton "Comprar".

    private void manejarCompra() {
        int filaSeleccionada = tablaActivos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecciona un activo de la tabla primero.",
                "Sin seleccion",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int cantidad = validarCantidad();
        if (cantidad <= 0) return; // validarCantidad ya muestra el error

        // Obtener el simbolo del activo seleccionado
        String simbolo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        // Llamar al metodo del Alumno 5 que maneja las operaciones de compra.
        try {
            Mercado.getInstancia().comprar(simbolo, cantidad);

            // Refrescar para ver el saldo actualizado (solo si la compra tuvo exito)
            refrescarDatos();

        } catch (SimboloNoValidoException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Simbolo invalido",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (FondosInsuficientesException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Fondos insuficientes",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    //Maneja el evento del boton "Vender".

    private void manejarVenta() {
        int filaSeleccionada = tablaActivos.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Selecciona un activo de la tabla primero.",
                "Sin seleccion",
                JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        int cantidad = validarCantidad();
        if (cantidad <= 0) return;

        String simbolo = (String) modeloTabla.getValueAt(filaSeleccionada, 0);

        // Llamar al metodo del Alumno 5 que maneja las operaciones de venta. Mercado.vender() debe declarar "throws SimboloNoValidoException, InventarioInsuficienteException" y delegar en ProcesadorOrdenes.procesarVenta().
        try {
            Mercado.getInstancia().vender(simbolo, cantidad);
            refrescarDatos();

        } catch (SimboloNoValidoException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Simbolo invalido",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (InventarioInsuficienteException ex) {
            JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Inventario insuficiente",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    /**
     * Lee y valida el campo de cantidad.
     * Regresa la cantidad como entero, o -1 si hay un error.
     */
    private int validarCantidad() {
        String texto = campCantidad.getText().trim();

        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(
                this,
                "Ingresa una cantidad antes de operar.",
                "Cantidad vacia",
                JOptionPane.WARNING_MESSAGE
            );
            return -1;
        }

        try {
            int cantidad = Integer.parseInt(texto);
            if (cantidad <= 0) {
                throw new NumberFormatException("Debe ser positivo");
            }
            return cantidad;
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                this,
                "La cantidad debe ser un numero entero positivo.\nEjemplo: 10",
                "Cantidad invalida",
                JOptionPane.ERROR_MESSAGE
            );
            return -1;
        }
    }
}
