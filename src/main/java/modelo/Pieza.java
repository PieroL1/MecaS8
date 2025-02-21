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
}
