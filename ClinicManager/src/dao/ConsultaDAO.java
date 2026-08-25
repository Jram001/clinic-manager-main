package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import logico.ConexionDB;
import logico.Consulta;
import logico.EnfermedadBajoVigilancia;
import logico.Medico;
import logico.Paciente;

public class ConsultaDAO {

    public static boolean registrarConsulta(Consulta c, HashMap<Integer, String[]> vacunasInfo,
            HashMap<Integer, String> medicamentosInfo, ArrayList<Integer> examenesIds) {
        String sql = "INSERT INTO Consulta (fecha, sintomas, diagnostico, esImportante, id_medico, id_paciente, id_enfermedad) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String sqlVacuna = "INSERT INTO AplicacionVacuna (id_paciente, id_vacuna, fechaAplicacion, lote, numeroDosis) VALUES (?, ?, ?, ?, ?)";
        String sqlReceta = "INSERT INTO Receta (id_consulta, fecha) VALUES (?, ?)";
        String sqlDetalleReceta = "INSERT INTO DetalleReceta (id_receta, id_medicamento, instrucciones) VALUES (?, ?, ?)";
        String sqlOrdenExamen = "INSERT INTO OrdenExamen (id_consulta, id_examen, fechaOrden, estado) VALUES (?, ?, ?, 'Pendiente')";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            int idPaciente = Integer.parseInt(c.getPaciente().getId());

            try (PreparedStatement ps = con.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                ps.setDate(1, java.sql.Date.valueOf(c.getFecha()));
                ps.setString(2, c.getSintomas());
                ps.setString(3, c.getDiagnostico());
                ps.setBoolean(4, c.isEsImportante());
                ps.setInt(5, Integer.parseInt(c.getMedico().getId()));
                ps.setInt(6, idPaciente);

                if (c.getEnfermedadVigilada() != null && c.getEnfermedadVigilada().getId() != null) {
                    ps.setInt(7, Integer.parseInt(c.getEnfermedadVigilada().getId()));
                } else {
                    ps.setNull(7, java.sql.Types.INTEGER);
                }

                ps.executeUpdate();

                int idConsulta = 0;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        idConsulta = rs.getInt(1);
                        c.setId(String.valueOf(idConsulta));
                    }
                }

                if (vacunasInfo != null && !vacunasInfo.isEmpty()) {
                    try (PreparedStatement psVacuna = con.prepareStatement(sqlVacuna)) {
                        for (java.util.Map.Entry<Integer, String[]> entry : vacunasInfo.entrySet()) {
                            Integer idVacuna = entry.getKey();
                            String[] info = entry.getValue();
                            String lote = info[0];
                            int numeroDosis = Integer.parseInt(info[1]);

                            psVacuna.setInt(1, idPaciente);
                            psVacuna.setInt(2, idVacuna);
                            psVacuna.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                            psVacuna.setString(4, lote);
                            psVacuna.setInt(5, numeroDosis);
                            psVacuna.executeUpdate();
                        }
                    }
                }

                if (medicamentosInfo != null && !medicamentosInfo.isEmpty()) {
                    int idReceta = 0;
                    try (PreparedStatement psReceta = con.prepareStatement(sqlReceta,
                            java.sql.Statement.RETURN_GENERATED_KEYS)) {
                        psReceta.setInt(1, idConsulta);
                        psReceta.setDate(2, java.sql.Date.valueOf(c.getFecha()));
                        psReceta.executeUpdate();

                        try (ResultSet rsReceta = psReceta.getGeneratedKeys()) {
                            if (rsReceta.next()) {
                                idReceta = rsReceta.getInt(1);
                            }
                        }
                    }

                    if (idReceta > 0) {
                        try (PreparedStatement psDetalle = con.prepareStatement(sqlDetalleReceta)) {
                            for (java.util.Map.Entry<Integer, String> entry : medicamentosInfo.entrySet()) {
                                psDetalle.setInt(1, idReceta);
                                psDetalle.setInt(2, entry.getKey());
                                psDetalle.setString(3, entry.getValue());
                                psDetalle.executeUpdate();
                            }
                        }
                    }
                }

                if (examenesIds != null && !examenesIds.isEmpty()) {
                    try (PreparedStatement psExamen = con.prepareStatement(sqlOrdenExamen)) {
                        for (Integer idExamen : examenesIds) {
                            psExamen.setInt(1, idConsulta);
                            psExamen.setInt(2, idExamen);
                            psExamen.setDate(3, java.sql.Date.valueOf(java.time.LocalDate.now()));
                            psExamen.executeUpdate();
                        }
                    }
                }

                con.commit();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al registrar consulta: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }

    public static ArrayList<Consulta> listarConsultasPorPaciente(String id_paciente) {
        ArrayList<Consulta> consultas = new ArrayList<>();
        String sql = "SELECT * FROM Consulta WHERE id_paciente = ? ORDER BY fecha DESC;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id_paciente));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String id = String.valueOf(rs.getInt("id"));
                    String sintomas = rs.getString("sintomas");
                    String diagnostico = rs.getString("diagnostico");
                    boolean esImportante = rs.getBoolean("esImportante");
                    String id_medico = String.valueOf(rs.getInt("id_medico"));
                    String id_enfermedad = rs.getString("id_enfermedad");

                    Paciente pac = PacienteDAO.buscarPacientePorId(id_paciente);
                    Medico med = MedicoDAO.buscarMedicoPorId(id_medico);
                    EnfermedadBajoVigilancia enf = null;
                    if (id_enfermedad != null) {
                        enf = EnfermedadDAO.buscarEnfermedadPorId(id_enfermedad);
                    }

                    Consulta c = new Consulta(id, pac, med, sintomas, diagnostico, enf, esImportante);
                    c.setFecha(rs.getDate("fecha").toLocalDate());

                    consultas.add(c);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al listar consultas: " + e.getMessage());
        }
        return consultas;
    }
}
