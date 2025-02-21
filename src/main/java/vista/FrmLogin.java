package vista;

import modelo.Usuario;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    public static Usuario usuarioActual;

    public FrmLogin() {
        setTitle("Iniciar Sesión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        add(new JLabel("Usuario:"));
        txtUsuario = new JTextField();
        add(txtUsuario);

        add(new JLabel("Contraseña:"));
        txtContrasena = new JPasswordField();
        add(txtContrasena);

        btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                autenticar();
            }
        });
        add(btnIngresar);

        setLocationRelativeTo(null);
    }

    private void autenticar() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());

        Usuario usuarioValidado = Usuario.validarCredenciales(usuario, contrasena);
        if (usuarioValidado != null) {
            usuarioActual = usuarioValidado;
            JOptionPane.showMessageDialog(this, "Bienvenido, " + usuarioValidado.getNombre());
            dispose();
            new FrmDashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
        }
    }

}
