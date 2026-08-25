package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.Paciente;

public class PacienteDAO {

    public static Paciente buscarPacientePorId(String id) {
        if (id == null)
            return null;
        for (Paciente p : listarPacientes()) {
            if (p.getId().trim().equals(id.trim())) {
                return p;
            }
        }
        return null;
    }

    public static ArrayList<Paciente> listarPacientes() {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        String sql = "SELECT pac.id, per.cedula, per.nombre, per.apellido, per.edad, per.sexo, per.esActivo, pac.telefono, pac.direccion, pac.peso, pac.estatura, pac.tipoSangre, pac.id_seguro, pac.id_persona FROM Paciente pac INNER JOIN Persona per ON pac.id_persona = per.id WHERE per.esActivo = 1;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("edad"),
                        rs.getString("cedula"),
                        rs.getString("sexo"),
                        rs.getFloat("peso"),
                        rs.getFloat("estatura"),
                        rs.getString("tipoSangre"),
                        rs.getString("direccion"),
                        rs.getString("telefono"));
                paciente.setActivo(rs.getBoolean("esActivo"));

                int idSeguro = rs.getInt("id_seguro");
                if (!rs.wasNull()) {
                    for (logico.SeguroMedico s : SeguroMedicoDAO.listarSeguros()) {
                        if (s.getId().equals(String.valueOf(idSeguro))) {
                            paciente.setSeguro(s);
                            break;
                        }
                    }
                }
                pacientes.add(paciente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }

    public static boolean registrarPaciente(Paciente paciente) {
        String sqlPersona = "INSERT INTO Persona (cedula, nombre, apellido, edad, sexo, esActivo) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlPaciente = "INSERT INTO Paciente (telefono, direccion, peso, estatura, tipoSangre, id_seguro, id_persona) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona,
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, paciente.getCedula());
                psPersona.setString(2, paciente.getNombre());
                psPersona.setString(3, paciente.getApellido());
                psPersona.setInt(4, paciente.getEdad());
                psPersona.setString(5, paciente.getSexo());
                psPersona.executeUpdate();

                ResultSet rs = psPersona.getGeneratedKeys();
                int idPersona = 0;
                if (rs.next()) {
                    idPersona = rs.getInt(1);
                }

                try (PreparedStatement psPaciente = con.prepareStatement(sqlPaciente)) {
                    psPaciente.setString(1, paciente.getTelefono());
                    psPaciente.setString(2, paciente.getDireccion());
                    psPaciente.setFloat(3, paciente.getPeso());
                    psPaciente.setFloat(4, paciente.getEstatura());
                    psPaciente.setString(5, paciente.getTipoSangre());
                    if (paciente.getSeguro() != null && paciente.getSeguro().getId() != null) {
                        psPaciente.setInt(6, Integer.parseInt(paciente.getSeguro().getId()));
                    } else {
                        psPaciente.setNull(6, java.sql.Types.INTEGER);
                    }
                    psPaciente.setInt(7, idPersona);
                    psPaciente.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar paciente: " + e.getMessage());
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
                    System.out.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    public static boolean actualizarPaciente(Paciente paciente) {
        String sqlSelect = "SELECT id_persona FROM Paciente WHERE id = ?";
        String sqlPersona = "UPDATE Persona SET cedula = ?, nombre = ?, apellido = ?, edad = ?, sexo = ? WHERE id = ?";
        String sqlPaciente = "UPDATE Paciente SET telefono = ?, direccion = ?, peso = ?, estatura = ?, tipoSangre = ?, id_seguro = ? WHERE id = ?";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            int idPersona = -1;
            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                psSelect.setString(1, paciente.getId());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    }
                }
            }

            if (idPersona != -1) {
                try (PreparedStatement psPersona = con.prepareStatement(sqlPersona)) {
                    psPersona.setString(1, paciente.getCedula());
                    psPersona.setString(2, paciente.getNombre());
                    psPersona.setString(3, paciente.getApellido());
                    psPersona.setInt(4, paciente.getEdad());
                    psPersona.setString(5, paciente.getSexo());
                    psPersona.setInt(6, idPersona);
                    psPersona.executeUpdate();
                }

                try (PreparedStatement psPaciente = con.prepareStatement(sqlPaciente)) {
                    psPaciente.setString(1, paciente.getTelefono());
                    psPaciente.setString(2, paciente.getDireccion());
                    psPaciente.setFloat(3, paciente.getPeso());
                    psPaciente.setFloat(4, paciente.getEstatura());
                    psPaciente.setString(5, paciente.getTipoSangre());
                    if (paciente.getSeguro() != null && paciente.getSeguro().getId() != null) {
                        psPaciente.setInt(6, Integer.parseInt(paciente.getSeguro().getId()));
                    } else {
                        psPaciente.setNull(6, java.sql.Types.INTEGER);
                    }
                    psPaciente.setString(7, paciente.getId());
                    psPaciente.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar paciente: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public static boolean eliminarPaciente(String idPaciente) {
        String sqlSelect = "SELECT id_persona FROM Paciente WHERE id = ?";
        String sqlUpdate = "UPDATE Persona SET esActivo = 0 WHERE id = ?";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            int idPersona = -1;
            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                psSelect.setString(1, idPaciente.trim());
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    }
                }
            }

            if (idPersona != -1) {
                try (PreparedStatement psUpdate = con.prepareStatement(sqlUpdate)) {
                    psUpdate.setInt(1, idPersona);
                    psUpdate.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar paciente: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException ex) {
                }
            }
        }
    }
}
