package vista;

import modelo.Cliente;
import presentador.ClientePresentador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FrmClientes extends JFrame {
    private ClientePresentador presentador;
    private JTextField txtNombre, txtDireccion, txtTelefono, txtCorreo;
    private JTextArea txtClientes;
    private JButton btnAgregar, btnEditar, btnEliminar, btnCargar;
    private JList<String> listaClientes;
    private DefaultListModel<String> modeloLista;
    
    public FrmClientes() {
        presentador = new ClientePresentador();
        setTitle("Gestión de Clientes");
        setSize(500, 400);
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
        
        // Lista de clientes
        modeloLista = new DefaultListModel<>();
        listaClientes = new JList<>(modeloLista);
        add(new JScrollPane(listaClientes), BorderLayout.CENTER);

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
        btnAgregar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                agregarCliente();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarCliente();
            }
        });

        btnEliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarCliente();
            }
        });

        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarClientes();
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
        int index = listaClientes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para editar");
            return;
        }
        
        String[] datos = modeloLista.get(index).split(" - ");
        int id = Integer.parseInt(datos[0]);
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
        int index = listaClientes.getSelectedIndex();
        if (index == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un cliente para eliminar");
            return;
        }
        
        String[] datos = modeloLista.get(index).split(" - ");
        int id = Integer.parseInt(datos[0]);
        
        if (presentador.eliminarCliente(id)) {
            JOptionPane.showMessageDialog(this, "Cliente eliminado exitosamente");
            cargarClientes();
        } else {
            JOptionPane.showMessageDialog(this, "Error al eliminar cliente");
        }
    }

    private void cargarClientes() {
        modeloLista.clear();
        List<Cliente> clientes = presentador.obtenerClientes();
        for (Cliente c : clientes) {
            modeloLista.addElement(c.getId() + " - " + c.getNombre());
        }
    }
}
