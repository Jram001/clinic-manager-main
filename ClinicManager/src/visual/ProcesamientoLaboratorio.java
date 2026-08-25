package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import dao.ExamenLaboratorioDAO;
import logico.ConexionDB;

public class ProcesamientoLaboratorio extends JFrame {

    private JPanel contentPane;
    private JTable tableOrdenes;
    private DefaultTableModel tableModel;

    public ProcesamientoLaboratorio() {
        setTitle("Procesamiento de Laboratorio");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 900, 600);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(245, 245, 250));
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(new BorderLayout(0, 10));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("Órdenes de Laboratorio Pendientes");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        contentPane.add(lblTitulo, BorderLayout.NORTH);

        // Table Implementation
        String[] columnNames = { "ID Orden", "Fecha", "Paciente", "Examen" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableOrdenes = new JTable(tableModel);
        tableOrdenes.setRowHeight(25);
        tableOrdenes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableOrdenes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(tableOrdenes);
        contentPane.add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 250));

        JButton btnIngresarResultado = new JButton("Ingresar Resultado");
        btnIngresarResultado.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnIngresarResultado.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableOrdenes.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(ProcesamientoLaboratorio.this,
                            "Seleccione una orden de la lista para ingresar su resultado.", "Advertencia",
                            JOptionPane.WARNING_MESSAGE);
                    return;
                }

                int idOrden = Integer.parseInt(tableModel.getValueAt(selectedRow, 0).toString());

                javax.swing.JTextArea areaTexto = new javax.swing.JTextArea(10, 40);
                areaTexto.setLineWrap(true);
                areaTexto.setWrapStyleWord(true);
                JScrollPane scroll = new JScrollPane(areaTexto);

                int opcion = JOptionPane.showConfirmDialog(ProcesamientoLaboratorio.this, scroll,
                        "Valores del resultado - Orden " + idOrden, JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE);

                if (opcion == JOptionPane.OK_OPTION) {
                    String resultado = areaTexto.getText().trim();
                    if (resultado != null && !resultado.isEmpty()) {
                        boolean exito = ExamenLaboratorioDAO.registrarResultadoExamen(idOrden, resultado);
                        if (exito) {
                            JOptionPane.showMessageDialog(ProcesamientoLaboratorio.this,
                                    "Resultado registrado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                            cargarOrdenesPendientes();
                        } else {
                            JOptionPane.showMessageDialog(ProcesamientoLaboratorio.this,
                                    "Ocurrió un error al registrar el resultado.", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        buttonPanel.add(btnIngresarResultado);

        JButton btnActualizar = new JButton("Actualizar Lista");
        btnActualizar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cargarOrdenesPendientes();
            }
        });
        buttonPanel.add(btnActualizar);

        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        // Load data
        cargarOrdenesPendientes();
    }

    private void cargarOrdenesPendientes() {
        tableModel.setRowCount(0);
        String sql = "SELECT o.id, o.fechaOrden, p.nombre, p.apellido, e.nombre AS examen " +
                "FROM OrdenExamen o " +
                "INNER JOIN Consulta c ON o.id_consulta = c.id " +
                "INNER JOIN Paciente pac ON c.id_paciente = pac.id " +
                "INNER JOIN Persona p ON pac.id_persona = p.id " +
                "INNER JOIN ExamenLaboratorio e ON o.id_examen = e.id " +
                "WHERE o.estado = 'Pendiente'";

        try (Connection con = ConexionDB.getConexion();
                PreparedStatement ps = con.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getDate("fechaOrden").toString(),
                        rs.getString("nombre") + " " + rs.getString("apellido"),
                        rs.getString("examen")
                };
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            System.out.println("Error al cargar ordenes pendientes: " + e.getMessage());
        }
    }
}
