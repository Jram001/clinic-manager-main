package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.SeguroMedico;

public class SeguroMedicoDAO {

    public static ArrayList<SeguroMedico> listarSeguros() {
        ArrayList<SeguroMedico> seguros = new ArrayList<>();
        String sql = "SELECT id, RNC, nombre, telefono, porcentajeCobertura, esActivo FROM SeguroMedico";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                SeguroMedico s = new SeguroMedico(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("RNC"),
                        rs.getString("telefono"),
                        rs.getFloat("porcentajeCobertura"));
                s.setEsActivo(rs.getBoolean("esActivo"));
                seguros.add(s);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar seguros medicos: " + e.getMessage());
        }
        return seguros;
    }

    public static boolean registrarSeguro(SeguroMedico seguro) {
        String sql = "INSERT INTO SeguroMedico (RNC, nombre, telefono, porcentajeCobertura, esActivo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, seguro.getRnc());
            ps.setString(2, seguro.getNombre());
            ps.setString(3, seguro.getTelefono());
            ps.setFloat(4, seguro.getPorcentajeCobertura());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar seguro: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarSeguro(SeguroMedico seguro) {
        String sql = "UPDATE SeguroMedico SET RNC = ?, nombre = ?, telefono = ?, porcentajeCobertura = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, seguro.getRnc());
            ps.setString(2, seguro.getNombre());
            ps.setString(3, seguro.getTelefono());
            ps.setFloat(4, seguro.getPorcentajeCobertura());
            ps.setInt(5, Integer.parseInt(seguro.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar seguro: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarSeguro(String id) {
        String sql = "UPDATE SeguroMedico SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar seguro: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarSeguro(String id) {
        String sql = "UPDATE SeguroMedico SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar seguro: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarSeguroDefinitivo(String id) {
        String sql = "DELETE FROM SeguroMedico WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar seguro definitivamente: " + e.getMessage());
            return false;
        }
    }
}
