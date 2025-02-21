package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Vehiculo {
    private int id;
    private int clienteId;
    private String clienteNombre;
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private String tipo;

    public Vehiculo(int id, int clienteId, String clienteNombre, String placa, String marca, String modelo, int anio, String tipo) {
        this.id = id;
        this.clienteId = clienteId;
        this.clienteNombre = clienteNombre;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public int getClienteId() { return clienteId; }
    public String getClienteNombre() { return clienteNombre; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public String getTipo() { return tipo; }

    public static List<Vehiculo> obtenerTodos() {
        List<Vehiculo> vehiculos = new ArrayList<>();
        String sql = "SELECT v.id, v.cliente_id, c.nombre AS cliente_nombre, v.placa, v.marca, v.modelo, v.anio, v.tipo FROM vehiculos v JOIN clientes c ON v.cliente_id = c.id";

        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                vehiculos.add(new Vehiculo(
                    rs.getInt("id"),
                    rs.getInt("cliente_id"),
                    rs.getString("cliente_nombre"),
                    rs.getString("placa"),
                    rs.getString("marca"),
                    rs.getString("modelo"),
                    rs.getInt("anio"),
                    rs.getString("tipo")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener vehículos: " + e.getMessage());
        }
        return vehiculos;
    }

    public boolean guardar() {
        String sql = "INSERT INTO vehiculos (cliente_id, placa, marca, modelo, anio, tipo) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setString(2, placa);
            ps.setString(3, marca);
            ps.setString(4, modelo);
            ps.setInt(5, anio);
            ps.setString(6, tipo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean editar() {
        String sql = "UPDATE vehiculos SET cliente_id = ?, placa = ?, marca = ?, modelo = ?, anio = ?, tipo = ? WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, clienteId);
            ps.setString(2, placa);
            ps.setString(3, marca);
            ps.setString(4, modelo);
            ps.setInt(5, anio);
            ps.setString(6, tipo);
            ps.setInt(7, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al editar vehículo: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar() {
        String sql = "DELETE FROM vehiculos WHERE id = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar vehículo: " + e.getMessage());
            return false;
        }
    }
}
