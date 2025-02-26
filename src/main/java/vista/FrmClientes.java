package vista;

import modelo.Cliente;
import presentador.ClientePresentador;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmClientes extends JFrame {
    private ClientePresentador presentador;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtCorreo;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar, btnCargar, btnLimpiar;
    
    // Colores más vibrantes
    private Color colorPrimario = new Color(25, 118, 210); // Azul más brillante
    private Color colorSecundario = new Color(240, 240, 240); // Gris claro para fondo
    private Color colorHeaderTabla = new Color(13, 71, 161); // Azul oscuro para header de tabla
    private Color colorFilaSeleccionada = new Color(144, 202, 249); // Azul claro para selección
    private Color colorFilaPar = new Color(232, 245, 253); // Azul muy claro para filas pares
    
    public FrmClientes() {
        presentador = new ClientePresentador();
        configurarVentana();
        inicializarComponentes();
        establecerEventos();
        setLocationRelativeTo(null);
        cargarClientes(); // Cargamos los clientes al iniciar
    }
    
    private void configurarVentana() {
        setTitle("Sistema de Gestión de Clientes");
        setSize(900, 650); // Ventana más grande
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(colorSecundario);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (Exception e) {
            System.err.println("Error al establecer Look & Feel: " + e.getMessage());
        }
    }
    
    private void inicializarComponentes() {
        // Panel de cabecera con degradado
        JPanel panelHeader = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, new Color(25, 118, 210), w, h, new Color(13, 71, 161));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
            }
        };
        panelHeader.setPreferredSize(new Dimension(getWidth(), 60));
        JLabel lblTitulo = new JLabel("ADMINISTRACIÓN DE CLIENTES", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(new EmptyBorder(15, 0, 15, 0));
        panelHeader.add(lblTitulo);
        add(panelHeader, BorderLayout.NORTH);
        
        // Panel principal con división clara
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setContinuousLayout(true);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        
        // Panel izquierdo (formulario)
        JPanel panelFormulario = new JPanel(new BorderLayout(10, 10));
        panelFormulario.setBackground(Color.WHITE);
        panelFormulario.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 0, 1, Color.LIGHT_GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        
        // Título del formulario
        JLabel lblTituloForm = new JLabel("Información del Cliente");
        lblTituloForm.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloForm.setForeground(colorPrimario);
        lblTituloForm.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, colorPrimario),
                BorderFactory.createEmptyBorder(0, 0, 10, 0)));
        panelFormulario.add(lblTituloForm, BorderLayout.NORTH);
        
        // Campos del formulario 
        JPanel camposPanel = new JPanel(new GridLayout(8, 1, 0, 15));
        camposPanel.setBackground(Color.WHITE);
        
        camposPanel.add(crearLabelField("Nombre:"));
        txtNombre = crearTextField();
        camposPanel.add(txtNombre);
        
        camposPanel.add(crearLabelField("Dirección:"));
        txtDireccion = crearTextField();
        camposPanel.add(txtDireccion);
        
        camposPanel.add(crearLabelField("Teléfono:"));
        txtTelefono = crearTextField();
        camposPanel.add(txtTelefono);
        
        camposPanel.add(crearLabelField("Correo:"));
        txtCorreo = crearTextField();
        camposPanel.add(txtCorreo);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(camposPanel, BorderLayout.NORTH);
        panelFormulario.add(centerPanel, BorderLayout.CENTER);
        
        // Botón Limpiar en el formulario
        JPanel panelBtnLimpiar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBtnLimpiar.setBackground(Color.WHITE);
        btnLimpiar = crearBoton("Limpiar", new Color(158, 158, 158));
        btnLimpiar.setIcon(UIManager.getIcon("OptionPane.informationIcon"));
        btnLimpiar.setPreferredSize(new Dimension(150, 40));
        panelBtnLimpiar.add(btnLimpiar);
        panelFormulario.add(panelBtnLimpiar, BorderLayout.SOUTH);
        
        splitPane.setLeftComponent(panelFormulario);
        
        // Panel derecho (tabla)
        JPanel panelTabla = new JPanel(new BorderLayout(10, 10));
        panelTabla.setBackground(Color.WHITE);
        panelTabla.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Título de la tabla
        JLabel lblTituloTabla = new JLabel("Listado de Clientes");
        lblTituloTabla.setFont(new Font("Arial", Font.BOLD, 16));
        lblTituloTabla.setForeground(colorPrimario);
        lblTituloTabla.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, colorPrimario),
                BorderFactory.createEmptyBorder(0, 0, 10, 0)));
        panelTabla.add(lblTituloTabla, BorderLayout.NORTH);
        
        // Tabla con formato mejorado
        modeloTabla = new DefaultTableModel(
                new String[]{"ID", "Nombre", "Dirección", "Teléfono", "Correo"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tablaClientes = new JTable(modeloTabla) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    comp.setBackground(colorFilaSeleccionada);
                    comp.setForeground(Color.BLACK);
                } else {
                    comp.setBackground(row % 2 == 0 ? colorFilaPar : Color.WHITE);
                    comp.setForeground(Color.BLACK);
                }
                return comp;
            }
        };
        
        tablaClientes.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaClientes.setRowHeight(30);
        tablaClientes.setIntercellSpacing(new Dimension(10, 5));
        tablaClientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tablaClientes.setShowGrid(true);
        tablaClientes.setGridColor(new Color(230, 230, 230));
        tablaClientes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Personalizar encabezado de tabla
        JTableHeader header = tablaClientes.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(colorHeaderTabla);
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 40));
        header.setReorderingAllowed(false);
        
        JScrollPane scrollTabla = new JScrollPane(tablaClientes);
        scrollTabla.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelTabla.add(scrollTabla, BorderLayout.CENTER);
        
        splitPane.setRightComponent(panelTabla);
        add(splitPane, BorderLayout.CENTER);
        
        // Panel de botones con colores vibrantes
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        panelBotones.setBackground(colorSecundario);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        btnAgregar = crearBoton("Agregar", new Color(46, 174, 50)); // Verde más brillante
        btnAgregar.setIcon(UIManager.getIcon("FileView.fileIcon"));
        
        btnEditar = crearBoton("Editar", new Color(33, 150, 243)); // Azul brillante
        btnEditar.setIcon(UIManager.getIcon("FileView.directoryIcon"));
        
        btnEliminar = crearBoton("Eliminar", new Color(211, 47, 47)); // Rojo brillante
        btnEliminar.setIcon(UIManager.getIcon("OptionPane.warningIcon"));
        
        btnCargar = crearBoton("Actualizar", new Color(96, 125, 139)); // Gris azulado
        btnCargar.setIcon(UIManager.getIcon("FileView.refreshIcon"));
        
        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        
        add(panelBotones, BorderLayout.SOUTH);
    }
    
    private JLabel crearLabelField(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(70, 70, 70));
        return label;
    }
    
    private JTextField crearTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 2, 0, colorPrimario),
                BorderFactory.createEmptyBorder(5, 7, 5, 7)));
        return textField;
    }
    
    private JButton crearBoton(String texto, Color bgColor) {
        JButton boton = new JButton(texto);
        boton.setFont(new Font("Arial", Font.BOLD, 14));
        boton.setBackground(bgColor);
        boton.setForeground(Color.WHITE);
        boton.setFocusPainted(false);
        boton.setBorderPainted(false);
        boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        boton.setPreferredSize(new Dimension(150, 45));
        
        // Efecto hover con brillo
        boton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                boton.setBackground(hacerColorMasBrillante(bgColor));
                boton.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                boton.setBackground(bgColor);
                boton.setBorder(null);
            }
        });
        
        return boton;
    }
    
    private Color hacerColorMasBrillante(Color color) {
        int r = Math.min(255, color.getRed() + 30);
        int g = Math.min(255, color.getGreen() + 30);
        int b = Math.min(255, color.getBlue() + 30);
        return new Color(r, g, b);
    }
    
    private void establecerEventos() {
        btnAgregar.addActionListener(e -> agregarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnCargar.addActionListener(e -> cargarClientes());
        btnLimpiar.addActionListener(e -> limpiarFormulario());
        
        // Evento de selección en la tabla
        tablaClientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaClientes.getSelectedRow();
                if (fila != -1) {
                    txtNombre.setText(modeloTabla.getValueAt(fila, 1).toString());
                    txtDireccion.setText(modeloTabla.getValueAt(fila, 2).toString());
                    txtTelefono.setText(modeloTabla.getValueAt(fila, 3).toString());
                    txtCorreo.setText(modeloTabla.getValueAt(fila, 4).toString());
                }
            }
        });
    }
    
    private void limpiarFormulario() {
        txtNombre.setText("");
        txtDireccion.setText("");
        txtTelefono.setText("");
        txtCorreo.setText("");
        tablaClientes.clearSelection();
        txtNombre.requestFocus();
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        
        // Validación básica
        if (nombre.isEmpty() || telefono.isEmpty()) {
            mostrarMensaje("El nombre y teléfono son obligatorios", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (presentador.agregarCliente(nombre, direccion, telefono, correo)) {
            mostrarMensaje("Cliente agregado exitosamente", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarClientes();
        } else {
            mostrarMensaje("Error al agregar cliente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void editarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un cliente para editar", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nombre = txtNombre.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();
        
        // Validación básica
        if (nombre.isEmpty() || telefono.isEmpty()) {
            mostrarMensaje("El nombre y teléfono son obligatorios", "Error de validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        
        if (presentador.modificarCliente(id, nombre, direccion, telefono, correo)) {
            mostrarMensaje("Cliente actualizado exitosamente", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
            limpiarFormulario();
            cargarClientes();
        } else {
            mostrarMensaje("Error al actualizar cliente", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void eliminarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            mostrarMensaje("Seleccione un cliente para eliminar", "Selección requerida", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nombre = modeloTabla.getValueAt(fila, 1).toString();
        
        // Diálogo de confirmación personalizado
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        
        int confirmacion = JOptionPane.showConfirmDialog(
            this, 
            "¿Está seguro de eliminar al cliente " + nombre + "?", 
            "Confirmar eliminación", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (presentador.eliminarCliente(id)) {
                mostrarMensaje("Cliente eliminado exitosamente", "Operación exitosa", JOptionPane.INFORMATION_MESSAGE);
                limpiarFormulario();
                cargarClientes();
            } else {
                mostrarMensaje("Error al eliminar cliente", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarClientes() {
        modeloTabla.setRowCount(0);
        List<Cliente> clientes = presentador.obtenerClientes();
        
        if (clientes != null && !clientes.isEmpty()) {
            for (Cliente c : clientes) {
                modeloTabla.addRow(new Object[]{c.getId(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getCorreo()});
            }
        } else {
            // Mensaje cuando no hay datos
            JLabel lblNoData = new JLabel("No hay clientes registrados. Agregue uno nuevo.", JLabel.CENTER);
            lblNoData.setFont(new Font("Arial", Font.BOLD, 14));
            lblNoData.setForeground(Color.GRAY);
            tablaClientes.add(lblNoData);
        }
    }
    
    private void mostrarMensaje(String mensaje, String titulo, int tipo) {
        // Personalizar los diálogos
        UIManager.put("OptionPane.background", Color.WHITE);
        UIManager.put("Panel.background", Color.WHITE);
        
        JOptionPane.showMessageDialog(this, mensaje, titulo, tipo);
    }
    
}