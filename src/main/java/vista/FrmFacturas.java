package vista;

import modelo.Factura;
import presentador.FacturaPresentador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmFacturas extends JFrame {
    private JTable tablaFacturas;
    private JButton btnGenerar, btnPagar, btnRefrescar, btnGenerarComprobante;
    private FacturaPresentador presentador;

    public FrmFacturas() {
        setTitle("Gestión de Facturación");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        presentador = new FacturaPresentador();

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4, 10, 10));
        panelBotones.setBorder(BorderFactory.createTitledBorder("Acciones"));

        btnGenerar = new JButton("Generar Factura");
        btnGenerar.setFont(new Font("Arial", Font.BOLD, 16));
        btnGenerar.setBackground(new Color(34, 139, 34));
        btnGenerar.setForeground(Color.WHITE);
        btnGenerar.addActionListener(e -> generarFactura());

        btnPagar = new JButton("Registrar Pago");
        btnPagar.setFont(new Font("Arial", Font.BOLD, 16));
        btnPagar.setBackground(new Color(70, 130, 180));
        btnPagar.setForeground(Color.WHITE);
        btnPagar.addActionListener(e -> registrarPago());

        btnRefrescar = new JButton("Refrescar");
        btnRefrescar.setFont(new Font("Arial", Font.PLAIN, 16));
        btnRefrescar.setBackground(new Color(255, 140, 0));
        btnRefrescar.setForeground(Color.WHITE);
        btnRefrescar.addActionListener(e -> cargarFacturas());

        btnGenerarComprobante = new JButton("Generar Comprobante");
        btnGenerarComprobante.setFont(new Font("Arial", Font.PLAIN, 16));
        btnGenerarComprobante.setBackground(new Color(75, 0, 130));
        btnGenerarComprobante.setForeground(Color.WHITE);
        btnGenerarComprobante.addActionListener(e -> generarComprobantePDF());

        panelBotones.add(btnGenerar);
        panelBotones.add(btnPagar);
        panelBotones.add(btnRefrescar);
        panelBotones.add(btnGenerarComprobante);

        // Tabla de facturas
        tablaFacturas = new JTable();
        tablaFacturas.setFont(new Font("Arial", Font.PLAIN, 16));
        tablaFacturas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tablaFacturas.setRowHeight(30);
        tablaFacturas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Inicializar la tabla
        cargarFacturas();

        JScrollPane scrollPane = new JScrollPane(tablaFacturas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Añadir componentes al frame
        JLabel lblTitulo = new JLabel("Sistema de Facturación - Taller Mecánico", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(0, 128, 128));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
    }

    /**
     * Carga las facturas en la tabla
     */
    private void cargarFacturas() {
        List<Factura> facturas = presentador.obtenerFacturas();

        String[] columnas = {"ID", "Orden ID", "Fecha", "Total (S/)", "Estado"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };

        for (Factura f : facturas) {
            Object[] fila = {
                f.getId(),
                f.getOrdenId(),
                f.getFecha(),
                String.format("%.2f", f.getTotal()),
                f.getEstado()
            };
            modelo.addRow(fila);
        }

        tablaFacturas.setModel(modelo);

        // Ajustar ancho de columnas
        tablaFacturas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaFacturas.getColumnModel().getColumn(1).setPreferredWidth(70);
        tablaFacturas.getColumnModel().getColumn(2).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaFacturas.getColumnModel().getColumn(4).setPreferredWidth(100);
    }

    /**
     * Muestra un diálogo para generar una factura
     */
    private void generarFactura() {
        try {
            String input = JOptionPane.showInputDialog(this, 
                "Ingrese el ID de la orden de reparación:", 
                "Generar Factura", 
                JOptionPane.QUESTION_MESSAGE);

            if (input != null && !input.trim().isEmpty()) {
                int ordenId = Integer.parseInt(input);

                if (presentador.generarFactura(ordenId)) {
                    JOptionPane.showMessageDialog(this, 
                        "Factura generada correctamente",
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarFacturas();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al generar la factura.\nVerifique que la orden exista y no tenga ya una factura.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese un número válido para el ID de la orden.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Muestra un diálogo para registrar un pago
     */
    private void registrarPago() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione una factura para pagar.",
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int facturaId = (int) tablaFacturas.getValueAt(filaSeleccionada, 0);
        String estado = (String) tablaFacturas.getValueAt(filaSeleccionada, 4);
        double total = Double.parseDouble(((String) tablaFacturas.getValueAt(filaSeleccionada, 3)).replace(",", "."));

        if ("Pagado".equals(estado)) {
            JOptionPane.showMessageDialog(this, 
                "Esta factura ya está pagada.",
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Opciones de método de pago
        String[] metodosOptions = {"Efectivo", "Tarjeta", "Transferencia"};
        String metodoPago = (String) JOptionPane.showInputDialog(this,
            "Seleccione el método de pago:",
            "Registrar Pago",
            JOptionPane.QUESTION_MESSAGE,
            null,
            metodosOptions,
            metodosOptions[0]);

        if (metodoPago != null) {
            if (presentador.registrarPago(facturaId, total, metodoPago)) {
                JOptionPane.showMessageDialog(this, 
                    "Pago registrado correctamente",
                    "Éxito", 
                    JOptionPane.INFORMATION_MESSAGE);
                cargarFacturas();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al registrar el pago.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Genera un comprobante de pago en PDF
     */
    private void generarComprobantePDF() {
        int filaSeleccionada = tablaFacturas.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione una factura para generar el comprobante.",
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int facturaId = (int) tablaFacturas.getValueAt(filaSeleccionada, 0);
        if (presentador.generarComprobantePDF(facturaId, facturaId, null)) { // Puedes ajustar el pagoId según tu lógica
            JOptionPane.showMessageDialog(this, 
                "Comprobante de pago generado correctamente en la carpeta de descargas",
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Error al generar el comprobante de pago.",
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}