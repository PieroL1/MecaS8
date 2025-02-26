package vista;

import modelo.OrdenReparacion;
import modelo.Vehiculo;
import modelo.Pieza;
import presentador.OrdenReparacionPresentador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

public class FrmOrdenes extends JFrame {
    private OrdenReparacionPresentador presentador;
    private JComboBox<String> cbVehiculos, cbEstado, cbPiezas;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear, btnActualizar, btnAsignarFecha, btnCalcularTotal, btnAsignarPieza;
    private JTextField txtFechaEntrega, txtTotal, txtCantidadPieza;
    private Color primaryColor = new Color(66, 139, 202);
    private Color accentColor = new Color(92, 184, 92);
    private Color backgroundColor = new Color(248, 249, 250);
    private Font titleFont = new Font("Segoe UI", Font.BOLD, 16);
    private Font labelFont = new Font("Segoe UI", Font.PLAIN, 14);
    private Font buttonFont = new Font("Segoe UI", Font.BOLD, 13);
    
    public FrmOrdenes() {
        presentador = new OrdenReparacionPresentador();
        setTitle("Gestión de Órdenes de Reparación");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(backgroundColor);
        
        // Panel principal con margen
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(backgroundColor);
        
        // Panel de título
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBackground(primaryColor);
        JLabel titleLabel = new JLabel("Sistema de Gestión de Órdenes de Reparación");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        
        // Panel de formulario con GridBagLayout para mejor distribución
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        
        // Sección Vehículo
        JPanel vehiculoPanel = createSectionPanel("Información del Vehículo");
        JPanel vehiculoContent = new JPanel(new GridLayout(1, 2, 10, 0));
        vehiculoContent.setOpaque(false);
        
        JLabel lblVehiculo = new JLabel("Seleccione Vehículo:");
        lblVehiculo.setFont(labelFont);
        cbVehiculos = new JComboBox<>();
        cbVehiculos.setFont(labelFont);
        decorateComboBox(cbVehiculos);
        cargarVehiculos();
        
        vehiculoContent.add(lblVehiculo);
        vehiculoContent.add(cbVehiculos);
        vehiculoPanel.add(vehiculoContent, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.gridwidth = 2;
        panelFormulario.add(vehiculoPanel, gbc);
        
        // Sección Estado y Fechas
        JPanel estadoPanel = createSectionPanel("Estado y Fechas");
        JPanel estadoContent = new JPanel(new GridLayout(2, 4, 10, 10));
        estadoContent.setOpaque(false);
        
        JLabel lblEstado = new JLabel("Estado:");
        lblEstado.setFont(labelFont);
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "En Proceso", "Completado"});
        cbEstado.setFont(labelFont);
        decorateComboBox(cbEstado);
        
        JLabel lblFechaEntrega = new JLabel("Fecha de Entrega:");
        lblFechaEntrega.setFont(labelFont);
        txtFechaEntrega = new JTextField(10);
        txtFechaEntrega.setFont(labelFont);
        decorateTextField(txtFechaEntrega);
        
        JLabel lblTotal = new JLabel("Total ($):");
        lblTotal.setFont(labelFont);
        txtTotal = new JTextField(10);
        txtTotal.setFont(labelFont);
        decorateTextField(txtTotal);
        
        estadoContent.add(lblEstado);
        estadoContent.add(cbEstado);
        estadoContent.add(lblFechaEntrega);
        estadoContent.add(txtFechaEntrega);
        estadoContent.add(lblTotal);
        estadoContent.add(txtTotal);
        estadoPanel.add(estadoContent, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 2;
        panelFormulario.add(estadoPanel, gbc);
        
        // Sección Piezas
        JPanel piezasPanel = createSectionPanel("Asignación de Piezas");
        JPanel piezasContent = new JPanel(new GridLayout(2, 4, 10, 10));
        piezasContent.setOpaque(false);
        
        JLabel lblPieza = new JLabel("Pieza Necesaria:");
        lblPieza.setFont(labelFont);
        cbPiezas = new JComboBox<>();
        cbPiezas.setFont(labelFont);
        decorateComboBox(cbPiezas);
        cargarPiezas();
        
        JLabel lblCantidad = new JLabel("Cantidad:");
        lblCantidad.setFont(labelFont);
        txtCantidadPieza = new JTextField(5);
        txtCantidadPieza.setFont(labelFont);
        decorateTextField(txtCantidadPieza);
        
        piezasContent.add(lblPieza);
        piezasContent.add(cbPiezas);
        piezasContent.add(lblCantidad);
        piezasContent.add(txtCantidadPieza);
        piezasPanel.add(piezasContent, BorderLayout.CENTER);
        
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        panelFormulario.add(piezasPanel, gbc);
        
        mainPanel.add(panelFormulario, BorderLayout.NORTH);
        
        // Tabla de órdenes con estilo mejorado
        JPanel tablaPanel = new JPanel(new BorderLayout(0, 10));
        tablaPanel.setBackground(Color.WHITE);
        tablaPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        JLabel tablaTitle = new JLabel("Listado de Órdenes de Reparación");
        tablaTitle.setFont(titleFont);
        tablaTitle.setForeground(primaryColor);
        tablaPanel.add(tablaTitle, BorderLayout.NORTH);
        
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Vehículo", "Estado", "Fecha Ingreso", "Fecha Entrega", "Total ($)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabla no editable
            }
        };
        
        tablaOrdenes = new JTable(modeloTabla);
        tablaOrdenes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tablaOrdenes.setRowHeight(30);
        tablaOrdenes.setSelectionBackground(new Color(185, 213, 237));
        tablaOrdenes.setSelectionForeground(Color.BLACK);
        tablaOrdenes.setShowGrid(true);
        tablaOrdenes.setGridColor(new Color(230, 230, 230));
        tablaOrdenes.setFillsViewportHeight(true);
        
        JTableHeader header = tablaOrdenes.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        
        JScrollPane scrollPane = new JScrollPane(tablaOrdenes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        tablaPanel.add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.add(tablaPanel, BorderLayout.CENTER);
        
        // Panel de botones con FlowLayout para alinearlos bien
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panelBotones.setBackground(Color.WHITE);
        panelBotones.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                new EmptyBorder(15, 15, 15, 15)
        ));
        
        btnCrear = createStyledButton("Crear Orden", primaryColor, Color.WHITE);
        btnActualizar = createStyledButton("Actualizar Estado", new Color(0, 123, 255), Color.WHITE);
        btnAsignarFecha = createStyledButton("Asignar Fecha", new Color(255, 193, 7), Color.BLACK);
        btnCalcularTotal = createStyledButton("Calcular Total", new Color(108, 117, 125), Color.WHITE);
        btnAsignarPieza = createStyledButton("Asignar Piezas", accentColor, Color.WHITE);
        
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAsignarFecha);
        panelBotones.add(btnCalcularTotal);
        panelBotones.add(btnAsignarPieza);
        
        mainPanel.add(panelBotones, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Listeners
        btnCrear.addActionListener(e -> crearOrden());
        btnActualizar.addActionListener(e -> actualizarEstado());
        btnAsignarFecha.addActionListener(e -> asignarFechaEntrega());
        btnCalcularTotal.addActionListener(e -> calcularTotal());
        btnAsignarPieza.addActionListener(e -> asignarPieza());
        
        // Selección en la tabla actualiza los campos
        tablaOrdenes.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && tablaOrdenes.getSelectedRow() != -1) {
                int fila = tablaOrdenes.getSelectedRow();
                actualizarCamposDesdeTabla(fila);
            }
        });
        
        setLocationRelativeTo(null);
        cargarOrdenes();
    }
    
    private JPanel createSectionPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(222, 226, 230), 1),
                title,
                TitledBorder.DEFAULT_JUSTIFICATION,
                TitledBorder.DEFAULT_POSITION,
                titleFont,
                primaryColor
        ));
        return panel;
    }
    
    private JButton createStyledButton(String text, Color bgColor, Color fgColor) {
        JButton button = new JButton(text);
        button.setFont(buttonFont);
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(150, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
    
    private void decorateTextField(JTextField textField) {
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }
    
    private void decorateComboBox(JComboBox<?> comboBox) {
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(206, 212, 218), 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
    }
    
    private void actualizarCamposDesdeTabla(int fila) {
        // Actualiza los campos con los datos de la fila seleccionada
        if (fila >= 0) {
            String estado = modeloTabla.getValueAt(fila, 2).toString();
            String fechaEntrega = modeloTabla.getValueAt(fila, 4) != null ? 
                    modeloTabla.getValueAt(fila, 4).toString() : "";
            String total = modeloTabla.getValueAt(fila, 5) != null ? 
                    modeloTabla.getValueAt(fila, 5).toString() : "";
            
            for (int i = 0; i < cbEstado.getItemCount(); i++) {
                if (cbEstado.getItemAt(i).equals(estado)) {
                    cbEstado.setSelectedIndex(i);
                    break;
                }
            }
            
            txtFechaEntrega.setText(fechaEntrega);
            txtTotal.setText(total);
            txtCantidadPieza.setText("1"); // Por defecto
        }
    }
    
    private void cargarVehiculos() {
        cbVehiculos.removeAllItems();
        List<Vehiculo> vehiculos = Vehiculo.obtenerTodos();
        for (Vehiculo v : vehiculos) {
            cbVehiculos.addItem(v.getId() + " - " + v.getPlaca() + " (" + v.getMarca() + " " + v.getModelo() + ")");
        }
    }

    private void cargarPiezas() {
        cbPiezas.removeAllItems();
        List<Pieza> piezas = Pieza.obtenerTodas();
        for (Pieza p : piezas) {
            cbPiezas.addItem(p.getId() + " - " + p.getNombre() + " ($" + p.getPrecio() + ")");
        }
    }

    private void cargarOrdenes() {
        modeloTabla.setRowCount(0);
        List<OrdenReparacion> ordenes = presentador.obtenerTodasOrdenes();
        for (OrdenReparacion o : ordenes) {
            modeloTabla.addRow(new Object[]{
                o.getId(), 
                o.getVehiculoId(), 
                o.getEstado(), 
                formatearFecha(o.getFechaIngreso()), 
                formatearFecha(o.getFechaEntrega()), 
                formatearMoneda(o.getTotal())
            });
        }
        // Ajustar el ancho de las columnas
        tablaOrdenes.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tablaOrdenes.getColumnModel().getColumn(1).setPreferredWidth(150); // Vehículo
        tablaOrdenes.getColumnModel().getColumn(2).setPreferredWidth(100); // Estado
        tablaOrdenes.getColumnModel().getColumn(3).setPreferredWidth(120); // Fecha Ingreso
        tablaOrdenes.getColumnModel().getColumn(4).setPreferredWidth(120); // Fecha Entrega
        tablaOrdenes.getColumnModel().getColumn(5).setPreferredWidth(100); // Total
    }
    
    // Métodos auxiliares para formato
    private String formatearFecha(String fecha) {
        return fecha != null ? fecha : "";
    }
    
    private String formatearMoneda(double valor) {
        return String.format("$%.2f", valor);
    }

    private void crearOrden() {
        try {
            String seleccion = cbVehiculos.getSelectedItem().toString();
            int vehiculoId = Integer.parseInt(seleccion.split(" - ")[0]);
            
            if (presentador.crearOrden(vehiculoId, 1)) {  // Usuario ID temporalmente 1
                mostrarMensajeExito("Orden creada exitosamente");
                cargarOrdenes();
            } else {
                mostrarError("Error al crear la orden");
            }
        } catch (Exception ex) {
            mostrarError("Error en los datos: " + ex.getMessage());
        }
    }

    private void actualizarEstado() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            mostrarAdvertencia("Seleccione una orden para actualizar");
            return;
        }
        
        try {
            int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            String nuevoEstado = cbEstado.getSelectedItem().toString();

            if (presentador.actualizarEstado(ordenId, nuevoEstado)) {
                mostrarMensajeExito("Estado actualizado correctamente");
                cargarOrdenes();
            } else {
                mostrarError("Error al actualizar el estado");
            }
        } catch (Exception ex) {
            mostrarError("Error en los datos: " + ex.getMessage());
        }
    }

    private void asignarPieza() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            mostrarAdvertencia("Seleccione una orden para asignar una pieza");
            return;
        }
        
        try {
            int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            String seleccion = cbPiezas.getSelectedItem().toString();
            int piezaId = Integer.parseInt(seleccion.split(" - ")[0]);
            int cantidad = Integer.parseInt(txtCantidadPieza.getText());

            if (cantidad <= 0) {
                mostrarAdvertencia("La cantidad debe ser mayor a cero");
                return;
            }

            if (presentador.asignarPieza(ordenId, piezaId, cantidad)) {
                mostrarMensajeExito("Pieza asignada correctamente");
                cargarOrdenes();
            } else {
                mostrarError("Error al asignar la pieza");
            }
        } catch (NumberFormatException ex) {
            mostrarError("Error: Cantidad debe ser un número entero");
        } catch (Exception ex) {
            mostrarError("Error en los datos: " + ex.getMessage());
        }
    }
    
    private void asignarFechaEntrega() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            mostrarAdvertencia("Seleccione una orden para asignar la fecha de entrega");
            return;
        }
        
        try {
            int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            String fechaEntrega = txtFechaEntrega.getText().trim();
            
            if (fechaEntrega.isEmpty()) {
                mostrarAdvertencia("Debe ingresar una fecha válida (YYYY-MM-DD)");
                return;
            }

            if (presentador.asignarFechaEntrega(ordenId, fechaEntrega)) {
                mostrarMensajeExito("Fecha de entrega asignada correctamente");
                cargarOrdenes();
            } else {
                mostrarError("Error al asignar la fecha de entrega");
            }
        } catch (Exception ex) {
            mostrarError("Error en el formato de fecha: " + ex.getMessage());
        }
    }

    private void calcularTotal() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            mostrarAdvertencia("Seleccione una orden para calcular el total");
            return;
        }
        
        try {
            int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
            double nuevoTotal = Double.parseDouble(txtTotal.getText().replace(",", "."));
            
            if (nuevoTotal < 0) {
                mostrarAdvertencia("El total no puede ser negativo");
                return;
            }

            if (presentador.calcularTotal(ordenId, nuevoTotal)) {
                mostrarMensajeExito("Total actualizado correctamente");
                cargarOrdenes();
            } else {
                mostrarError("Error al actualizar el total");
            }
        } catch (NumberFormatException ex) {
            mostrarError("Error: Total debe ser un número válido");
        } catch (Exception ex) {
            mostrarError("Error en los datos: " + ex.getMessage());
        }
    }
    
    // Métodos para mostrar mensajes con estilos mejorados
    private void mostrarMensajeExito(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Operación Exitosa", 
                JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", 
                JOptionPane.ERROR_MESSAGE);
    }
    
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Advertencia", 
                JOptionPane.WARNING_MESSAGE);
    }
}