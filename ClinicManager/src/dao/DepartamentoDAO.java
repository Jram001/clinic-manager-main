package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.Departamento;

public class DepartamentoDAO {

    public static ArrayList<Departamento> listarDepartamentos() {
        ArrayList<Departamento> departamentos = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, ubicacion, esActivo FROM Departamento";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Departamento d = new Departamento(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("ubicacion"));
                d.setEsActivo(rs.getBoolean("esActivo"));
                departamentos.add(d);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar departamentos: " + e.getMessage());
        }
        return departamentos;
    }

    public static boolean registrarDepartamento(Departamento dep) {
        String sql = "INSERT INTO Departamento (nombre, descripcion, ubicacion, esActivo) VALUES (?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dep.getNombre());
            ps.setString(2, dep.getDescripcion());
            ps.setString(3, dep.getUbicacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar departamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean actualizarDepartamento(Departamento dep) {
        String sql = "UPDATE Departamento SET nombre = ?, descripcion = ?, ubicacion = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, dep.getNombre());
            ps.setString(2, dep.getDescripcion());
            ps.setString(3, dep.getUbicacion());
            ps.setInt(4, Integer.parseInt(dep.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al actualizar departamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean eliminarDepartamento(String id) {
        String sql = "UPDATE Departamento SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar departamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarDepartamento(String id) {
        String sql = "UPDATE Departamento SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar departamento: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarDepartamentoDefinitivo(String id) {
        String sql = "DELETE FROM Departamento WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar departamento definitivamente: " + e.getMessage());
            return false;
        }
    }
}
