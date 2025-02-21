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
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Tabla para mostrar historial de reparaciones
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Fecha Ingreso", "Fecha Entrega", "Estado", "Total"}, 0);
        tablaHistorial = new JTable(modeloTabla);
        add(new JScrollPane(tablaHistorial), BorderLayout.CENTER);

        cargarHistorial();

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
                orden.getTotal()
            });
        }
    }
}
