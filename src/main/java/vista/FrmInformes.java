package vista;

import presentador.InformesPresentador;
import modelo.OrdenReparacion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.YearMonth;
import java.util.List;

public class FrmInformes extends JFrame {
    private JTable tablaInformes;
    private JButton btnIngresosMensuales, btnVehiculosReparacion, btnHistorialServicios;
    private InformesPresentador presentador;

    public FrmInformes() {
        setTitle("Reportes e Informes");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        presentador = new InformesPresentador();
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnIngresosMensuales = new JButton("Ingresos Mensuales");
        btnIngresosMensuales.setFont(new Font("Arial", Font.BOLD, 14));
        btnIngresosMensuales.addActionListener(e -> mostrarIngresosMensuales());
        
        btnVehiculosReparacion = new JButton("Vehículos en Reparación");
        btnVehiculosReparacion.setFont(new Font("Arial", Font.BOLD, 14));
        btnVehiculosReparacion.addActionListener(e -> mostrarVehiculosEnReparacion());
        
        btnHistorialServicios = new JButton("Historial de Servicios por Cliente");
        btnHistorialServicios.setFont(new Font("Arial", Font.BOLD, 14));
        btnHistorialServicios.addActionListener(e -> mostrarHistorialServicios());
        
        panelBotones.add(btnIngresosMensuales);
        panelBotones.add(btnVehiculosReparacion);
        panelBotones.add(btnHistorialServicios);
        
        // Tabla de informes
        tablaInformes = new JTable();
        tablaInformes.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaInformes.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaInformes.setRowHeight(25);
        tablaInformes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tablaInformes);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Añadir componentes al frame
        add(new JLabel("  Reportes e Informes - Taller Mecánico", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }

    /**
     * Muestra los ingresos mensuales
     */
    private void mostrarIngresosMensuales() {
        JComboBox<String> mesComboBox = new JComboBox<>(new String[]{
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        });
        JComboBox<String> anioComboBox = new JComboBox<>();
        int currentYear = YearMonth.now().getYear();
        for (int i = 2000; i <= currentYear; i++) {
            anioComboBox.addItem(String.valueOf(i));
        }

        JPanel panel = new JPanel(new GridLayout(2, 2));
        panel.add(new JLabel("Mes:"));
        panel.add(mesComboBox);
        panel.add(new JLabel("Año:"));
        panel.add(anioComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Seleccionar mes y año", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String mes = (String) mesComboBox.getSelectedItem();
            String anio = (String) anioComboBox.getSelectedItem();
            double ingresos = presentador.calcularIngresosMensuales(mes, anio);
            
            JOptionPane.showMessageDialog(this, 
                "Ingresos del mes de " + mes + " del año " + anio + ": S/ " + String.format("%.2f", ingresos),
                "Ingresos Mensuales", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Muestra los vehículos en reparación
     */
    private void mostrarVehiculosEnReparacion() {
        List<OrdenReparacion> ordenes = presentador.obtenerVehiculosEnReparacion();
        
        String[] columnas = {"ID", "Vehículo ID", "Estado", "Fecha Ingreso", "Fecha Entrega", "Total (S/)"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        for (OrdenReparacion o : ordenes) {
            Object[] fila = {
                o.getId(),
                o.getVehiculoId(),
                o.getEstado(),
                o.getFechaIngreso(),
                o.getFechaEntrega(),
                String.format("%.2f", o.getTotal())
            };
            modelo.addRow(fila);
        }
        
        tablaInformes.setModel(modelo);
        
        // Ajustar ancho de columnas
        tablaInformes.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaInformes.getColumnModel().getColumn(1).setPreferredWidth(100);
        tablaInformes.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaInformes.getColumnModel().getColumn(3).setPreferredWidth(120);
        tablaInformes.getColumnModel().getColumn(4).setPreferredWidth(120);
        tablaInformes.getColumnModel().getColumn(5).setPreferredWidth(100);
    }

    /**
     * Muestra el historial de servicios por cliente
     */
    private void mostrarHistorialServicios() {
        String clienteIdInput = JOptionPane.showInputDialog(this, 
            "Ingrese el ID del cliente:", 
            "Historial de Servicios por Cliente", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (clienteIdInput != null && !clienteIdInput.trim().isEmpty()) {
            try {
                int clienteId = Integer.parseInt(clienteIdInput);
                List<OrdenReparacion> historial = presentador.obtenerHistorialServiciosPorCliente(clienteId);
                
                String[] columnas = {"ID", "Vehículo ID", "Estado", "Fecha Ingreso", "Fecha Entrega", "Total (S/)"};
                DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false; // Hacer la tabla no editable
                    }
                };
                
                for (OrdenReparacion o : historial) {
                    Object[] fila = {
                        o.getId(),
                        o.getVehiculoId(),
                        o.getEstado(),
                        o.getFechaIngreso(),
                        o.getFechaEntrega(),
                        String.format("%.2f", o.getTotal())
                    };
                    modelo.addRow(fila);
                }
                
                tablaInformes.setModel(modelo);
                
                // Ajustar ancho de columnas
                tablaInformes.getColumnModel().getColumn(0).setPreferredWidth(50);
                tablaInformes.getColumnModel().getColumn(1).setPreferredWidth(100);
                tablaInformes.getColumnModel().getColumn(2).setPreferredWidth(100);
                tablaInformes.getColumnModel().getColumn(3).setPreferredWidth(120);
                tablaInformes.getColumnModel().getColumn(4).setPreferredWidth(120);
                tablaInformes.getColumnModel().getColumn(5).setPreferredWidth(100);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese un número válido para el ID del cliente.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}