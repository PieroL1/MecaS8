package vista;

import modelo.Cliente;
import presentador.ClientePresentador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class FrmClientes extends JFrame {
    private ClientePresentador presentador;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtCorreo;
    private JTable tablaClientes;
    private DefaultTableModel modeloTabla;
    private JButton btnAgregar, btnEditar, btnEliminar, btnCargar;
    
    public FrmClientes() {
        presentador = new ClientePresentador();
        setTitle("Gestión de Clientes");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel de ingreso de datos
        JPanel panelFormulario = new JPanel(new GridLayout(5, 2));
        panelFormulario.add(new JLabel("Nombre:"));
        txtNombre = new JTextField();
        panelFormulario.add(txtNombre);
        
        panelFormulario.add(new JLabel("Dirección:"));
        txtDireccion = new JTextField();
        panelFormulario.add(txtDireccion);
        
        panelFormulario.add(new JLabel("Teléfono:"));
        txtTelefono = new JTextField();
        panelFormulario.add(txtTelefono);
        
        panelFormulario.add(new JLabel("Correo:"));
        txtCorreo = new JTextField();
        panelFormulario.add(txtCorreo);
        
        add(panelFormulario, BorderLayout.NORTH);
        
        // Tabla de clientes
        modeloTabla = new DefaultTableModel(new String[]{"ID", "Nombre", "Dirección", "Teléfono", "Correo"}, 0);
        tablaClientes = new JTable(modeloTabla);
        add(new JScrollPane(tablaClientes), BorderLayout.CENTER);

        // Panel de botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 4));
        btnAgregar = new JButton("Agregar");
        btnEditar = new JButton("Editar");
        btnEliminar = new JButton("Eliminar");
        btnCargar = new JButton("Cargar");

        panelBotones.add(btnAgregar);
        panelBotones.add(btnEditar);
        panelBotones.add(btnEliminar);
        panelBotones.add(btnCargar);
        
        add(panelBotones, BorderLayout.SOUTH);
        
        // Listeners de botones
        btnAgregar.addActionListener(e -> agregarCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnEliminar.addActionListener(e -> eliminarCliente());
        btnCargar.addActionListener(e -> cargarClientes());
        
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

        setLocationRelativeTo(null);
    }

    private void agregarCliente() {
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        
        if (presentador.agregarCliente(nombre, direccion, telefono, correo)) {
            JOptionPane.showMessageDialog(this, "Cliente agregado exitosamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al agregar cliente");
        }
    }

    private void editarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar");
            return;
        }
        
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        String nombre = txtNombre.getText();
        String direccion = txtDireccion.getText();
        String telefono = txtTelefono.getText();
        String correo = txtCorreo.getText();
        
        if (presentador.modificarCliente(id, nombre, direccion, telefono, correo)) {
            JOptionPane.showMessageDialog(this, "Cliente editado exitosamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al editar cliente");
        }
    }

    private void eliminarCliente() {
        int fila = tablaClientes.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
            return;
        }
        
        int id = Integer.parseInt(modeloTabla.getValueAt(fila, 0).toString());
        
        if (presentador.eliminarCliente(id)) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente");
        }
    }

    private void cargarClientes() {
        modeloTabla.setRowCount(0);
        List<Cliente> clientes = presentador.obtenerClientes();
        for (Cliente c : clientes) {
            modeloTabla.addRow(new Object[]{c.getId(), c.getNombre(), c.getDireccion(), c.getTelefono(), c.getCorreo()});
        }
    }
}
