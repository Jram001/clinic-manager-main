package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;
import logico.ConexionDB;

public class ReportesDAO {

    public static DefaultTableModel obtenerOcupacionAgenda() {
        String sql = "SELECT m.id AS ID, p.nombre + ' ' + p.apellido AS Médico, p.cedula AS Cédula, " +
                "m.especialidad AS Especialidad, d.nombre AS Departamento, m.maxCitas AS Cupo, " +
                "COUNT(c.id) AS [Citas Hoy], " +
                "CASE WHEN m.maxCitas > 0 THEN (COUNT(c.id) * 100.0 / m.maxCitas) ELSE 0 END AS [Ocupación %] " +
                "FROM Medico m " +
                "INNER JOIN Persona p ON m.id_persona = p.id " +
                "LEFT JOIN Departamento d ON m.id_departamento = d.id " +
                "LEFT JOIN Cita c ON c.id_medico = m.id AND CAST(c.fecha AS DATE) = CAST(GETDATE() AS DATE) " +
                "WHERE p.esActivo = 1 " +
                "GROUP BY m.id, p.nombre, p.apellido, p.cedula, m.especialidad, d.nombre, m.maxCitas";

        return executeQueryToModel(sql);
    }

    public static DefaultTableModel obtenerFacturacionMes() {
        String sql = "SELECT s.nombre AS Seguro, s.rnc AS RNC, " +
                "CAST(s.porcentajeCobertura AS VARCHAR) + '%' AS Cobertura, " +
                "COUNT(f.id) AS Facturas, " +
                "SUM(CASE WHEN LOWER(f.estado) = 'pagada' THEN 1 ELSE 0 END) AS Pagadas, " +
                "SUM(CASE WHEN LOWER(f.estado) = 'pendiente' THEN 1 ELSE 0 END) AS Pendientes, " +
                "SUM(CASE WHEN LOWER(f.estado) = 'anulada' THEN 1 ELSE 0 END) AS Anuladas, " +
                "SUM(f.subtotal) AS Subtotal, " +
                "SUM(f.descuentoSeguro) AS Descuento, " +
                "SUM(f.total) AS [Total Cobrado] " +
                "FROM SeguroMedico s " +
                "LEFT JOIN Factura f ON f.id_seguro = s.id AND MONTH(f.fecha) = MONTH(GETDATE()) AND YEAR(f.fecha) = YEAR(GETDATE()) "
                +
                "GROUP BY s.nombre, s.rnc, s.porcentajeCobertura";

        return executeQueryToModel(sql);
    }

    public static DefaultTableModel obtenerVigilanciaEpidemiologica() {
        String sql = "SELECT e.id AS ID, e.nombre AS Enfermedad, e.gravedad AS Gravedad, " +
                "COUNT(c.id) AS [Total Casos], " +
                "SUM(CASE WHEN c.esImportante = 1 THEN 1 ELSE 0 END) AS [Casos Importantes], " +
                "MAX(c.fecha) AS [Último Caso], " +
                "COUNT(DISTINCT c.id_medico) AS Médicos " +
                "FROM EnfermedadBajoVigilancia e " +
                "LEFT JOIN Consulta c ON c.id_enfermedad = e.id " +
                "GROUP BY e.id, e.nombre, e.gravedad";

        return executeQueryToModel(sql);
    }

    private static DefaultTableModel executeQueryToModel(String sql) {
        DefaultTableModel model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                model.addColumn(metaData.getColumnName(i));
            }

            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int i = 1; i <= columnCount; i++) {
                    vector.add(rs.getObject(i));
                }
                model.addRow(vector);
            }

        } catch (SQLException e) {
            System.err.println("Error ejecutando reporte SQL: " + e.getMessage());
        }

        return model;
    }
}
