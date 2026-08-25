package logico;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBInfo {
    public static void main(String[] args) {
        try (Connection con = ConexionDB.getConexion()) {
            DatabaseMetaData metaData = con.getMetaData();

            String[] tables = { "Consulta", "AplicacionVacuna", "PacienteVacuna", "Vacuna" };

            for (String tableName : tables) {
                System.out.println("Table: " + tableName);
                try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                    while (columns.next()) {
                        System.out.println("  - " + columns.getString("COLUMN_NAME") + " ("
                                + columns.getString("TYPE_NAME") + ")");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
