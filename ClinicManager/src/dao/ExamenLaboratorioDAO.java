package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.ExamenLaboratorio;

public class ExamenLaboratorioDAO {

    public static ArrayList<ExamenLaboratorio> listarExamenes() {
        ArrayList<ExamenLaboratorio> examenes = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion, categoria, precioBase, esActivo FROM ExamenLaboratorio";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                ExamenLaboratorio e = new ExamenLaboratorio(
                        String.valueOf(rs.getInt("id")),
                        rs.getString("nombre"),
                        rs.getString("descripcion"),
                        rs.getString("categoria"),
                        rs.getFloat("precioBase"));
                e.setEsActivo(rs.getBoolean("esActivo"));
                examenes.add(e);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar examenes de laboratorio: " + e.getMessage());
        }
        return examenes;
    }

    public static boolean registrarExamen(ExamenLaboratorio e) {
        String sql = "INSERT INTO ExamenLaboratorio (nombre, descripcion, categoria, precioBase, esActivo) VALUES (?, ?, ?, ?, 1)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getCategoria());
            ps.setFloat(4, e.getPrecioBase());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al registrar : " + ex.getMessage());
            return false;
        }
    }

    public static boolean actualizarExamen(ExamenLaboratorio e) {
        String sql = "UPDATE ExamenLaboratorio SET nombre = ?, descripcion = ?, categoria = ?, precioBase = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, e.getNombre());
            ps.setString(2, e.getDescripcion());
            ps.setString(3, e.getCategoria());
            ps.setFloat(4, e.getPrecioBase());
            ps.setInt(5, Integer.parseInt(e.getId()));
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            System.out.println("Error al actualizar : " + ex.getMessage());
            return false;
        }
    }

    public static boolean eliminarExamen(String id) {
        String sql = "UPDATE ExamenLaboratorio SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar examen: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarExamen(String id) {
        String sql = "UPDATE ExamenLaboratorio SET esActivo = 1 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar examen: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarExamenDefinitivo(String id) {
        String sql = "DELETE FROM ExamenLaboratorio WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar examen definitivamente: " + e.getMessage());
            return false;
        }
    }

    public static boolean registrarResultadoExamen(int idOrden, String resultado) {
        String sql = "UPDATE OrdenExamen SET resultado = ?, fechaResultado = GETDATE(), estado = 'Completado' WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, resultado);
            ps.setInt(2, idOrden);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar resultado de examen: " + e.getMessage());
            return false;
        }
    }
}
