package vista;

import modelo.OrdenReparacion;
import modelo.Vehiculo;
import modelo.Pieza;
import presentador.OrdenReparacionPresentador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmOrdenes extends JFrame {
    private OrdenReparacionPresentador presentador;
    private JComboBox<String> cbVehiculos, cbEstado, cbPiezas;
    private JTable tablaOrdenes;
    private DefaultTableModel modeloTabla;
    private JButton btnCrear, btnActualizar, btnAsignarFecha, btnCalcularTotal, btnAsignarPieza;
    private JTextField txtFechaEntrega, txtTotal, txtCantidadPieza;

    public FrmOrdenes() {
        presentador = new OrdenReparacionPresentador();
        setTitle("Gestión de Órdenes de Reparación");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de formulario con GridBagLayout para mejor distribución
        JPanel panelFormulario = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        panelFormulario.add(new JLabel("Vehículo:"), gbc);
        gbc.gridx = 1;
        cbVehiculos = new JComboBox<>();
        cargarVehiculos();
        panelFormulario.add(cbVehiculos, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        panelFormulario.add(new JLabel("Estado:"), gbc);
        gbc.gridx = 1;
        cbEstado = new JComboBox<>(new String[]{"Pendiente", "En Proceso", "Completado"});
        panelFormulario.add(cbEstado, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panelFormulario.add(new JLabel("Fecha de Entrega:"), gbc);
        gbc.gridx = 1;
        txtFechaEntrega = new JTextField(10);
        panelFormulario.add(txtFechaEntrega, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        panelFormulario.add(new JLabel("Total:"), gbc);
        gbc.gridx = 1;
        txtTotal = new JTextField(10);
        panelFormulario.add(txtTotal, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        panelFormulario.add(new JLabel("Pieza Necesaria:"), gbc);
        gbc.gridx = 1;
        cbPiezas = new JComboBox<>();
        cargarPiezas();
        panelFormulario.add(cbPiezas, gbc);

        gbc.gridx = 0; gbc.gridy = 5;
        panelFormulario.add(new JLabel("Cantidad de Piezas:"), gbc);
        gbc.gridx = 1;
        txtCantidadPieza = new JTextField(5);
        panelFormulario.add(txtCantidadPieza, gbc);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla de órdenes
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Vehículo", "Estado", "Fecha Ingreso", "Fecha Entrega", "Total"}, 0);
        tablaOrdenes = new JTable(modeloTabla);
        add(new JScrollPane(tablaOrdenes), BorderLayout.CENTER);

        // Panel de botones con FlowLayout para alinearlos bien
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        btnCrear = new JButton("Crear Orden");
        btnActualizar = new JButton("Actualizar Estado");
        btnAsignarFecha = new JButton("Asignar Fecha Entrega");
        btnCalcularTotal = new JButton("Calcular Total");
        btnAsignarPieza = new JButton("Asignar Piezas");

        panelBotones.add(btnCrear);
        panelBotones.add(btnActualizar);
        panelBotones.add(btnAsignarFecha);
        panelBotones.add(btnCalcularTotal);
        panelBotones.add(btnAsignarPieza);

        add(panelBotones, BorderLayout.SOUTH);

        // Listeners
        btnCrear.addActionListener(e -> crearOrden());
        btnActualizar.addActionListener(e -> actualizarEstado());
        btnAsignarFecha.addActionListener(e -> asignarFechaEntrega());
        btnCalcularTotal.addActionListener(e -> calcularTotal());
        btnAsignarPieza.addActionListener(e -> asignarPieza());

        setLocationRelativeTo(null);
        cargarOrdenes();
    }

    private void cargarVehiculos() {
        cbVehiculos.removeAllItems();
        List<Vehiculo> vehiculos = Vehiculo.obtenerTodos();
        for (Vehiculo v : vehiculos) {
            cbVehiculos.addItem(v.getId() + " - " + v.getPlaca());
        }
    }

    private void cargarPiezas() {
        cbPiezas.removeAllItems();
        List<Pieza> piezas = Pieza.obtenerTodas();
        for (Pieza p : piezas) {
            cbPiezas.addItem(p.getId() + " - " + p.getNombre());
        }
    }

    private void cargarOrdenes() {
        modeloTabla.setRowCount(0);
        List<OrdenReparacion> ordenes = presentador.obtenerTodasOrdenes();
        for (OrdenReparacion o : ordenes) {
            modeloTabla.addRow(new Object[]{o.getId(), o.getVehiculoId(), o.getEstado(), o.getFechaIngreso(), o.getFechaEntrega(), o.getTotal()});
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

    private void asignarPieza() {
        int fila = tablaOrdenes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una orden para asignar una pieza");
            return;
        }
        int ordenId = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        int piezaId = Integer.parseInt(cbPiezas.getSelectedItem().toString().split(" - ")[0]);
        int cantidad = Integer.parseInt(txtCantidadPieza.getText());

        if (presentador.asignarPieza(ordenId, piezaId, cantidad)) {
            JOptionPane.showMessageDialog(this, "Pieza asignada correctamente");
            cargarOrdenes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al asignar la pieza");
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
    
    
}
