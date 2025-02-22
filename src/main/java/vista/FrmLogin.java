package vista;

import modelo.Usuario;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class FrmLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtContrasena;
    private JButton btnIngresar;
    private JCheckBox chkRecordar;
    private JLabel lblOlvideContrasena;
    public static Usuario usuarioActual;
    
    // Colores mejorados para mejor contraste
    private static final Color BACKGROUND_COLOR = new Color(255, 255, 255);  // Fondo blanco puro
    private static final Color PRIMARY_BLUE = new Color(47, 128, 237);      // Azul más visible
    private static final Color TEXT_COLOR = new Color(33, 33, 33);          // Texto casi negro
    private static final Color FIELD_BACKGROUND = new Color(247, 248, 249); // Gris muy claro para campos
    private static final Color BORDER_COLOR = new Color(226, 232, 240);     // Gris claro para bordes
    
    public FrmLogin() {
        setTitle("Iniciar Sesión");
        setSize(350, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setLayout(new GridBagLayout());
        setContentPane(mainPanel);
        
         // Panel del formulario
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BACKGROUND_COLOR);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 30, 10, 30);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Icono personalizado
        try {
            // Alternativa usando getResource si la imagen está en el proyecto
// En tu FrmLogin.java
ImageIcon originalIcon = new ImageIcon(getClass().getResource("/imagenes/login.png"));
Image scaled = originalIcon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            JLabel lblIcon = new JLabel(new ImageIcon(scaled));
            lblIcon.setHorizontalAlignment(SwingConstants.CENTER);
            lblIcon.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            // Contenedor para centrar la imagen
            JPanel imagePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            imagePanel.setBackground(BACKGROUND_COLOR);
            imagePanel.add(lblIcon);
            
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(20, 0, 30, 0);
            gbc.anchor = GridBagConstraints.CENTER;
            formPanel.add(imagePanel, gbc);
            
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + e.getMessage());
            // Si hay error, no mostrar imagen
        }
        
        // Campo de usuario
        txtUsuario = createTextField("Usuario");
        gbc.gridy++;
        gbc.insets = new Insets(5, 30, 5, 30);
        formPanel.add(txtUsuario, gbc);
        
        // Campo de contraseña
        txtContrasena = createPasswordField("Contraseña");
        gbc.gridy++;
        formPanel.add(txtContrasena, gbc);
        
        // Panel de opciones
        JPanel optionsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        optionsPanel.setBackground(BACKGROUND_COLOR);
        
        chkRecordar = new JCheckBox("Recordarme");
        chkRecordar.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkRecordar.setForeground(TEXT_COLOR);
        chkRecordar.setBackground(BACKGROUND_COLOR);
        optionsPanel.add(chkRecordar);
        
        lblOlvideContrasena = new JLabel("¿Olvidaste tu contraseña?");
        lblOlvideContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblOlvideContrasena.setForeground(PRIMARY_BLUE);
        lblOlvideContrasena.setCursor(new Cursor(Cursor.HAND_CURSOR));
        optionsPanel.add(lblOlvideContrasena);
        
        gbc.gridy++;
        gbc.insets = new Insets(15, 30, 15, 30);
        formPanel.add(optionsPanel, gbc);
        
        // Botón de ingreso
        btnIngresar = createLoginButton("INGRESAR");
        gbc.gridy++;
        gbc.insets = new Insets(20, 30, 20, 30);
        formPanel.add(btnIngresar, gbc);
        
        mainPanel.add(formPanel);
        setLocationRelativeTo(null);
        
        // Eventos
        btnIngresar.addActionListener(e -> autenticar());
        setupForgotPasswordLink();
    }
    
    private JLabel createUserIcon() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(80, 80));
        label.setHorizontalAlignment(JLabel.CENTER);
        
        // Crear icono circular
        ImageIcon icon = new ImageIcon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Dibujar círculo azul
                g2.setColor(PRIMARY_BLUE);
                g2.fillOval(0, 0, 79, 79);
                
                // Dibujar silueta de usuario en blanco
                g2.setColor(Color.WHITE);
                // Cabeza
                g2.fillOval(25, 15, 30, 30);
                // Cuerpo
                g2.fillArc(15, 35, 50, 50, 0, 180);
                
                g2.dispose();
            }
            
            @Override
            public int getIconWidth() {
                return 80;
            }
            
            @Override
            public int getIconHeight() {
                return 80;
            }
        };
        
        label.setIcon(icon);
        return label;
    }
    
    private JTextField createTextField(String placeholder) {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        setupPlaceholder(field, placeholder);
        return field;
    }
    
    private JPasswordField createPasswordField(String placeholder) {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(TEXT_COLOR);
        field.setBackground(FIELD_BACKGROUND);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(BORDER_COLOR),
            BorderFactory.createEmptyBorder(12, 15, 12, 15)
        ));
        
        setupPlaceholder(field, placeholder);
        return field;
    }
    
    private void setupPlaceholder(JTextField field, String placeholder) {
        field.setText(placeholder);
        if (field instanceof JPasswordField) {
            ((JPasswordField) field).setEchoChar((char) 0);
        }
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar('●');
                    }
                }
                field.setBackground(Color.WHITE);
            }
            
            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    if (field instanceof JPasswordField) {
                        ((JPasswordField) field).setEchoChar((char) 0);
                    }
                }
                field.setBackground(FIELD_BACKGROUND);
            }
        });
    }
    
    private JButton createLoginButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_BLUE);
        button.setBorder(new RoundedBorder(PRIMARY_BLUE));
        button.setPreferredSize(new Dimension(0, 45));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(PRIMARY_BLUE.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(PRIMARY_BLUE);
            }
        });
        
        return button;
    }
    
    private void setupForgotPasswordLink() {
        lblOlvideContrasena.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(FrmLogin.this,
                    "Por favor, contacta al administrador para restablecer tu contraseña.",
                    "Recuperar Contraseña",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }
    
    private static class RoundedBorder extends AbstractBorder {
        private final Color color;
        
        RoundedBorder(Color color) {
            this.color = color;
        }
        
        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, width-1, height-1, 8, 8);
            g2.dispose();
        }
        
        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(1, 1, 1, 1);
        }
    }
    
    private void autenticar() {
        String usuario = txtUsuario.getText();
        String contrasena = new String(txtContrasena.getPassword());
        
        if (usuario.equals("Usuario") || String.valueOf(txtContrasena.getPassword()).equals("Contraseña")) {
            JOptionPane.showMessageDialog(this,
                "Por favor, ingrese sus credenciales",
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        Usuario usuarioValidado = Usuario.validarCredenciales(usuario, contrasena);
        if (usuarioValidado != null) {
            usuarioActual = usuarioValidado;
            JOptionPane.showMessageDialog(this,
                "Bienvenido, " + usuarioValidado.getNombre(),
                "Éxito",
                JOptionPane.INFORMATION_MESSAGE);
            dispose();
            new FrmDashboard().setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this,
                "Credenciales incorrectas",
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}