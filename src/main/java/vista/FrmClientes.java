package vista;

import presentador.ClientePresentador;
import modelo.Cliente;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class FrmClientes extends JFrame {
    private ClientePresentador presentador;
    private JTextArea txtClientes;

    public FrmClientes() {
        presentador = new ClientePresentador();
        setTitle("Gestión de Clientes");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Área de texto para mostrar clientes
        txtClientes = new JTextArea();
        txtClientes.setEditable(false);
        add(new JScrollPane(txtClientes), BorderLayout.CENTER);

        // Botón para cargar clientes
        JButton btnCargar = new JButton("Cargar Clientes");
        btnCargar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cargarClientes();
            }
        });

        add(btnCargar, BorderLayout.SOUTH);
        setVisible(true);
    }

    public void cargarClientes() {
        List<Cliente> clientes = presentador.obtenerClientes();
        txtClientes.setText("");

        for (Cliente c : clientes) {
            txtClientes.append(c.getId() + " - " + c.getNombre() + "\n");
        }
    }

    public static void main(String[] args) {
        new FrmClientes();
    }
}
