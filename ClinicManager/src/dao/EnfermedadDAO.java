package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.EnfermedadBajoVigilancia;

public class EnfermedadDAO {

    public static EnfermedadBajoVigilancia buscarEnfermedadPorId(String id) {
        if (id == null)
            return null;
        for (EnfermedadBajoVigilancia enf : listarEnfermedades()) {
            if (enf.getId().trim().equals(id.trim())) {
                return enf;
            }
        }
        return null;
    }

    public static ArrayList<EnfermedadBajoVigilancia> listarEnfermedades() {
        ArrayList<EnfermedadBajoVigilancia> enfermedades = new ArrayList<>();
        String sql = "SELECT * FROM EnfermedadBajoVigilancia;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                EnfermedadBajoVigilancia enfermedad = new EnfermedadBajoVigilancia(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("gravedad"));
                // esActivo should be implicitly true from constructor, but safe assignment:
                enfermedad.setEsActivo(rs.getBoolean("esActivo"));
                enfermedades.add(enfermedad);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar enfermedades: " + e.getMessage());
        }

        return enfermedades;
    }

    public static boolean registrarEnfermedad(EnfermedadBajoVigilancia enf) {
        String sql = "INSERT INTO EnfermedadBajoVigilancia (nombre, descripcion, gravedad, esActivo) VALUES (?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, enf.getNombre());
            ps.setString(2, enf.getDescripcion());
            ps.setString(3, enf.getGravedad());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar enfermedad: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarEnfermedad(EnfermedadBajoVigilancia enf) {
        String sql = "UPDATE EnfermedadBajoVigilancia SET nombre = ?, descripcion = ?, gravedad = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, enf.getNombre());
            ps.setString(2, enf.getDescripcion());
            ps.setString(3, enf.getGravedad());
            ps.setInt(4, Integer.parseInt(enf.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar enfermedad: " + ex.getMessage());
            return false;
        }
    }

    public static boolean eliminarEnfermedad(String id) {
        String sql = "UPDATE EnfermedadBajoVigilancia SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar enfermedad: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarEnfermedad(String id) {
        String sql = "UPDATE EnfermedadBajoVigilancia SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar enfermedad: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarEnfermedadDefinitivo(String id) {
        String sql = "DELETE FROM EnfermedadBajoVigilancia WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar enfermedad definitivamente: " + e.getMessage());
            return false;
        }
    }
}
