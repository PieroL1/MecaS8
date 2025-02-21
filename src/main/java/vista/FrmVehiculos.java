package vista;

import modelo.Vehiculo;
import modelo.Cliente;
import presentador.VehiculoPresentador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
    private JButton btnAgregar, btnEditar, btnEliminar, btnCargar, btnHistorial;

    public FrmVehiculos() {
        presentador = new VehiculoPresentador();
        setTitle("Gestión de Vehículos");
        setSize(700, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de ingreso de datos
        JPanel panelFormulario = new JPanel(new GridLayout(6, 2));
        panelFormulario.add(new JLabel("Cliente:"));
        cbClientes = new JComboBox<>();
        cargarClientes();
        panelFormulario.add(cbClientes);

        panelFormulario.add(new JLabel("Placa:"));
        txtPlaca = new JTextField();
        panelFormulario.add(txtPlaca);

        panelFormulario.add(new JLabel("Marca:"));
        txtMarca = new JTextField();
        panelFormulario.add(txtMarca);

        panelFormulario.add(new JLabel("Modelo:"));
        txtModelo = new JTextField();
        panelFormulario.add(txtModelo);

        panelFormulario.add(new JLabel("Año:"));
        txtAnio = new JTextField();
        panelFormulario.add(txtAnio);

        panelFormulario.add(new JLabel("Tipo:"));
        txtTipo = new JTextField();
        panelFormulario.add(txtTipo);

        add(panelFormulario, BorderLayout.NORTH);

        // Tabla de vehículos
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Placa", "Marca", "Modelo", "Año", "Tipo", "Cliente"}, 0);
        tablaVehiculos = new JTable(modeloTabla);
        add(new JScrollPane(tablaVehiculos), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 5));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar");
        btnHistorial = new JButton("Ver Historial");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        panelBotones.add(btnHistorial);

        add(panelBotones, BorderLayout.SOUTH);

        // Listeners de botones
        btnAgregar.addActionListener(e -> agregarVehiculo());
        btnEditar.addActionListener(e -> editarVehiculo());
        btnEliminar.addActionListener(e -> eliminarVehiculo());
        btnCargar.addActionListener(e -> cargarVehiculos());
        btnHistorial.addActionListener(e -> verHistorial());

        // Evento de selección en la tabla
        tablaVehiculos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tablaVehiculos.getSelectedRow();
                if (fila != -1) {
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
            }
        });

        setLocationRelativeTo(null);
    }

    private void cargarClientes() {
        List<Cliente> clientes = Cliente.obtenerTodos();
        cbClientes.removeAllItems();
        for (Cliente c : clientes) {
            cbClientes.addItem(c.getId() + " - " + c.getNombre());
        }
    }

    private void agregarVehiculo() {
        String cliente = cbClientes.getSelectedItem().toString().split(" - ")[0];
        String placa = txtPlaca.getText();
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String anio = txtAnio.getText();
        String tipo = txtTipo.getText();

        if (presentador.agregarVehiculo(cliente, placa, marca, modelo, anio, tipo)) {
            JOptionPane.showMessageDialog(this, "Vehículo agregado exitosamente");
            cargarVehiculos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar vehículo");
        }
    }

    private void editarVehiculo() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo para editar");
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
            JOptionPane.showMessageDialog(this, "Vehículo editado exitosamente");
            cargarVehiculos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al editar vehículo");
        }
    }

    private void eliminarVehiculo() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo para eliminar");
            return;
        }

        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());

        if (presentador.eliminarVehiculo(id)) {
            JOptionPane.showMessageDialog(this, "Vehículo eliminado exitosamente");
            cargarVehiculos();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar vehículo");
        }
    }

    private void cargarVehiculos() {
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
    }

    private void verHistorial() {
        int fila = tablaVehiculos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un vehículo para ver el historial");
            return;
        }

        int idVehiculo = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        new FrmHistorialReparaciones(idVehiculo).setVisible(true);
    }
}
