package vista;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmDashboard extends JFrame {
    public FrmDashboard() {
        setTitle("Dashboard - Taller Mecánico");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 1));

        JButton btnClientes = new JButton("Gestión de Clientes");
        JButton btnVehiculos = new JButton("Gestión de Vehículos");
        JButton btnOrdenes = new JButton("Órdenes de Reparación");
        JButton btnFacturas = new JButton("Facturación");
        JButton btnInventario = new JButton("Inventario de Piezas");
        JButton btnInformes = new JButton("Reportes e Informes");
        
        btnClientes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmClientes().setVisible(true);
            }
        });

        btnVehiculos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmVehiculos().setVisible(true);
            }
        });

        btnOrdenes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmOrdenes().setVisible(true);
            }
        });

        btnFacturas.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmFacturas().setVisible(true);
            }
        });
        
        btnInventario.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmInventario().setVisible(true);
            }
        });
        
        btnInformes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmInformes().setVisible(true);
            }
        });
        
        add(btnClientes);
        add(btnVehiculos);
        add(btnOrdenes);
        add(btnFacturas);
        add(btnInventario);
        add(btnInventario);
        add(btnInformes);

        setLocationRelativeTo(null);
    }

}
