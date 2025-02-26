package vista;

import modelo.Vehiculo;
import modelo.Cliente;
import presentador.VehiculoPresentador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmVehiculos extends JFrame {
    private VehiculoPresentador presentador;
    private JTextField txtPlaca, txtMarca, txtModelo, txtAnio, txtTipo;
    private JComboBox<String> cbClientes;
    private JTable tablaVehiculos;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar, btnCargar, btnHistorial, btnLimpiar;
    
    // Paleta de colores
    private final Color COLOR_PRIMARY = new Color(66, 139, 202);
    private final Color COLOR_SECONDARY = new Color(51, 51, 51);
    private final Color COLOR_BACKGROUND = new Color(240, 240, 240);
    private final Color COLOR_TEXT = new Color(33, 33, 33);
    private final Color COLOR_BUTTON = new Color(66, 139, 202);
    private final Color COLOR_BUTTON_TEXT = Color.WHITE;
    private final Color COLOR_HEADER = new Color(66, 139, 202);
    private final Color COLOR_TABLE_ALTERNATE = new Color(245, 245, 245);

    public FrmVehiculos() {
        presentador = new VehiculoPresentador();
        configurarVentana();
        inicializarComponentes();
        aplicarEstilos();
        configurarEventos();
        
        pack();
        setLocationRelativeTo(null);
        cargarVehiculos();
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión de Vehículos");
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(COLOR_BACKGROUND);
    }
    
    private void inicializarComponentes() {
        // Panel superior con título
        JPanel panelTitulo = new JPanel(new BorderLayout());
        panelTitulo.setBackground(COLOR_PRIMARY);
        JLabel lblTitulo = new JLabel("GESTIÓN DE VEHÍCULOS", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        panelTitulo.add(lblTitulo, BorderLayout.CENTER);
        
        add(panelTitulo, BorderLayout.NORTH);
        
        // Panel principal con diseño de cuadrícula
        JPanel panelPrincipal = new JPanel(new BorderLayout(10, 10));
        panelPrincipal.setBorder(new EmptyBorder(10, 10, 10, 10));
        panelPrincipal.setBackground(COLOR_BACKGROUND);
        
        // Panel de formulario
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        panelFormulario.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                "Datos del Vehículo",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                COLOR_PRIMARY));
        panelFormulario.setBackground(COLOR_BACKGROUND);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Cliente
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblCliente = new JLabel("Cliente:");
        lblCliente.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblCliente, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        cbClientes = new JComboBox<>();
        cbClientes.setPreferredSize(new Dimension(250, 30));
        cargarClientes();
        panelFormulario.add(cbClientes, gbc);
        
        // Placa
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel lblPlaca = new JLabel("Placa:");
        lblPlaca.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblPlaca, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        txtPlaca = new JTextField();
        txtPlaca.setPreferredSize(new Dimension(150, 30));
        panelFormulario.add(txtPlaca, gbc);
        
        // Marca
        gbc.gridx = 2;
        gbc.gridy = 1;
        JLabel lblMarca = new JLabel("Marca:");
        lblMarca.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblMarca, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 1;
        txtMarca = new JTextField();
        txtMarca.setPreferredSize(new Dimension(150, 30));
        panelFormulario.add(txtMarca, gbc);
        
        // Modelo
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblModelo = new JLabel("Modelo:");
        lblModelo.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblModelo, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 2;
        txtModelo = new JTextField();
        txtModelo.setPreferredSize(new Dimension(150, 30));
        panelFormulario.add(txtModelo, gbc);
        
        // Año
        gbc.gridx = 2;
        gbc.gridy = 2;
        JLabel lblAnio = new JLabel("Año:");
        lblAnio.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblAnio, gbc);
        
        gbc.gridx = 3;
        gbc.gridy = 2;
        txtAnio = new JTextField();
        txtAnio.setPreferredSize(new Dimension(150, 30));
        panelFormulario.add(txtAnio, gbc);
        
        // Tipo
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblTipo = new JLabel("Tipo:");
        lblTipo.setFont(new Font("Arial", Font.BOLD, 12));
        panelFormulario.add(lblTipo, gbc);
        
        gbc.gridx = 1;
        gbc.gridy = 3;
        txtTipo = new JTextField();
        txtTipo.setPreferredSize(new Dimension(150, 30));
        panelFormulario.add(txtTipo, gbc);
        
        panelPrincipal.add(panelFormulario, BorderLayout.NORTH);
        
        // Tabla de vehículos
        JPanel panelTabla = new JPanel(new BorderLayout(5, 5));
        panelTabla.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY, 1),
                "Lista de Vehículos",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 14),
                COLOR_PRIMARY));
        panelTabla.setBackground(COLOR_BACKGROUND);
        
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Placa", "Marca", "Modelo", "Año", "Tipo", "Cliente"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaVehiculos = new JTable(modeloTabla);
        tablaVehiculos.setRowHeight(25);
        tablaVehiculos.setFont(new Font("Arial", Font.PLAIN, 12));
        tablaVehiculos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaVehiculos.setShowGrid(true);
        tablaVehiculos.setGridColor(new Color(200, 200, 200));
        
        JScrollPane scrollPane = new JScrollPane(tablaVehiculos);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        panelTabla.add(scrollPane, BorderLayout.CENTER);
        
        panelPrincipal.add(panelTabla, BorderLayout.CENTER);
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panelBotones.setBackground(COLOR_BACKGROUND);
        
        btnAgregar = crearBoton("Agregar");
        btnEditar = crearBoton("Editar");
        btnEliminar = crearBoton("Eliminar");
        btnCargar = crearBoton("Recargar");
        btnHistorial = crearBoton("Ver Historial");
        btnLimpiar = crearBoton("Limpiar");
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelBotones.add(btnHistorial);
        panelBotones.add(btnLimpiar);
        
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);
        
        add(panelPrincipal, BorderLayout.CENTER);
        
        // Barra de estado
        JPanel panelEstado = new JPanel(new BorderLayout());
        panelEstado.setBackground(COLOR_SECONDARY);
        JLabel lblEstado = new JLabel(" Sistema de Taller Mecánico v1.0 © 2025", JLabel.LEFT);
        lblEstado.setFont(new Font("Arial", Font.PLAIN, 11));
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBorder(new EmptyBorder(3, 5, 3, 5));
        panelEstado.add(lblEstado, BorderLayout.WEST);
        
        add(panelEstado, BorderLayout.SOUTH);
    }
    
    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setPreferredSize(new Dimension(130, 40));
        boton.setFont(new Font("Arial", Font.BOLD, 12));
        boton.setForeground(COLOR_BUTTON_TEXT);
        boton.setBackground(COLOR_BUTTON);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        return boton;
    }
    
    private void aplicarEstilos() {
        // Estilo de la tabla
        JTableHeader header = tablaVehiculos.getTableHeader();
        header.setBackground(COLOR_HEADER);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        
        // Renderer para colorear filas alternativas
        tablaVehiculos.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component comp = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    comp.setBackground(row % 2 == 0 ? Color.WHITE : COLOR_TABLE_ALTERNATE);
                    comp.setForeground(COLOR_TEXT);
                }
                
                setHorizontalAlignment(column == 0 ? JLabel.CENTER : JLabel.LEFT);
                return comp;
            }
        });
    }
    
    private void configurarEventos() {
        // Listeners de botones
        btnAgregar.addActionListener(e -> agregarVehiculo());
        btnEditar.addActionListener(e -> editarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnCargar.addActionListener(e -> cargarVehiculos());
        btnHistorial.addActionListener(e -> verHistorial());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        // Evento de selección en la tabla
        tablaVehiculos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaVehiculos.getSelectedRow();
                if (fila != -1) {
                    seleccionarFila(fila);
                }
            }
        });
    }
    
    private void seleccionarFila(int fila) {
        txtPlaca.setText(modeloTabla.getValueAt(fila, 1).toString());
        txtMarca.setText(modeloTabla.getValueAt(fila, 2).toString());
        txtModelo.setText(modeloTabla.getValueAt(fila, 3).toString());
        txtAnio.setText(modeloTabla.getValueAt(fila, 4).toString());
        txtTipo.setText(modeloTabla.getValueAt(fila, 5).toString());

        String clienteNombre = modeloTabla.getValueAt(fila, 6).toString();
        for (int i = 0; i < cbClientes.getItemCount(); i++) {
            if (cbClientes.getItemAt(i).contains(clienteNombre)) {
                cbClientes.setSelectedIndex(i);
                break;
            }
        }
    }
    
    private void limpiarFormulario() {
        txtPlaca.setText("");
        txtMarca.setText("");
        txtModelo.setText("");
        txtAnio.setText("");
        txtTipo.setText("");
        if (cbClientes.getItemCount() > 0) {
            cbClientes.setSelectedIndex(0);
        }
        tablaVehiculos.clearSelection();
    }

    private void cargarClientes() {
        List<Cliente> clientes = Cliente.obtenerTodos();
        cbClientes.removeAllItems();
        for (Cliente c : clientes) {
            cbClientes.addItem(c.getId() + " - " + c.getNombre());
        }
    }

    private void agregarVehiculo() {
        if (!validarCampos()) {
            return;
        }
        
        String cliente = cbClientes.getSelectedItem().toString().split(" - ")[0];
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String anio = txtAnio.getText();
        String tipo = txtTipo.getText();

        if (presentador.agregarVehiculo(cliente, placa, marca, modelo, anio, tipo)) {
            mostrarMensaje("Vehículo agregado exitosamente", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculos();
            limpiarFormulario();
        } else {
            mostrarMensaje("Error al agregar vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarVehiculo() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un vehículo para editar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!validarCampos()) {
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String cliente = cbClientes.getSelectedItem().toString().split(" - ")[0];
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String anio = txtAnio.getText();
        String tipo = txtTipo.getText();

        if (presentador.modificarVehiculo(id, cliente, placa, marca, modelo, anio, tipo)) {
            mostrarMensaje("Vehículo editado exitosamente", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculos();
        } else {
            mostrarMensaje("Error al editar vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarVehiculo() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un vehículo para eliminar", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String placa = modeloTabla.getValueAt(fila, 1).toString();
        
        int respuesta = JOptionPane.showConfirmDialog(
                this,
                "¿Está seguro de eliminar el vehículo con placa " + placa + "?",
                "Confirmar Eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
                
        if (respuesta != JOptionPane.YES_OPTION) {
            return;
        }

        if (presentador.eliminarVehiculo(id)) {
            mostrarMensaje("Vehículo eliminado exitosamente", "Operación Exitosa", JOptionPane.INFORMATION_MESSAGE);
            cargarVehiculos();
            limpiarFormulario();
        } else {
            mostrarMensaje("Error al eliminar vehículo", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cargarVehiculos() {
        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        modeloTabla.setRowCount(0);
        List<Vehiculo> vehiculos = presentador.obtenerVehiculos();
        for (Vehiculo v : vehiculos) {
            modeloTabla.addRow(new Object[]{
                v.getId(),
                v.getPlaca(),
                v.getMarca(),
                v.getModelo(),
                v.getAnio(),
                v.getTipo(),
                v.getClienteNombre()
            });
        }
        setCursor(Cursor.getDefaultCursor());
    }

    private void verHistorial() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un vehículo para ver el historial", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idVehiculo = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String placaVehiculo = modeloTabla.getValueAt(fila, 1).toString();
        String marcaVehiculo = modeloTabla.getValueAt(fila, 2).toString();
        String modeloVehiculo = modeloTabla.getValueAt(fila, 3).toString();
        
        FrmHistorialReparaciones frmHistorial = new FrmHistorialReparaciones(idVehiculo);
        frmHistorial.setTitle("Historial de Reparaciones - " + marcaVehiculo + " " + modeloVehiculo + " (" + placaVehiculo + ")");
        frmHistorial.setVisible(true);
    }
    
    private boolean validarCampos() {
        if (cbClientes.getSelectedItem() == null) {
            mostrarMensaje("Debe seleccionar un cliente", "Validación", JOptionPane.WARNING_MESSAGE);
            cbClientes.requestFocus();
            return false;
        }
        
        if (txtPlaca.getText().trim().isEmpty()) {
            mostrarMensaje("La placa no puede estar vacía", "Validación", JOptionPane.WARNING_MESSAGE);
            txtPlaca.requestFocus();
            return false;
        }
        
        if (txtMarca.getText().trim().isEmpty()) {
            mostrarMensaje("La marca no puede estar vacía", "Validación", JOptionPane.WARNING_MESSAGE);
            txtMarca.requestFocus();
            return false;
        }
        
        if (txtModelo.getText().trim().isEmpty()) {
            mostrarMensaje("El modelo no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            txtModelo.requestFocus();
            return false;
        }
        
        if (txtAnio.getText().trim().isEmpty()) {
            mostrarMensaje("El año no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            txtAnio.requestFocus();
            return false;
        }
        
        try {
            int anio = Integer.parseInt(txtAnio.getText().trim());
            if (anio < 1900 || anio > 2100) {
                mostrarMensaje("El año debe estar entre 1900 y 2100", "Validación", JOptionPane.WARNING_MESSAGE);
                txtAnio.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("El año debe ser un número válido", "Validación", JOptionPane.WARNING_MESSAGE);
            txtAnio.requestFocus();
            return false;
        }
        
        if (txtTipo.getText().trim().isEmpty()) {
            mostrarMensaje("El tipo no puede estar vacío", "Validación", JOptionPane.WARNING_MESSAGE);
            txtTipo.requestFocus();
            return false;
        }
        
        return true;
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
}