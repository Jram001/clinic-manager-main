package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import logico.Paciente;

public class PerfilPaciente extends JDialog {

    private Paciente paciente;
    private DefaultTableModel modelHistorial;

    public PerfilPaciente(String idPaciente) {
        this.paciente = dao.PacienteDAO.buscarPacientePorId(idPaciente);

        setTitle("Ficha del Paciente");
        setModal(true);
        setSize(800, 600);
        setLocationRelativeTo(null);
        getContentPane().setLayout(new BorderLayout(0, 0));
        getContentPane().setBackground(Color.WHITE);

        if (paciente == null) {
            JLabel lblError = new JLabel("Error: Paciente no encontrado.");
            lblError.setHorizontalAlignment(JLabel.CENTER);
            getContentPane().add(lblError, BorderLayout.CENTER);
            return;
        }

        // Header Panel (Aesthetic Header)
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(40, 60, 80));
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(20, 24, 20, 24));

        JLabel lblNombre = new JLabel(paciente.getNombre() + " " + paciente.getApellido());
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblNombre.setAlignmentX(Component.LEFT_ALIGNMENT);
        headerPanel.add(lblNombre);

        headerPanel.add(Box.createVerticalStrut(12));

        JPanel tagsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tagsPanel.setOpaque(false);
        tagsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        tagsPanel.add(crearTag("Cédula: " + paciente.getCedula()));
        tagsPanel.add(Box.createHorizontalStrut(12));
        tagsPanel.add(crearTag("Edad: " + paciente.getEdad() + " años"));
        tagsPanel.add(Box.createHorizontalStrut(12));
        tagsPanel.add(crearTag("Sexo: " + paciente.getSexo()));
        tagsPanel.add(Box.createHorizontalStrut(12));
        tagsPanel.add(crearTag("Sangre: " + paciente.getTipoSangre()));

        headerPanel.add(tagsPanel);
        getContentPane().add(headerPanel, BorderLayout.NORTH);

        // Body Panel (Body details \& History Table)
        JPanel bodyPanel = new JPanel();
        bodyPanel.setBackground(Color.WHITE);
        bodyPanel.setLayout(new BorderLayout(0, 16));
        bodyPanel.setBorder(new EmptyBorder(16, 24, 16, 24));

        JPanel detailsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 24, 0));
        detailsPanel.setOpaque(false);
        detailsPanel.add(crearDetalle("Teléfono", paciente.getTelefono()));
        detailsPanel.add(crearDetalle("Dirección", paciente.getDireccion()));
        detailsPanel.add(crearDetalle("Estatura", String.format("%.2f m", paciente.getEstatura())));
        detailsPanel.add(crearDetalle("Peso", String.format("%.1f kg", paciente.getPeso())));
        bodyPanel.add(detailsPanel, BorderLayout.NORTH);

        JPanel historyContainer = new JPanel(new BorderLayout(0, 8));
        historyContainer.setOpaque(false);
        JLabel lblHistory = new JLabel("Historial de Consultas Médicas");
        lblHistory.setFont(new Font("Segoe UI", Font.BOLD, 16));
        historyContainer.add(lblHistory, BorderLayout.NORTH);

        String[] cols = { "Fecha", "Sintomas", "Diagnostico", "Medico", "Importante" };
        modelHistorial = new DefaultTableModel(cols, 0);
        JTable tableHistorial = new JTable(modelHistorial);
        tableHistorial.setDefaultEditor(Object.class, null);
        tableHistorial.setShowGrid(false);
        tableHistorial.setRowHeight(32);
        tableHistorial.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        tableHistorial.setFont(new Font("Segoe UI", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(tableHistorial);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(SystemColor.control));
        historyContainer.add(scrollPane, BorderLayout.CENTER);

        bodyPanel.add(historyContainer, BorderLayout.CENTER);

        getContentPane().add(bodyPanel, BorderLayout.CENTER);

        cargarHistorial();
    }

    private JPanel crearTag(String text) {
        JPanel tag = new JPanel(new BorderLayout());
        tag.setBackground(new Color(60, 90, 120));
        tag.setBorder(new EmptyBorder(4, 12, 4, 12));
        JLabel label = new JLabel(text);
        label.setForeground(new Color(230, 240, 255));
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        tag.add(label);
        return tag;
    }

    private JPanel crearDetalle(String title, String value) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        JLabel lblTitle = new JLabel(title);
        lblTitle.setForeground(new Color(120, 130, 140));
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        JLabel lblVal = new JLabel(value != null && !value.isEmpty() ? value : "N/A");
        lblVal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        panel.add(lblTitle);
        panel.add(Box.createVerticalStrut(4));
        panel.add(lblVal);
        return panel;
    }

    private void cargarHistorial() {
        if (paciente == null)
            return;
        modelHistorial.setRowCount(0);
        String sql = "SELECT TOP 5 c.fecha, c.sintomas, c.diagnostico, p.nombre, p.apellido, c.esImportante " +
                "FROM Consulta c " +
                "LEFT JOIN Medico m ON c.id_medico = m.id " +
                "LEFT JOIN Persona p ON m.id_persona = p.id " +
                "WHERE c.id_paciente = ? ORDER BY c.fecha DESC";

        try (java.sql.Connection con = logico.ConexionDB.getConexion();
                java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, paciente.getId());
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String fecha = rs.getTimestamp("fecha") != null ? rs.getTimestamp("fecha").toString() : "N/A";
                    String sintomas = rs.getString("sintomas");
                    String diagnostico = rs.getString("diagnostico");
                    String medico = (rs.getString("nombre") != null)
                            ? rs.getString("nombre") + " " + rs.getString("apellido")
                            : "N/A";
                    String importante = rs.getBoolean("esImportante") ? "SI" : "NO";
                    modelHistorial.addRow(new Object[] { fecha, sintomas, diagnostico, medico, importante });
                }
            }
        } catch (java.sql.SQLException e) {
            System.out.println("Error al cargar ficha: " + e.getMessage());
        }
    }
}
