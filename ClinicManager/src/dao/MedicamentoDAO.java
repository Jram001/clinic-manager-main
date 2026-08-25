package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.Medicamento;

public class MedicamentoDAO {

    public static ArrayList<Medicamento> listarMedicamentos() {
        ArrayList<Medicamento> medicamentos = new ArrayList<>();
        String sql = "SELECT id, nombre, presentacion, principioActivo, concentracion, esActivo FROM Medicamento";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medicamento m = new Medicamento(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("principioActivo"),
                        rs.getString("presentacion"),
                        rs.getString("concentracion"));
                m.setEsActivo(rs.getBoolean("esActivo"));
                medicamentos.add(m);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar medicamentos: " + e.getMessage());
        }
        return medicamentos;
    }

    public static boolean registrarMedicamento(Medicamento m) {
        String sql = "INSERT INTO Medicamento (nombre, presentacion, principioActivo, concentracion, esActivo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getPresentacion());
            ps.setString(3, m.getPrincipioActivo());
            ps.setString(4, m.getConcentracion());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al registrar: " + ex.getMessage());
            return false;
        }
    }

    public static boolean actualizarMedicamento(Medicamento m) {
        String sql = "UPDATE Medicamento SET nombre = ?, presentacion = ?, principioActivo = ?, concentracion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getNombre());
            ps.setString(2, m.getPresentacion());
            ps.setString(3, m.getPrincipioActivo());
            ps.setString(4, m.getConcentracion());
            ps.setInt(5, Integer.parseInt(m.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar: " + ex.getMessage());
            return false;
        }
    }

    public static boolean eliminarMedicamento(String id) {
        String sql = "UPDATE Medicamento SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar medicamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarMedicamento(String id) {
        String sql = "UPDATE Medicamento SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar medicamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarMedicamentoDefinitivo(String id) {
        String sql = "DELETE FROM Medicamento WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar medicamento definitivamente: " + e.getMessage());
            return false;
        }
    }
}
