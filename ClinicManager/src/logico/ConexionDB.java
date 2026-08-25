package logico;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    public static Connection getConexion() {
        try {
            Connection con = DriverManager.getConnection(
                    "jdbc:sqlserver://localhost:1433;databaseName=ClinicManager;integratedSecurity=true;encrypt=true;trustServerCertificate=true;");
            System.out.println("Conexión establecida con SQL Server");
            return con;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static void main(String[] args) {
        ConexionDB.getConexion();
    }
}
