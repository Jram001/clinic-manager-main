package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import logico.ConexionDB;

public class PerfilClinico extends JDialog {

    private JPanel contentPane;
    private JTable tablaHistorial;
    private DefaultTableModel modeloHistorial;
    private JTextArea areaDetalles;
    private int idPaciente;

    public PerfilClinico(int idPaciente) {
        this.idPaciente = idPaciente;
        setTitle("Expediente Clínico");
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 700);
        setModal(true);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 250));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Historial Médico");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5);
        contentPane.add(splitPane, BorderLayout.CENTER);

        // Panel superior: Tabla Historial
        JPanel panelSuperior = new JPanel(new BorderLayout());
        panelSuperior.setBorder(new TitledBorder("Consultas Previas (Seleccione una fila)"));

        String[] columnas = { "ID Consulta", "Fecha", "Síntomas", "Diagnóstico" };
        modeloHistorial = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tablaHistorial = new JTable(modeloHistorial);
        tablaHistorial.getColumnModel().getColumn(0).setPreferredWidth(50);
        tablaHistorial.getColumnModel().getColumn(1).setPreferredWidth(90);
        tablaHistorial.getColumnModel().getColumn(2).setPreferredWidth(250);
        tablaHistorial.getColumnModel().getColumn(3).setPreferredWidth(250);
        tablaHistorial.setRowHeight(30);
        tablaHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        tablaHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        tablaHistorial.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tablaHistorial.getSelectedRow();
                if (row != -1) {
                    int idConsulta = (int) modeloHistorial.getValueAt(row, 0);
                    String sintomas = tablaHistorial.getValueAt(row, 2).toString();
                    String diagnostico = tablaHistorial.getValueAt(row, 3).toString();
                    cargarDetalles(idConsulta, sintomas, diagnostico);
                }
            }
        });

        JScrollPane scrollHistorial = new JScrollPane(tablaHistorial);
        panelSuperior.add(scrollHistorial, BorderLayout.CENTER);
        splitPane.setTopComponent(panelSuperior);

        // Panel inferior: Detalles
        JPanel panelInferior = new JPanel(new BorderLayout());
        panelInferior.setBorder(new TitledBorder("Detalles Complementarios de la Consulta"));

        areaDetalles = new JTextArea();
        areaDetalles.setEditable(false);
        areaDetalles.setLineWrap(true);
        areaDetalles.setWrapStyleWord(true);
        areaDetalles.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
        panelInferior.add(scrollDetalles, BorderLayout.CENTER);
        splitPane.setBottomComponent(panelInferior);

        cargarHistorial();
    }

    private void cargarHistorial() {
        modeloHistorial.setRowCount(0);
        String sql = "SELECT id, fecha, sintomas, diagnostico FROM Consulta WHERE id_paciente = ? ORDER BY fecha DESC";
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPaciente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Object[] fila = {
                            rs.getInt("id"),
                            rs.getTimestamp("fecha") != null ? rs.getTimestamp("fecha").toString() : "",
                            rs.getString("sintomas"),
                            rs.getString("diagnostico")
                    };
                    modeloHistorial.addRow(fila);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar historial clínico: " + e.getMessage());
        }
    }

    private void cargarDetalles(int idConsulta, String sintomas, String diagnostico) {
        StringBuilder sb = new StringBuilder();

        sb.append("=== MOTIVO DE CONSULTA ===\n");
        sb.append("Síntomas:\n").append(sintomas).append("\n\n");
        sb.append("Diagnóstico:\n").append(diagnostico).append("\n\n");

        // Bloque 1: Medicamentos + Recetas
        String sqlRecetas = "SELECT m.nombre, dr.instrucciones FROM DetalleReceta dr " +
                "INNER JOIN Receta r ON dr.id_receta = r.id " +
                "INNER JOIN Medicamento m ON dr.id_medicamento = m.id " +
                "WHERE r.id_consulta = ?";

        sb.append("=== RECETARIO Y MEDICAMENTOS ===\n");
        boolean tieneMedicamentos = false;
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sqlRecetas)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tieneMedicamentos = true;
                    sb.append("- ").append(rs.getString("nombre"))
                            .append(": ").append(rs.getString("instrucciones")).append("\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Medicamentos: " + e.getMessage());
        }
        if (!tieneMedicamentos)
            sb.append("No se prescribieron medicamentos.\n");

        sb.append("\n=== EXÁMENES DE LABORATORIO ===\n");
        // Bloque 2: Exámenes de Laboratorio + Resultados
        String sqlExamenes = "SELECT e.nombre, oe.estado, oe.resultado FROM OrdenExamen oe " +
                "INNER JOIN ExamenLaboratorio e ON oe.id_examen = e.id " +
                "WHERE oe.id_consulta = ?";

        boolean tieneExamenes = false;
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sqlExamenes)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tieneExamenes = true;
                    sb.append("- ").append(rs.getString("nombre"))
                            .append(" [Estado: ").append(rs.getString("estado")).append("]\n");
                    String resultado = rs.getString("resultado");
                    if (resultado != null && !resultado.isEmpty()) {
                        sb.append("   Resultado: ").append(resultado).append("\n");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Exámenes: " + e.getMessage());
        }
        if (!tieneExamenes)
            sb.append("No se ordenaron exámenes.\n");

        sb.append("\n=== VACUNACIONES ===\n");
        // Bloque 3: Vacunas + Lotes/Dosis
        String sqlVacunas = "SELECT v.nombre, a.numeroDosis, a.lote FROM AplicacionVacuna a " +
                "INNER JOIN Vacuna v ON a.id_vacuna = v.id " +
                "INNER JOIN Consulta c ON a.id_paciente = c.id_paciente AND a.fechaAplicacion = c.fecha " +
                "WHERE c.id = ?";

        boolean tieneVacunas = false;
        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sqlVacunas)) {
            ps.setInt(1, idConsulta);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tieneVacunas = true;
                    Object objDosis = rs.getObject("numeroDosis");
                    String dosisStr = objDosis != null ? objDosis.toString() : "N/A";
                    String loteStr = rs.getString("lote");
                    if (loteStr == null || loteStr.trim().isEmpty()) {
                        loteStr = "N/A";
                    }

                    sb.append("- ").append(rs.getString("nombre"))
                            .append(" (Dosis: ").append(dosisStr)
                            .append(", Lote: ").append(loteStr).append(")\n");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error Vacunas: " + e.getMessage());
        }
        if (!tieneVacunas)
            sb.append("No se aplicaron vacunas en esta consulta.\n");

        areaDetalles.setText(sb.toString());
        areaDetalles.setCaretPosition(0);
    }
}
