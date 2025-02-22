package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Pago {
    private int id;
    private int facturaId;
    private double monto;
    private String metodoPago;
    private String fecha;

    public Pago(int id, int facturaId, double monto, String metodoPago, String fecha) {
        this.id = id;
        this.facturaId = facturaId;
        this.monto = monto;
        this.metodoPago = metodoPago;
        this.fecha = fecha;
    }

    /**
     * Registra un nuevo pago en la base de datos y actualiza el estado de la factura
     * @return true si el pago se registró correctamente
     */
    public boolean registrarPago() {
        Connection con = Database.conectar();
        
        try {
            // Comenzar una transacción
            con.setAutoCommit(false);
            
            // Insertar el pago
            String sqlPago = "INSERT INTO pagos (factura_id, monto, metodo_pago, fecha) VALUES (?, ?, ?, CURDATE())";
            PreparedStatement stmtPago = con.prepareStatement(sqlPago, Statement.RETURN_GENERATED_KEYS);
            stmtPago.setInt(1, facturaId);
            stmtPago.setDouble(2, monto);
            stmtPago.setString(3, metodoPago);
            
            int affectedRows = stmtPago.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("La creación del pago falló, no se insertaron filas.");
            }
            
            // Actualizar el estado de la factura a 'Pagado'
            String sqlFactura = "UPDATE facturas SET estado = 'Pagado' WHERE id = ?";
            PreparedStatement stmtFactura = con.prepareStatement(sqlFactura);
            stmtFactura.setInt(1, facturaId);
            stmtFactura.executeUpdate();
            
            // Confirmar la transacción
            con.commit();
            con.close();
            return true;
        } catch (SQLException e) {
            try {
                // Si hay un error, revertir la transacción
                con.rollback();
            } catch (SQLException ex) {
                System.out.println("Error en rollback: " + ex.getMessage());
            }
            System.out.println("Error al registrar pago: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si una factura ya ha sido pagada
     * @param facturaId ID de la factura a verificar
     * @return true si la factura ya está pagada
     */
    public static boolean facturaEstaPagada(int facturaId) {
        Connection con = Database.conectar();
        String sql = "SELECT estado FROM facturas WHERE id = ?";
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, facturaId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return "Pagado".equals(rs.getString("estado"));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al verificar estado de factura: " + e.getMessage());
        }
        
        return false;
    }
    
    /**
     * Obtiene un pago por su ID
     * @param pagoId ID del pago
     * @return Objeto Pago o null si no existe
     */
    public static Pago obtenerPagoPorId(int pagoId) {
        Connection con = Database.conectar();
        String sql = "SELECT * FROM pagos WHERE id = ?";
        
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, pagoId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Pago pago = new Pago(
                    rs.getInt("id"),
                    rs.getInt("factura_id"),
                    rs.getDouble("monto"),
                    rs.getString("metodo_pago"),
                    rs.getString("fecha")
                );
                con.close();
                return pago;
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener pago: " + e.getMessage());
        }
        
        return null;
    }
    
    // Getters
    public int getId() {
        return id;
    }

    public int getFacturaId() {
        return facturaId;
    }

    public double getMonto() {
        return monto;
    }

    public String getMetodoPago() {
        return metodoPago;
    }
    
    public String getFecha() {
        return fecha;
    }
}