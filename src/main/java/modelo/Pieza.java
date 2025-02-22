package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Pieza {
    private int id;
    private String nombre;
    private double precio;
    private int stock;

    public Pieza(int id, String nombre, double precio, int stock) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }

    public static List<Pieza> obtenerTodas() {
        List<Pieza> piezas = new ArrayList<>();
        String sql = "SELECT * FROM piezas";

        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                piezas.add(new Pieza(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener piezas: " + e.getMessage());
        }
        return piezas;
    }
    
    // Métodos para manejar la base de datos
    public static Pieza obtenerPiezaPorId(int id) {
        Connection con = Database.conectar();
        Pieza pieza = null;
        try {
            String sql = "SELECT * FROM piezas WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                pieza = new Pieza(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getDouble("precio"),
                    rs.getInt("stock")
                );
            }
            con.close();
        } catch (SQLException e) {
            System.out.println("Error al obtener pieza: " + e.getMessage());
        }
        return pieza;
    }

    public static boolean actualizarStock(int id, int cantidad) {
        Connection con = Database.conectar();
        try {
            String sql = "UPDATE piezas SET stock = ? WHERE id = ?";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, cantidad);
            stmt.setInt(2, id);
            int rowsUpdated = stmt.executeUpdate();
            con.close();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar stock: " + e.getMessage());
            return false;
        }
    }
    
    public static boolean registrarPieza(Pieza pieza) {
        Connection con = Database.conectar();
        try {
            String sql = "INSERT INTO piezas (nombre, precio, stock) VALUES (?, ?, ?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setString(1, pieza.getNombre());
            stmt.setDouble(2, pieza.getPrecio());
            stmt.setInt(3, pieza.getStock());
            int rowsInserted = stmt.executeUpdate();
            con.close();
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar pieza: " + e.getMessage());
            return false;
        }
    }


}
