package presentador;

import modelo.OrdenReparacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class InformesPresentador {

    public double calcularIngresosMensuales(String mes, String anio) {
        Connection con = modelo.Database.conectar();
        double ingresos = 0.0;
        try {
            int mesInt = obtenerMesComoEntero(mes);
            int anioInt = Integer.parseInt(anio);
            
            String sql = "SELECT SUM(total) AS ingresos FROM facturas WHERE MONTH(fecha) = ? AND YEAR(fecha) = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, mesInt);
            stmt.setInt(2, anioInt);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                ingresos = rs.getDouble("ingresos");
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al calcular ingresos mensuales: " + e.getMessage());
        }
        return ingresos;
    }

    private int obtenerMesComoEntero(String mes) {
        switch (mes.toLowerCase()) {
            case "enero": return 1;
            case "febrero": return 2;
            case "marzo": return 3;
            case "abril": return 4;
            case "mayo": return 5;
            case "junio": return 6;
            case "julio": return 7;
            case "agosto": return 8;
            case "septiembre": return 9;
            case "octubre": return 10;
            case "noviembre": return 11;
            case "diciembre": return 12;
            default: throw new IllegalArgumentException("Mes inválido: " + mes);
        }
    }

    public List<OrdenReparacion> obtenerVehiculosEnReparacion() {
        Connection con = modelo.Database.conectar();
        List<OrdenReparacion> ordenes = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ordenes_reparacion WHERE estado = 'En Proceso'";
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                ordenes.add(new OrdenReparacion(
                        rs.getInt("id"),
                        rs.getInt("vehiculo_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("estado"),
                        rs.getDate("fecha_ingreso").toString(),
                        rs.getDate("fecha_entrega") != null ? rs.getDate("fecha_entrega").toString() : "N/A",
                        rs.getDouble("total")
                ));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener vehículos en reparación: " + e.getMessage());
        }
        return ordenes;
    }

    public List<OrdenReparacion> obtenerHistorialServiciosPorCliente(int clienteId) {
        Connection con = modelo.Database.conectar();
        List<OrdenReparacion> historial = new ArrayList<>();
        try {
            String sql = "SELECT o.* FROM ordenes_reparacion o JOIN vehiculos v ON o.vehiculo_id = v.id WHERE v.cliente_id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, clienteId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                historial.add(new OrdenReparacion(
                        rs.getInt("id"),
                        rs.getInt("vehiculo_id"),
                        rs.getInt("usuario_id"),
                        rs.getString("estado"),
                        rs.getDate("fecha_ingreso").toString(),
                        rs.getDate("fecha_entrega") != null ? rs.getDate("fecha_entrega").toString() : "N/A",
                        rs.getDouble("total")
                ));
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener historial de servicios por cliente: " + e.getMessage());
        }
        return historial;
    }
}