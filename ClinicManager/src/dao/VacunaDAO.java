package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.Vacuna;

public class VacunaDAO {

    public static ArrayList<Vacuna> listarVacunas() {
        ArrayList<Vacuna> vacunas = new ArrayList<>();
        String sql = "SELECT * FROM Vacuna;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Vacuna vacuna = new Vacuna(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("fabricante"),
                        rs.getFloat("dosis"),
                        rs.getString("descripcion"));
                vacuna.setEsActivo(rs.getBoolean("esActivo"));
                vacunas.add(vacuna);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar vacunas: " + e.getMessage());
        }

        return vacunas;
    }

    public static boolean registrarVacuna(Vacuna vacuna) {
        String sql = "INSERT INTO Vacuna (nombre, fabricante, dosis, descripcion, esActivo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vacuna.getNombre());
            ps.setString(2, vacuna.getFabricante());
            ps.setFloat(3, vacuna.getDosis());
            ps.setString(4, vacuna.getDescripcion());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar vacuna: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarVacuna(Vacuna vacuna) {
        String sql = "UPDATE Vacuna SET nombre = ?, fabricante = ?, dosis = ?, descripcion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, vacuna.getNombre());
            ps.setString(2, vacuna.getFabricante());
            ps.setFloat(3, vacuna.getDosis());
            ps.setString(4, vacuna.getDescripcion());
            ps.setInt(5, Integer.parseInt(vacuna.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar vacuna: " + ex.getMessage());
            return false;
        }
    }

    public static boolean eliminarVacuna(String id) {
        String sql = "UPDATE Vacuna SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar vacuna: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarVacuna(String id) {
        String sql = "UPDATE Vacuna SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar vacuna: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarVacunaDefinitivo(String id) {
        String sql = "DELETE FROM Vacuna WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar vacuna definitivamente: " + e.getMessage());
            return false;
        }
    }
}
