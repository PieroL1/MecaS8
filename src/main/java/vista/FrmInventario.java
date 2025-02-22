package vista;

import modelo.Pieza;
import presentador.InventarioPresentador;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class FrmInventario extends JFrame {
    private JTable tablaPiezas;
    private JButton btnActualizarStock, btnRegistrarPieza;
    private InventarioPresentador presentador;

    public FrmInventario() {
        setTitle("Inventario de Piezas");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        
        presentador = new InventarioPresentador();
        
        // Panel de botones
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        btnActualizarStock = new JButton("Actualizar Stock");
        btnActualizarStock.setFont(new Font("Arial", Font.BOLD, 14));
        btnActualizarStock.addActionListener(e -> actualizarStock());
        
        btnRegistrarPieza = new JButton("Registrar Pieza");
        btnRegistrarPieza.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegistrarPieza.addActionListener(e -> registrarPieza());
        
        panelBotones.add(btnActualizarStock);
        panelBotones.add(btnRegistrarPieza);
        
        // Tabla de piezas
        tablaPiezas = new JTable();
        tablaPiezas.setFont(new Font("Arial", Font.PLAIN, 14));
        tablaPiezas.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tablaPiezas.setRowHeight(25);
        tablaPiezas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Inicializar la tabla
        cargarPiezas();
        
        JScrollPane scrollPane = new JScrollPane(tablaPiezas);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Añadir componentes al frame
        add(new JLabel("  Inventario de Piezas - Taller Mecánico", JLabel.CENTER), BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);
        
        setLocationRelativeTo(null);
    }

    /**
     * Carga las piezas en la tabla
     */
    private void cargarPiezas() {
        List<Pieza> piezas = presentador.obtenerPiezas();
        
        String[] columnas = {"ID", "Nombre", "Precio (S/)", "Stock"};
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer la tabla no editable
            }
        };
        
        for (Pieza p : piezas) {
            Object[] fila = {
                p.getId(),
                p.getNombre(),
                String.format("%.2f", p.getPrecio()),
                p.getStock()
            };
            modelo.addRow(fila);
        }
        
        tablaPiezas.setModel(modelo);
        
        // Ajustar ancho de columnas
        tablaPiezas.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaPiezas.getColumnModel().getColumn(1).setPreferredWidth(200);
        tablaPiezas.getColumnModel().getColumn(2).setPreferredWidth(100);
        tablaPiezas.getColumnModel().getColumn(3).setPreferredWidth(100);
    }

    /**
     * Muestra un diálogo para actualizar el stock de una pieza
     */
    private void actualizarStock() {
        int filaSeleccionada = tablaPiezas.getSelectedRow();
        
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione una pieza para actualizar el stock.",
                "Información", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int piezaId = (int) tablaPiezas.getValueAt(filaSeleccionada, 0);
        String nombrePieza = (String) tablaPiezas.getValueAt(filaSeleccionada, 1);
        
        String input = JOptionPane.showInputDialog(this, 
            "Ingrese la nueva cantidad de stock para " + nombrePieza + ":", 
            "Actualizar Stock", 
            JOptionPane.QUESTION_MESSAGE);
        
        if (input != null && !input.trim().isEmpty()) {
            try {
                int cantidad = Integer.parseInt(input);
                if (presentador.actualizarStock(piezaId, cantidad)) {
                    JOptionPane.showMessageDialog(this, 
                        "Stock actualizado correctamente",
                        "Éxito", 
                        JOptionPane.INFORMATION_MESSAGE);
                    cargarPiezas();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al actualizar el stock.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, ingrese un número válido para la cantidad.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Muestra un diálogo para registrar una nueva pieza
     */
    private void registrarPieza() {
        JTextField nombreField = new JTextField();
        JTextField precioField = new JTextField();
        JTextField stockField = new JTextField();
        
        Object[] message = {
            "Nombre:", nombreField,
            "Precio (S/):", precioField,
            "Stock:", stockField
        };
        
        int option = JOptionPane.showConfirmDialog(this, message, "Registrar Pieza", JOptionPane.OK_CANCEL_OPTION);
        
        if (option == JOptionPane.OK_OPTION) {
            String nombre = nombreField.getText().trim();
            String precioStr = precioField.getText().trim();
            String stockStr = stockField.getText().trim();
            
            if (!nombre.isEmpty() && !precioStr.isEmpty() && !stockStr.isEmpty()) {
                try {
                    double precio = Double.parseDouble(precioStr);
                    int stock = Integer.parseInt(stockStr);
                    Pieza nuevaPieza = new Pieza(0, nombre, precio, stock);
                    
                    if (presentador.registrarPieza(nuevaPieza)) {
                        JOptionPane.showMessageDialog(this, 
                            "Pieza registrada correctamente",
                            "Éxito", 
                            JOptionPane.INFORMATION_MESSAGE);
                        cargarPiezas();
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Error al registrar la pieza.",
                            "Error", 
                            JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, 
                        "Por favor, ingrese valores válidos para el precio y el stock.",
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Por favor, complete todos los campos.",
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}