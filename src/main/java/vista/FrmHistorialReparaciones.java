package vista;

import modelo.OrdenReparacion;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmHistorialReparaciones extends JFrame {
    private JTable tablaHistorial;
    private DefaultTableModel modeloTabla;
    private int idVehiculo;

    public FrmHistorialReparaciones(int idVehiculo) {
        this.idVehiculo = idVehiculo;
        setTitle("Historial de Reparaciones");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Tabla para mostrar historial de reparaciones
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha Ingreso", "Fecha Entrega", "Estado", "Total (S/)"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        tablaHistorial = new JTable(modeloTabla);
        tablaHistorial.setFont(new Font("Arial", Font.PLAIN, 16));
        tablaHistorial.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tablaHistorial.setRowHeight(30);
        tablaHistorial.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(tablaHistorial);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        cargarHistorial();

        // Añadir componentes al frame
        JLabel lblTitulo = new JLabel("Historial de Reparaciones - Vehículo ID: " + idVehiculo, JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setOpaque(true);
        lblTitulo.setBackground(new Color(70, 130, 180));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        add(lblTitulo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setLocationRelativeTo(null);
    }

    private void cargarHistorial() {
        modeloTabla.setRowCount(0);
        List<OrdenReparacion> historial = OrdenReparacion.obtenerHistorialPorVehiculo(idVehiculo);
        for (OrdenReparacion orden : historial) {
            modeloTabla.addRow(new Object[]{
                orden.getId(),
                orden.getFechaIngreso(),
                orden.getFechaEntrega(),
                orden.getEstado(),
                String.format("%.2f", orden.getTotal())
            });
        }
    }
}