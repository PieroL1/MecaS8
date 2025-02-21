package modelo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String contrasena;

    // Constructor
    public Usuario(int id, String nombre, String usuario, String contrasena) {
        this.id = id;
        this.nombre = nombre;
        this.usuario = usuario;
        this.contrasena = contrasena;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUsuario() { return usuario; }

    // Método para validar credenciales
    public static Usuario validarCredenciales(String usuario, String contrasena) {
        String sql = "SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?";
        try (Connection con = Database.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("usuario"),
                    rs.getString("contrasena")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error en autenticación: " + e.getMessage());
        }
        return null;
    }
}
