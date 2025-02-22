package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Factura {
    private int id;
    private int ordenId;
    private String fecha;
    private double total;
    private String estado;

    public Factura(int id, int ordenId, String fecha, double total, String estado) {
        this.id = id;
        this.ordenId = ordenId;
        this.fecha = fecha;
        this.total = total;
        this.estado = estado;
    }

    /**
     * Genera una nueva factura en la base de datos
     * @param ordenId ID de la orden de reparación
     * @return true si la factura se generó correctamente
     */
    public static boolean generarFactura(int ordenId) {
        Connection con = Database.conectar();
        
        try {
            // Primero, calculamos el total de la orden
            double totalPiezas = calcularTotalPiezas(con, ordenId);
            double totalServicios = calcularTotalServicios(con, ordenId);
            double total = totalPiezas + totalServicios;
            
            // Actualizamos el total en la orden de reparación
            String updateOrden = "UPDATE ordenes_reparacion SET total = ? WHERE id = ?";
            PreparedStatement stmtOrden = con.prepareStatement(updateOrden);
            stmtOrden.setDouble(1, total);
            stmtOrden.setInt(2, ordenId);
            stmtOrden.executeUpdate();
            
            // Insertar la factura
            String sql = "INSERT INTO facturas (orden_id, fecha, total, estado) VALUES (?, CURDATE(), ?, 'Pendiente')";
            PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, ordenId);
            stmt.setDouble(2, total);
            
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La creación de la factura falló, no se insertaron filas.");
            }
            
            con.close();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al generar factura: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Calcula el total de las piezas usadas en una orden
     */
    private static double calcularTotalPiezas(Connection con, int ordenId) throws SQLException {
        String sql = "SELECT SUM(p.precio * dp.cantidad) as total " +
                     "FROM detalle_piezas dp " +
                     "JOIN piezas p ON dp.pieza_id = p.id " +
                     "WHERE dp.orden_id = ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, ordenId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0;
    }
    
    /**
     * Calcula el total de los servicios realizados en una orden
     */
    private static double calcularTotalServicios(Connection con, int ordenId) throws SQLException {
        String sql = "SELECT SUM(ds.costo) as total " +
                     "FROM detalle_servicio ds " +
                     "WHERE ds.orden_id = ?";
        
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, ordenId);
        ResultSet rs = stmt.executeQuery();
        
        if (rs.next()) {
            return rs.getDouble("total");
        }
        return 0;
    }

    /**
     * Obtiene todas las facturas de la base de datos
     */
    public static List<Factura> obtenerFacturas() {
        Connection con = Database.conectar();
        String sql = "SELECT * FROM facturas";
        List<Factura> facturas = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                facturas.add(new Factura(
                    rs.getInt("id"), 
                    rs.getInt("orden_id"), 
                    rs.getString("fecha"),
                    rs.getDouble("total"), 
                    rs.getString("estado")
                ));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener facturas: " + e.getMessage());
        }

        return facturas;
    }
    
    /**
     * Obtiene una factura específica por su ID
     */
    public static Factura obtenerFacturaPorId(int facturaId) {
        Connection con = Database.conectar();
        String sql = "SELECT * FROM facturas WHERE id = ?";
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Factura(
                    rs.getInt("id"), 
                    rs.getInt("orden_id"), 
                    rs.getString("fecha"),
                    rs.getDouble("total"), 
                    rs.getString("estado")
                );
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener factura: " + e.getMessage());
        }
        
        return null;
    }
    
    /**
     * Obtiene las facturas pendientes de pago
     */
    public static List<Factura> obtenerFacturasPendientes() {
        Connection con = Database.conectar();
        String sql = "SELECT * FROM facturas WHERE estado = 'Pendiente'";
        List<Factura> facturas = new ArrayList<>();

        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                facturas.add(new Factura(
                    rs.getInt("id"), 
                    rs.getInt("orden_id"), 
                    rs.getString("fecha"),
                    rs.getDouble("total"), 
                    rs.getString("estado")
                ));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener facturas pendientes: " + e.getMessage());
        }

        return facturas;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getOrdenId() {
        return ordenId;
    }

    public String getFecha() {
        return fecha;
    }

    public double getTotal() {
        return total;
    }

    public String getEstado() {
        return estado;
    }
}