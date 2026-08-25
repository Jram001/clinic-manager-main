package dao;

import logico.Usuario;
import logico.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public static Usuario autenticar(String nombreUsuario, String claveEncriptada) {
        Usuario usuario = null;

        String sql = "SELECT nombreUsuario, rol, id_persona, esActivo FROM Usuario WHERE nombreUsuario = ? AND clave = ?";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nombreUsuario);
            ps.setString(2, claveEncriptada);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario(rs.getString("nombreUsuario"), claveEncriptada, rs.getString("rol"),
                            String.valueOf(rs.getInt("id_persona")));
                    usuario.setEsActivo(rs.getBoolean("esActivo"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Error en autenticación: " + e.getMessage());
        }

        return usuario;
    }
}
