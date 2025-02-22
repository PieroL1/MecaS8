package vista;

import javax.swing.*;
import java.awt.*;

public class FrmDashboard extends JFrame {
    // Definición de colores modernos y equilibrados
    private static final Color COLOR_AZUL = new Color(41, 128, 185);
    private static final Color COLOR_BLANCO = Color.WHITE;
    private static final Color COLOR_GRIS_CLARO = new Color(236, 240, 241);
    private static final Color COLOR_GRIS_OSCURO = new Color(44, 62, 80);
    private static final Color COLOR_VERDE = new Color(39, 174, 96);
    private static final Color COLOR_GRIS = new Color(127, 140, 141);
    
    // Definición de fuentes más elegantes
    private static final Font FUENTE_TITULO = new Font("Roboto", Font.BOLD, 60);
    private static final Font FUENTE_BOTONES = new Font("Roboto", Font.PLAIN, 18);
    
    public FrmDashboard() {
        setTitle("Dashboard - Taller Mecánico");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        JPanel panelPrincipal = new JPanel(new BorderLayout(20, 20));
        panelPrincipal.setBackground(COLOR_GRIS_CLARO);
        
        // Encabezado con diseño moderno
        JPanel panelEncabezado = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelEncabezado.setBackground(COLOR_AZUL);
        panelEncabezado.setPreferredSize(new Dimension(0, 100));
        
        JLabel lblTitulo = new JLabel("Sistema de Gestión del Taller Mecánico");
        lblTitulo.setFont(FUENTE_TITULO);
        lblTitulo.setForeground(COLOR_BLANCO);
        
        panelEncabezado.add(lblTitulo);
        
        // Panel de botones con diseño moderno y mejor distribución
        JPanel panelBotones = new JPanel(new GridLayout(2, 3, 20, 20));
        panelBotones.setBackground(COLOR_GRIS_CLARO);
        panelBotones.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));
        
        String[] nombresBotones = {
            "Gestión de Clientes", "Gestión de Vehículos",
            "Órdenes de Reparación", "Facturación",
            "Inventario de Piezas", "Reportes e Informes"
        };
        
        for (String nombre : nombresBotones) {
            agregarBoton(panelBotones, nombre);
        }
        
        panelPrincipal.add(panelEncabezado, BorderLayout.NORTH);
        panelPrincipal.add(panelBotones, BorderLayout.CENTER);
        
        add(panelPrincipal);
        setLocationRelativeTo(null);
    }
    
    private void agregarBoton(JPanel panel, String texto) {
        JButton boton = new JButton(texto);
        boton.setFont(FUENTE_BOTONES);
        boton.setBackground(COLOR_VERDE);
        boton.setForeground(COLOR_BLANCO);
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        boton.setOpaque(true);
        boton.setPreferredSize(new Dimension(250, 80));
        boton.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_GRIS_OSCURO, 2, true),
            BorderFactory.createEmptyBorder(15, 40, 15, 40)
        ));
        
        // Añadiendo iconos a los botones y ajustando su tamaño
        ImageIcon icono = new ImageIcon(getClass().getResource("/imagenes/" + texto.replace(" ", "_") + ".png"));
        Image imagen = icono.getImage().getScaledInstance(250, 250, Image.SCALE_SMOOTH);
        boton.setIcon(new ImageIcon(imagen));
        boton.setHorizontalTextPosition(SwingConstants.CENTER);
        boton.setVerticalTextPosition(SwingConstants.BOTTOM);
        
        // Añadiendo un efecto hover
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_GRIS);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(COLOR_VERDE);
            }
        });
        
        boton.addActionListener(e -> abrirVentana(texto));
        
        panel.add(boton);
    }
    
    private void abrirVentana(String texto) {
        JFrame ventana = null;
        switch (texto) {
            case "Gestión de Clientes": ventana = new FrmClientes(); break;
            case "Gestión de Vehículos": ventana = new FrmVehiculos(); break;
            case "Órdenes de Reparación": ventana = new FrmOrdenes(); break;
            case "Facturación": ventana = new FrmFacturas(); break;
            case "Inventario de Piezas": ventana = new FrmInventario(); break;
            case "Reportes e Informes": ventana = new FrmInformes(); break;
        }
        if (ventana != null) ventana.setVisible(true);
    }
}