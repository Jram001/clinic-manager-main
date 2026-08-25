package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import logico.ConexionDB;
import logico.Secretaria;

import java.util.ArrayList;

public class SecretariaDAO {

    public static ArrayList<Secretaria> listarSecretarias() {
        ArrayList<Secretaria> secretarias = new ArrayList<>();
        String sql = "SELECT s.id, p.cedula, p.nombre, p.apellido, p.edad, p.sexo, p.esActivo, s.turno, s.salario, s.extensionTelefonica, s.id_persona "
                +
                "FROM Secretaria s INNER JOIN Persona p ON s.id_persona = p.id";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Secretaria sec = new Secretaria(
                        rs.getString("id"),
                        rs.getString("cedula"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        "", // telefono
                        "", // direccion
                        rs.getString("sexo"),
                        rs.getInt("edad"),
                        rs.getString("turno"),
                        rs.getDouble("salario"),
                        rs.getString("extensionTelefonica"));
                sec.setActivo(rs.getBoolean("esActivo"));
                secretarias.add(sec);
            }
        } catch (SQLException e) {
            System.out.println("Error al listar secretarias: " + e.getMessage());
        }
        return secretarias;
    }

    public static boolean registrarSecretaria(Secretaria secretaria) {
        String sqlPersona = "INSERT INTO Persona (cedula, nombre, apellido, edad, sexo, esActivo) VALUES (?, ?, ?, ?, ?, 1)";
        String sqlSecretaria = "INSERT INTO Secretaria (turno, salario, extensionTelefonica, id_persona) VALUES (?, ?, ?, ?)";
        String sqlUsuario = "INSERT INTO Usuario (nombreUsuario, clave, rol, id_persona, esActivo) VALUES (?, ?, 'Secretaria', ?, 1)";
        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            try (PreparedStatement psPersona = con.prepareStatement(sqlPersona,
                    java.sql.Statement.RETURN_GENERATED_KEYS)) {
                psPersona.setString(1, secretaria.getCedula());
                psPersona.setString(2, secretaria.getNombre());
                psPersona.setString(3, secretaria.getApellido());
                psPersona.setInt(4, secretaria.getEdad());
                psPersona.setString(5, secretaria.getSexo());
                psPersona.executeUpdate();

                ResultSet rs = psPersona.getGeneratedKeys();
                int idPersona = 0;
                if (rs.next()) {
                    idPersona = rs.getInt(1);
                }

                try (PreparedStatement psSecretaria = con.prepareStatement(sqlSecretaria)) {
                    psSecretaria.setString(1, secretaria.getTurno());
                    psSecretaria.setDouble(2, secretaria.getSalario());
                    psSecretaria.setString(3, secretaria.getExtensionTelefonica());
                    psSecretaria.setInt(4, idPersona);
                    psSecretaria.executeUpdate();
                }

                try (PreparedStatement psUsuario = con.prepareStatement(sqlUsuario)) {
                    psUsuario.setString(1, "Sec" + secretaria.getNombre());
                    psUsuario.setString(2, logico.Control.md5("123456"));
                    psUsuario.setInt(3, idPersona);
                    psUsuario.executeUpdate();
                }
            }

            con.commit();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al registrar secretaria: " + e.getMessage());
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

    public static boolean eliminarSecretaria(String id) {
        String sql = "UPDATE Persona SET esActivo = 0 WHERE id = (SELECT id_persona FROM Secretaria WHERE id = ?)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar secretaria: " + e.getMessage());
            return false;
        }
    }

    public static boolean activarSecretaria(String id) {
        String sql = "UPDATE Persona SET esActivo = 1 WHERE id = (SELECT id_persona FROM Secretaria WHERE id = ?)";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(id));
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al activar secretaria: " + e.getMessage());
            return false;
        }
    }

    public static boolean borrarSecretariaDefinitivo(String id) {
        String querySelectIdPersona = "SELECT id_persona FROM Secretaria WHERE id = ?";
        String deleteUsuario = "DELETE FROM Usuario WHERE id_persona = ?";
        String deleteSecretaria = "DELETE FROM Secretaria WHERE id = ?";
        String deletePersona = "DELETE FROM Persona WHERE id = ?";

        Connection con = null;
        try {
            con = ConexionDB.getConexion();
            con.setAutoCommit(false);

            int idPersona = -1;
            try (PreparedStatement ps1 = con.prepareStatement(querySelectIdPersona)) {
                ps1.setInt(1, Integer.parseInt(id));
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    idPersona = rs.getInt("id_persona");
                }
            }

            if (idPersona != -1) {
                try (PreparedStatement psU = con.prepareStatement(deleteUsuario)) {
                    psU.setInt(1, idPersona);
                    psU.executeUpdate();
                }
                try (PreparedStatement psS = con.prepareStatement(deleteSecretaria)) {
                    psS.setInt(1, Integer.parseInt(id));
                    psS.executeUpdate();
                }
                try (PreparedStatement psP = con.prepareStatement(deletePersona)) {
                    psP.setInt(1, idPersona);
                    psP.executeUpdate();
                }
            }

            con.commit();
            return true;

        } catch (SQLException e) {
            System.out.println("Error al eliminar secretaria: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.out.println("Error rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.out.println("Error cerrando conexion: " + e.getMessage());
                }
            }
        }
    }
}
