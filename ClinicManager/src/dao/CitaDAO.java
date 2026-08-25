package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.Cita;
import logico.ConexionDB;
import javax.swing.table.DefaultTableModel;

public class CitaDAO {

    public static boolean registrarCita(Cita cita) {
        String sql = "INSERT INTO Cita (fecha, esActivo, id_medico, id_paciente) VALUES (?, 1, ?, ?)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(cita.getFecha()));
            ps.setInt(2, Integer.parseInt(cita.getId_medico()));
            ps.setInt(3, Integer.parseInt(cita.getId_paciente()));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al registrar cita: " + e.getMessage());
            return false;
        }
    }

    public static ArrayList<Cita> listarCitas() {
        ArrayList<Cita> citas = new ArrayList<>();
        String sql = "SELECT c.id, c.fecha, perPac.nombre AS nombrePaciente, " +
                "perPac.apellido AS apellidoPaciente, perMed.nombre AS nombreMedico, " +
                "perMed.apellido AS apellidoMedico, c.id_medico, c.id_paciente " +
                "FROM Cita c " +
                "INNER JOIN Paciente pac ON c.id_paciente = pac.id " +
                "INNER JOIN Persona perPac ON pac.id_persona = perPac.id " +
                "INNER JOIN Medico med ON c.id_medico = med.id " +
                "INNER JOIN Persona perMed ON med.id_persona = perMed.id " +
                "WHERE c.esActivo = 1;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cita c = new Cita(
                        String.valueOf(rs.getInt("id")),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("nombrePaciente"),
                        rs.getString("apellidoPaciente"),
                        rs.getString("nombreMedico"),
                        rs.getString("apellidoMedico"));
                c.setId_medico(String.valueOf(rs.getInt("id_medico")));
                c.setId_paciente(String.valueOf(rs.getInt("id_paciente")));
                citas.add(c);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar citas: " + e.getMessage());
        }
        return citas;
    }

    public static int contarCitasActivasPorMedico(String idMedico, java.time.LocalDate fecha) {
        int count = 0;
        String sql = "SELECT COUNT(*) AS total FROM Cita WHERE id_medico = ? AND fecha = ? AND esActivo = 1";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idMedico));
            ps.setDate(2, java.sql.Date.valueOf(fecha));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    count = rs.getInt("total");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al contar citas activas: " + e.getMessage());
        }
        return count;
    }

    public static Cita buscarCitaPorId(String id) {
        String sql = "SELECT * FROM Cita WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Cita cita = new Cita(
                            String.valueOf(rs.getInt("id_paciente")),
                            String.valueOf(rs.getInt("id_medico")),
                            rs.getDate("fecha").toLocalDate());
                    cita.setId(String.valueOf(rs.getInt("id")));
                    cita.setEsActivo(rs.getBoolean("esActivo"));
                    return cita;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar cita por id: " + e.getMessage());
        }
        return null;
    }

    public static boolean posponerCita(String id, java.time.LocalDate nuevaFecha) {
        String sql = "UPDATE Cita SET fecha = ? WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setDate(1, java.sql.Date.valueOf(nuevaFecha));
            ps.setInt(2, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al posponer cita: " + e.getMessage());
            return false;
        }
    }

    public static boolean cancelarCita(String id) {
        String sql = "UPDATE Cita SET esActivo = 0 WHERE id = ?";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al cancelar cita: " + e.getMessage());
            return false;
        }
    }

    public static DefaultTableModel obtenerAgendaSecretaria(java.time.LocalDate fecha) {
        String[] columnNames = { "ID Cita", "Fecha", "Paciente", "Cédula", "Teléfono", "Sangre", "Seguro", "Médico",
                "Especialidad", "Departamento", "Estado", "ID Consulta" };
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

        String sql = "SELECT c.id, c.fecha, pp.nombre + ' ' + pp.apellido AS paciente, pp.cedula, ISNULL(p.telefono, 'Sin teléfono') AS telefono, ISNULL(p.tipoSangre, 'N/D') AS tipo_sangre, ISNULL(s.nombre, 'Particular') AS seguro, pm.nombre + ' ' + pm.apellido AS medico, m.especialidad, ISNULL(d.nombre, 'Sin departamento') AS departamento, CASE WHEN con.id IS NULL THEN 'Pendiente' ELSE 'Atendida' END AS estado_atencion, con.id FROM Cita AS c INNER JOIN Medico AS m ON m.id = c.id_medico INNER JOIN Persona AS pm ON pm.id = m.id_persona INNER JOIN Paciente AS p ON p.id = c.id_paciente INNER JOIN Persona AS pp ON pp.id = p.id_persona LEFT JOIN Departamento AS d ON d.id = m.id_departamento LEFT JOIN SeguroMedico AS s ON s.id = p.id_seguro OUTER APPLY (SELECT cons_ranked.id FROM (SELECT con2.id, ROW_NUMBER() OVER(ORDER BY con2.id ASC) AS rn FROM Consulta AS con2 WHERE con2.id_paciente = c.id_paciente AND con2.id_medico = c.id_medico AND CAST(con2.fecha AS DATE) = CAST(c.fecha AS DATE)) AS cons_ranked WHERE cons_ranked.rn = (SELECT COUNT(*) FROM Cita AS c2 WHERE c2.id_paciente = c.id_paciente AND c2.id_medico = c.id_medico AND CAST(c2.fecha AS DATE) = CAST(c.fecha AS DATE) AND c2.id <= c.id)) AS con WHERE c.esActivo = 1 AND CAST(c.fecha AS DATE) = ? ORDER BY m.especialidad, medico, paciente";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = new Object[12];
                    fila[0] = rs.getInt(1);
                    fila[1] = rs.getString(2);
                    fila[2] = rs.getString(3);
                    fila[3] = rs.getString(4);
                    fila[4] = rs.getString(5);
                    fila[5] = rs.getString(6);
                    fila[6] = rs.getString(7);
                    fila[7] = rs.getString(8);
                    fila[8] = rs.getString(9);
                    fila[9] = rs.getString(10);
                    fila[10] = rs.getString(11);
                    fila[11] = rs.getString(12);
                    model.addRow(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error SQL: " + e.getMessage());
            System.out.println("Error al obtener agenda: " + e.getMessage());
        }
        return model;
    }
}
