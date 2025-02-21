package vista;

import modelo.OrdenReparacion;
import modelo.Vehiculo;
import presentador.OrdenReparacionPresentador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FrmOrdenes extends JFrame {
    private OrdenReparacionPresentador presentador;
    private JComboBox<String> cbVehiculos, cbEstado;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear, btnActualizar, btnAsignarFecha, btnCalcularTotal;
    private JTextField txtFechaEntrega, txtTotal;
    
    public FrmOrdenes() {
        presentador = new OrdenReparacionPresentador();
        setTitle("Gestión de Órdenes de Reparación");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        
        JPanel panelFormulario = new JPanel(new GridLayout(4, 2));
        panelFormulario.add(new JLabel("Vehículo:"));
        cbVehiculos = new JComboBox<>();
        cargarVehiculos();
        panelFormulario.add(cbVehiculos);
        
        panelFormulario.add(new JLabel("Estado:"));
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "En Proceso", "Completado"});
        panelFormulario.add(cbEstado);
        
        panelFormulario.add(new JLabel("Fecha de Entrega:"));
        txtFechaEntrega = new JTextField();
        panelFormulario.add(txtFechaEntrega);
        
        panelFormulario.add(new JLabel("Total:"));
        txtTotal = new JTextField();
        panelFormulario.add(txtTotal);
        
        add(panelFormulario, BorderLayout.NORTH);
        
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Vehículo", "Estado", "Fecha Ingreso", "Fecha Entrega", "Total"}, 0);
        tablaOrdenes = new JTable(modeloTabla);
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);
        
        JPanel panelBotones = new JPanel(new GridLayout(1, 4));
        btnCrear = new JButton("Crear Orden");
        btnActualizar = new JButton("Actualizar Estado");
        btnAsignarFecha = new JButton("Asignar Fecha Entrega");
        btnCalcularTotal = new JButton("Calcular Total");
        
        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAsignarFecha);
        panelBotones.add(btnCalcularTotal);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        btnCrear.addActionListener(e -> crearOrden());
        btnActualizar.addActionListener(e -> actualizarEstado());
        btnAsignarFecha.addActionListener(e -> asignarFechaEntrega());
        btnCalcularTotal.addActionListener(e -> calcularTotal());
        
        setLocationRelativeTo(null);
        cargarOrdenes();
    }
    
    private void cargarVehiculos() {
        List<Vehiculo> vehiculos = Vehiculo.obtenerTodos();
        for (Vehiculo v : vehiculos) {
            cbVehiculos.addItem(v.getId() + " - " + v.getPlaca());
        }
    }
    
    private void crearOrden() {
        int vehiculoId = Integer.parseInt(cbVehiculos.getSelectedItem().toString().split(" - ")[0]);
        if (presentador.crearOrden(vehiculoId, 1)) {  // Usuario ID temporalmente 1
            JOptionPane.showMessageDialog(this, "Orden creada exitosamente");
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al crear la orden");
        }
    }
    
    private void actualizarEstado() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para actualizar");
            return;
        }
        int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nuevoEstado = cbEstado.getSelectedItem().toString();
        
        if (presentador.actualizarEstado(ordenId, nuevoEstado)) {
            JOptionPane.showMessageDialog(this, "Estado actualizado correctamente");
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado");
        }
    }
    
    private void asignarFechaEntrega() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para asignar la fecha de entrega");
            return;
        }
        int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String fechaEntrega = txtFechaEntrega.getText();
        
        if (presentador.asignarFechaEntrega(ordenId, fechaEntrega)) {
            JOptionPane.showMessageDialog(this, "Fecha de entrega asignada correctamente");
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al asignar la fecha de entrega");
        }
    }
    
    private void calcularTotal() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para calcular el total");
            return;
        }
        int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        double nuevoTotal = Double.parseDouble(txtTotal.getText());
        
        if (presentador.calcularTotal(ordenId, nuevoTotal)) {
            JOptionPane.showMessageDialog(this, "Total actualizado correctamente");
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al actualizar el total");
        }
    }
    
    private void cargarOrdenes() {
        modeloTabla.setRowCount(0);
        List<OrdenReparacion> ordenes = presentador.obtenerOrdenesPorVehiculo(1); // ID temporal
        for (OrdenReparacion o : ordenes) {
            modeloTabla.addRow(new Object[]{o.getId(), o.getVehiculoId(), o.getEstado(), o.getFechaIngreso(), o.getFechaEntrega(), o.getTotal()});
        }
    }
}
