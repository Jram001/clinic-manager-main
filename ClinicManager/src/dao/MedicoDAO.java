package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import logico.ConexionDB;
import logico.Medico;

public class MedicoDAO {

    public static Medico buscarMedicoPorId(String id) {
        if (id == null)
            return null;
        for (Medico m : listarMedicos()) {
            if (m.getId().trim().equals(id.trim())) {
                return m;
            }
        }
        return null;
    }

    public static ArrayList<Medico> listarMedicos() {
        ArrayList<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.id, p.cedula, p.nombre, p.apellido, p.edad, p.sexo, p.esActivo, m.especialidad, m.maxCitas, m.id_departamento, m.id_persona FROM Medico m INNER JOIN Persona p ON m.id_persona = p.id WHERE p.esActivo = 1;";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("edad"),
                        rs.getString("cedula"),
                        rs.getString("sexo"),
                        rs.getString("especialidad"),
                        rs.getInt("maxCitas"));
                medico.setActivo(rs.getBoolean("esActivo"));
                medicos.add(medico);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar medicos: " + e.getMessage());
        }

        return medicos;
    }

    public static ArrayList<Medico> obtenerMedicosParaConsultas() {
        ArrayList<Medico> medicos = new ArrayList<>();
        String sql = "SELECT m.id, p.cedula, p.nombre, p.apellido, p.edad, p.sexo, p.esActivo, m.especialidad, m.maxCitas "
                +
                "FROM Medico m INNER JOIN Persona p ON m.id_persona = p.id " +
                "WHERE p.esActivo = 1 " +
                "AND m.especialidad NOT LIKE '%Patologia%' " +
                "AND m.especialidad NOT LIKE '%Patol%gica%' " +
                "AND m.especialidad NOT LIKE '%Hematologia%';";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Medico medico = new Medico(
                        rs.getString("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getInt("edad"),
                        rs.getString("cedula"),
                        rs.getString("sexo"),
                        rs.getString("especialidad"),
                        rs.getInt("maxCitas"));
                medico.setActivo(rs.getBoolean("esActivo"));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar medicos filtrados: " + e.getMessage());
        }
        return medicos;
    }

    public static boolean registrarMedico(Medico medico) {
        String sqlPersona = "INSERT INTO Persona (cedula, nombre, apellido, edad, sexo, esActivo) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlMedico = "INSERT INTO Medico (especialidad, maxCitas, id_departamento, id_persona) VALUES (?, ?, ?, ?)";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona,
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, medico.getCedula());
                psPersona.setString(2, medico.getNombre());
                psPersona.setString(3, medico.getApellido());
                psPersona.setInt(4, medico.getEdad());
                psPersona.setString(5, medico.getSexo());
                psPersona.executeUpdate();

                ResultSet rs = psPersona.getGeneratedKeys();
                int idPersona = 0;
                if (rs.next()) {
                    idPersona = rs.getInt(1);
                }

                try (PreparedStatement psMedico = con.prepareStatement(sqlMedico)) {
                    psMedico.setString(1, medico.getEspecialidad());
                    psMedico.setInt(2, medico.getMaxCitas());
                    psMedico.setInt(3, determinarDepartamento(medico.getEspecialidad()));
                    psMedico.setInt(4, idPersona);
                    psMedico.executeUpdate();
                }

                String sqlUsuario = "INSERT INTO Usuario (nombreUsuario, clave, rol, id_persona, esActivo) VALUES (?, ?, 'Medico', ?, 1)";
                try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario)) {
                    psUsuario.setString(1, "Med" + medico.getNombre());
                    psUsuario.setString(2, logico.Control.md5("123456"));
                    psUsuario.setInt(3, idPersona);
                    psUsuario.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar medico: " + e.getMessage());
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

    public static boolean eliminarMedico(String idMedico) {
        String sqlSelect = "SELECT id_persona FROM Medico WHERE id = ?";
        String sqlUsuario = "DELETE FROM Usuario WHERE id_persona = ?";
        String sqlMedico = "DELETE FROM Medico WHERE id = ?";
        String sqlPersona = "DELETE FROM Persona WHERE id = ?";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);
            int idPersona = -1;
            try (PreparedStatement psSelect = con.prepareStatement(sqlSelect)) {
                psSelect.setString(1, idMedico);
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        idPersona = rs.getInt(1);
                    }
                }
            }
            if (idPersona != -1) {
                try (PreparedStatement psUser = con.prepareStatement(sqlUsuario)) {
                    psUser.setInt(1, idPersona);
                    psUser.executeUpdate();
                }
                try (PreparedStatement psMedico = con.prepareStatement(sqlMedico)) {
                    psMedico.setString(1, idMedico);
                    psMedico.executeUpdate();
                }
                try (PreparedStatement psPersona = con.prepareStatement(sqlPersona)) {
                    psPersona.setInt(1, idPersona);
                    psPersona.executeUpdate();
                }
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al eliminar medico: " + e.getMessage());
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

    public static boolean actualizarMedico(Medico medico) {
        String sqlPersona = "UPDATE Persona SET nombre = ?, apellido = ?, cedula = ?, sexo = ?, edad = ? WHERE id = (SELECT id_persona FROM Medico WHERE id = ?)";
        String sqlMedico = "UPDATE Medico SET especialidad = ?, maxCitas = ? WHERE id = ?";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement ps1 = con.prepareStatement(sqlPersona);
                    PreparedStatement ps2 = con.prepareStatement(sqlMedico)) {
                ps1.setString(1, medico.getNombre());
                ps1.setString(2, medico.getApellido());
                ps1.setString(3, medico.getCedula());
                ps1.setString(4, medico.getSexo());
                ps1.setInt(5, medico.getEdad());
                ps1.setString(6, medico.getId());
                ps1.executeUpdate();

                ps2.setString(1, medico.getEspecialidad());
                ps2.setInt(2, medico.getMaxCitas());
                ps2.setString(3, medico.getId());
                ps2.executeUpdate();
            }
            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al actualizar medico: " + e.getMessage());
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

    private static int determinarDepartamento(String especialidad) {
        if (especialidad == null)
            return 3; // Medicina General por defecto

        switch (especialidad) {
            case "Cardiologia":
                return 1;
            case "Pediatria":
            case "Cirugia Pediatrica":
                return 2;
            case "Cirugia General":
            case "Cirugia Cardiovascular":
            case "Cirugia Plastica y Reconstructiva":
            case "Cirugia Vascular":
            case "Neurocirugia":
            case "Cirugia Toracica":
            case "Coloproctologia":
                return 4;
            case "Alergologia":
            case "Endocrinologia":
            case "Gastroenterologia":
            case "Geriatria":
            case "Hematologia":
            case "Infectologia":
            case "Medicina Interna":
            case "Nefrologia":
            case "Neumologia":
            case "Reumatologia":
                return 5;
            case "Ginecologia y Obstetricia":
            case "Salud Sexual y Reproductiva":
                return 6;
            case "Neurologia":
            case "Psiquiatria":
                return 7;
            case "Oncologia Medica":
            case "Oncologia Radioterapica":
                return 8;
            default:
                return 3;
        }
    }

    public static Medico buscarMedicoPorIdPersona(String idPersona) {
        String sql = "SELECT m.id, p.cedula, p.nombre, p.apellido, p.edad, p.sexo, p.esActivo, m.especialidad, m.maxCitas "
                +
                "FROM Medico m INNER JOIN Persona p ON m.id_persona = p.id WHERE p.id = ? AND p.esActivo = 1";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(idPersona));
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Medico medico = new Medico(
                            rs.getString("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getInt("edad"),
                            rs.getString("cedula"),
                            rs.getString("sexo"),
                            rs.getString("especialidad"),
                            rs.getInt("maxCitas"));
                    medico.setActivo(rs.getBoolean("esActivo"));
                    return medico;
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar medico por id_persona: " + e.getMessage());
        }
        return null;
    }
}
