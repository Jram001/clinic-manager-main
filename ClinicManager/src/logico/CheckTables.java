package logico;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CheckTables {
    public static void main(String[] args) {
        try (Connection con = ConexionDB.getConexion()) {
            DatabaseMetaData metaData = con.getMetaData();

            String[] tables = { "Receta", "DetalleReceta", "OrdenExamen", "Factura", "DetalleFactura" };

            for (String tableName : tables) {
                System.out.println("===" + tableName + "===");
                try (ResultSet columns = metaData.getColumns(null, null, tableName, null)) {
                    while (columns.next()) {
                        System.out.println(columns.getString("COLUMN_NAME"));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
