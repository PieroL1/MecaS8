package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrdenReparacion {
    private int id;
    private int vehiculoId;
    private int usuarioId;
    private String estado;
    private String fechaIngreso;
    private String fechaEntrega;
    private double total;

    public OrdenReparacion(int id, int vehiculoId, int usuarioId, String estado, String fechaIngreso, String fechaEntrega, double total) {
        this.id = id;
        this.vehiculoId = vehiculoId;
        this.usuarioId = usuarioId;
        this.estado = estado;
        this.fechaIngreso = fechaIngreso;
        this.fechaEntrega = fechaEntrega;
        this.total = total;
    }

    public int getId() { return id; }
    public int getVehiculoId() { return vehiculoId; }
    public int getUsuarioId() { return usuarioId; }
    public String getEstado() { return estado; }
    public String getFechaIngreso() { return fechaIngreso; }
    public String getFechaEntrega() { return fechaEntrega; }
    public double getTotal() { return total; }

    public static List<OrdenReparacion> obtenerHistorialPorVehiculo(int vehiculoId) {
        List<OrdenReparacion> historial = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_reparacion WHERE vehiculo_id = ?";

        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehiculoId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                historial.add(new OrdenReparacion(
                    rs.getInt("id"),
                    rs.getInt("vehiculo_id"),
                    rs.getInt("usuario_id"),
                    rs.getString("estado"),
                    rs.getString("fecha_ingreso"),
                    rs.getString("fecha_entrega"),
                    rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener historial de reparaciones: " + e.getMessage());
        }
        return historial;
    }

    public boolean crearOrden() {
        String sql = "INSERT INTO ordenes_reparacion (vehiculo_id, usuario_id, estado, fecha_ingreso, total) VALUES (?, ?, 'Pendiente', CURDATE(), 0.00)";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, vehiculoId);
            ps.setInt(2, usuarioId);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al crear la orden: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizarEstado(String nuevoEstado) {
        String sql = "UPDATE ordenes_reparacion SET estado = ? WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar estado de la orden: " + e.getMessage());
            return false;
        }
    }

    public boolean calcularTotal(double nuevoTotal) {
        String sql = "UPDATE ordenes_reparacion SET total = ? WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDouble(1, nuevoTotal);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar total de la orden: " + e.getMessage());
            return false;
        }
    }
    
    public boolean asignarFechaEntrega(String fechaEntrega) {
        String sql = "UPDATE ordenes_reparacion SET fecha_entrega = ? WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, fechaEntrega);
            ps.setInt(2, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al asignar fecha de entrega: " + e.getMessage());
            return false;
        }
    }
    
    public static OrdenReparacion obtenerOrdenPorId(int ordenId) {
        String sql = "SELECT * FROM ordenes_reparacion WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new OrdenReparacion(
                    rs.getInt("id"),
                    rs.getInt("vehiculo_id"),
                    rs.getInt("usuario_id"),
                    rs.getString("estado"),
                    rs.getString("fecha_ingreso"),
                    rs.getString("fecha_entrega"),
                    rs.getDouble("total")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener orden de reparación: " + e.getMessage());
        }
        return null;
    }
    
    
    public static boolean asignarPieza(int ordenId, int piezaId, int cantidad) {
        String sql = "INSERT INTO detalle_piezas (orden_id, pieza_id, cantidad) VALUES (?, ?, ?)";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, ordenId);
            ps.setInt(2, piezaId);
            ps.setInt(3, cantidad);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al asignar pieza a la orden: " + e.getMessage());
            return false;
        }
    }

    public static List<OrdenReparacion> obtenerTodas() {
        List<OrdenReparacion> ordenes = new ArrayList<>();
        String sql = "SELECT * FROM ordenes_reparacion";

        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                ordenes.add(new OrdenReparacion(
                    rs.getInt("id"),
                    rs.getInt("vehiculo_id"),
                    rs.getInt("usuario_id"),
                    rs.getString("estado"),
                    rs.getString("fecha_ingreso"),
                    rs.getString("fecha_entrega"),
                    rs.getDouble("total")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las órdenes: " + e.getMessage());
        }
        return ordenes;
    }

    
}
