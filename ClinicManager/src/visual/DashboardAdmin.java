package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Clinica;
import logico.Control;
import logico.Medico;
import logico.Paciente;

public class DashboardAdmin extends JFrame {

    Clinica instancia = Clinica.getInstancia();
    private JPanel contentPane;
    private JTable tablaDoctores;
    private JTable tablaPacientes;
    JLabel lbDoctoresNum;
    JLabel lbPacientesNum;
    JLabel lbVacunaNum;
    JLabel lblEnfermedadesNum;
    DefaultTableModel modelDoctores;
    DefaultTableModel modelPacientes;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    DashboardAdmin frame = new DashboardAdmin();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public DashboardAdmin() {
        setTitle("Clinic Manager - Admin");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1392, 822);
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        setExtendedState(getExtendedState());
        setResizable(true);

        JMenu mnAgregar = new JMenu("Agregar");
        menuBar.add(mnAgregar);

        JMenuItem mnItemMedico = new JMenuItem("Medico");
        mnItemMedico.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarMedico pantallaAgregarMedico = new AgregarMedico();
                pantallaAgregarMedico.setLocationRelativeTo(DashboardAdmin.this);
                pantallaAgregarMedico.setVisible(true);
                lbDoctoresNum.setText(String.valueOf(contarNumMedicos()));
                cargarTablaDoctores();
            }
        });
        mnAgregar.add(mnItemMedico);

        JMenuItem mnItemPaciente = new JMenuItem("Paciente");
        mnItemPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarPaciente pantallaAgregarPaciente = new AgregarPaciente(null, null);
                pantallaAgregarPaciente.setLocationRelativeTo(DashboardAdmin.this);
                pantallaAgregarPaciente.setVisible(true);
                if (lbPacientesNum != null) {
                    lbPacientesNum.setText(String.valueOf(contarNumPacientes()));
                }
                cargarTablaPacientes();
            }
        });
        mnAgregar.add(mnItemPaciente);

        JMenuItem mnItemVacuna = new JMenuItem("Vacuna");
        mnItemVacuna.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarVacuna pantallaAgregarVacuna = new AgregarVacuna();
                pantallaAgregarVacuna.setLocationRelativeTo(DashboardAdmin.this);
                pantallaAgregarVacuna.setVisible(true);
                lbVacunaNum.setText(String.valueOf(contarNumVacunas()));
            }
        });
        mnAgregar.add(mnItemVacuna);

        JMenuItem mnItemEnfermedad = new JMenuItem("Enfermedad Vigilada");
        mnItemEnfermedad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarEnfermedad pantallaAgregarEnfermedad = new AgregarEnfermedad();
                pantallaAgregarEnfermedad.setLocationRelativeTo(DashboardAdmin.this);
                pantallaAgregarEnfermedad.setVisible(true);
                lblEnfermedadesNum.setText(String.valueOf(contarNumEnfermedades()));
            }
        });
        mnAgregar.add(mnItemEnfermedad);

        JMenuItem mnItemSecretaria = new JMenuItem("Secretaria");
        mnItemSecretaria.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                AgregarSecretaria pantallaAgregarSecretaria = new AgregarSecretaria();
                pantallaAgregarSecretaria.setLocationRelativeTo(DashboardAdmin.this);
                pantallaAgregarSecretaria.setVisible(true);
            }
        });
        mnAgregar.add(mnItemSecretaria);

        JMenuItem mnItemDepartamento = new JMenuItem("Departamento");
        mnItemDepartamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarDepartamento dialog = new AgregarDepartamento(null);
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnAgregar.add(mnItemDepartamento);

        JMenuItem mnItemSeguro = new JMenuItem("Seguro Medico");
        mnItemSeguro.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarSeguro dialog = new AgregarSeguro(null);
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnAgregar.add(mnItemSeguro);

        JMenuItem mnItemMedicamento = new JMenuItem("Medicamento");
        mnItemMedicamento.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarMedicamento dialog = new AgregarMedicamento(null);
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnAgregar.add(mnItemMedicamento);

        JMenuItem mnItemExamen = new JMenuItem("Examen de Laboratorio");
        mnItemExamen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                AgregarExamen dialog = new AgregarExamen(null);
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnAgregar.add(mnItemExamen);

        JMenu mnMenuVer = new JMenu("Ver");
        menuBar.add(mnMenuVer);

        JMenuItem mnMenuVacunas = new JMenuItem("Vacunas");
        mnMenuVacunas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaVacunas pantallaListaVacunas = new ListaVacunas();
                pantallaListaVacunas.setLocationRelativeTo(DashboardAdmin.this);
                pantallaListaVacunas.setVisible(true);
                lbVacunaNum.setText(String.valueOf(contarNumVacunas()));
            }
        });
        mnMenuVer.add(mnMenuVacunas);

        JMenuItem mnItemEnfermedades = new JMenuItem("Enfermedades Vigiladas");
        mnItemEnfermedades.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaEnfermedades pantallaListaEnfermedades = new ListaEnfermedades();
                pantallaListaEnfermedades.setLocationRelativeTo(DashboardAdmin.this);
                pantallaListaEnfermedades.setVisible(true);
                lblEnfermedadesNum.setText(String.valueOf(contarNumEnfermedades()));
            }
        });
        mnMenuVer.add(mnItemEnfermedades);

        JMenuItem mnSecretariaVer = new JMenuItem("Secretaria");
        mnSecretariaVer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                ListSecretaria pantallaListSecretaria = new ListSecretaria();
                pantallaListSecretaria.setLocationRelativeTo(DashboardAdmin.this);
                pantallaListSecretaria.setVisible(true);
            }
        });
        mnMenuVer.add(mnSecretariaVer);

        JMenuItem mnVerDepartamentos = new JMenuItem("Departamentos");
        mnVerDepartamentos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaDepartamentos dialog = new ListaDepartamentos();
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnMenuVer.add(mnVerDepartamentos);

        JMenuItem mnVerSeguros = new JMenuItem("Seguros Medicos");
        mnVerSeguros.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaSeguros dialog = new ListaSeguros();
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnMenuVer.add(mnVerSeguros);

        JMenuItem mnVerMedicamentos = new JMenuItem("Medicamentos");
        mnVerMedicamentos.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaMedicamentos dialog = new ListaMedicamentos();
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnMenuVer.add(mnVerMedicamentos);

        JMenuItem mnVerExamenes = new JMenuItem("Examenes de Laboratorio");
        mnVerExamenes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ListaExamenes dialog = new ListaExamenes();
                dialog.setLocationRelativeTo(DashboardAdmin.this);
                dialog.setVisible(true);
            }
        });
        mnMenuVer.add(mnVerExamenes);

        JMenu mnMenuStats = new JMenu("Stats");
        menuBar.add(mnMenuStats);

        JMenuItem mntmNewMenuItem_1 = new JMenuItem("Reportes");
        mntmNewMenuItem_1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                Reportes pantallaReportes = new Reportes();
                pantallaReportes.setLocationRelativeTo(DashboardAdmin.this);
                pantallaReportes.setVisible(true);
            }
        });
        mnMenuStats.add(mntmNewMenuItem_1);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);

        JPanel navbar = new JPanel(new BorderLayout());
        navbar.setBackground(SystemColor.textHighlight);
        navbar.setPreferredSize(new Dimension(0, 72));
        navbar.setBorder(new EmptyBorder(0, 0, 0, 0));
        contentPane.add(navbar, BorderLayout.NORTH);

        // Logo
        ImageIcon logoIcon = loadAndScaleIcon("/visual/logo.png", 152, 34);
        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setBorder(new EmptyBorder(8, 12, 8, 12));
        navbar.add(logoLabel, BorderLayout.WEST);

        // Avatar
        ImageIcon avatarIcon = loadAndScaleIcon("/visual/avatar.png", 48, 48);
        JLabel labelAvatar = new JLabel(avatarIcon);
        labelAvatar.setBorder(new EmptyBorder(8, 12, 8, 12));
        JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
        avatarPanel.setOpaque(false);
        JLabel lblUserName = new JLabel(Control.getLoggedUsuario().getNombreUsuario());
        lblUserName.setForeground(Color.WHITE);
        lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 15));
        avatarPanel.add(lblUserName);
        avatarPanel.add(labelAvatar);

        JButton btnSalirTop = new JButton("Salir");
        btnSalirTop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Control.logout();
                Login pantallaLogin = new Login();
                pantallaLogin.setVisible(true);
                dispose();
            }
        });
        avatarPanel.add(btnSalirTop);

        navbar.add(avatarPanel, BorderLayout.EAST);

        // Contenedor central
        JPanel centerContainer = new JPanel();
        centerContainer.setBackground(Color.WHITE);
        centerContainer.setLayout(new BoxLayout(centerContainer, BoxLayout.Y_AXIS));
        contentPane.add(centerContainer, BorderLayout.CENTER);

        // Spacer
        centerContainer.add(Box.createVerticalStrut(40));

        // Superior
        JPanel gridPanel = new JPanel();
        gridPanel.setOpaque(false);
        Dimension gridTopSize = new Dimension(900, 60); // Reducido a la mitad
        gridPanel.setPreferredSize(gridTopSize);
        gridPanel.setMaximumSize(new Dimension(1200, 80));
        gridPanel.setLayout(new GridLayout(1, 0, 8, 8));
        gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // KPI 1
        JPanel panelPacienteKPI = new JPanel();
        panelPacienteKPI.setBackground(SystemColor.inactiveCaptionBorder);
        panelPacienteKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelPacienteKPI.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitlePacientes = new JLabel("Pacientes:");
        lblTitlePacientes.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelPacienteKPI.add(lblTitlePacientes);

        lbPacientesNum = new JLabel(String.valueOf(contarNumPacientes()));
        lbPacientesNum.setForeground(SystemColor.textHighlight);
        lbPacientesNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelPacienteKPI.add(lbPacientesNum);

        gridPanel.add(panelPacienteKPI);

        // KPI 2
        JPanel panelDoctoresKPI = new JPanel();
        panelDoctoresKPI.setBackground(SystemColor.inactiveCaptionBorder);
        panelDoctoresKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelDoctoresKPI.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitleDoctores = new JLabel("Doctores:");
        lblTitleDoctores.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelDoctoresKPI.add(lblTitleDoctores);

        lbDoctoresNum = new JLabel(String.valueOf(contarNumMedicos()));
        lbDoctoresNum.setForeground(SystemColor.textHighlight);
        lbDoctoresNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelDoctoresKPI.add(lbDoctoresNum);

        gridPanel.add(panelDoctoresKPI);

        // KPI 3
        JPanel panelEnfermedadesKPI = new JPanel();
        panelEnfermedadesKPI.setBackground(SystemColor.inactiveCaptionBorder);
        panelEnfermedadesKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelEnfermedadesKPI.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lblTitleEnfermedades = new JLabel("Enfermedades:");
        lblTitleEnfermedades.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelEnfermedadesKPI.add(lblTitleEnfermedades);

        lblEnfermedadesNum = new JLabel(String.valueOf(contarNumEnfermedades()));
        lblEnfermedadesNum.setForeground(SystemColor.textHighlight);
        lblEnfermedadesNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelEnfermedadesKPI.add(lblEnfermedadesNum);

        gridPanel.add(panelEnfermedadesKPI);

        // KPI 4
        JPanel panelVacunasKPI = new JPanel();
        panelVacunasKPI.setBackground(SystemColor.inactiveCaptionBorder);
        panelVacunasKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelVacunasKPI.setBorder(new EmptyBorder(12, 12, 12, 12));

        JLabel lbTitleVacunas = new JLabel("Vacunas:");
        lbTitleVacunas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        panelVacunasKPI.add(lbTitleVacunas);

        lbVacunaNum = new JLabel(String.valueOf(contarNumVacunas()));
        lbVacunaNum.setForeground(SystemColor.textHighlight);
        lbVacunaNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelVacunasKPI.add(lbVacunaNum);

        gridPanel.add(panelVacunasKPI);

        // Agrego el grid superior
        centerContainer.add(gridPanel);

        // Spacer del Medio
        centerContainer.add(Box.createVerticalStrut(24));

        // Grid Inferior
        JPanel gridPanelBottom = new JPanel();
        gridPanelBottom.setOpaque(false);
        gridPanelBottom.setPreferredSize(new Dimension(1200, 480));
        gridPanelBottom.setMaximumSize(new Dimension(1200, 480));
        gridPanelBottom.setLayout(new GridLayout(1, 0, 8, 8));
        gridPanelBottom.setAlignmentX(Component.CENTER_ALIGNMENT);

        // ================ PANEL DOCTORES ================
        JPanel panelInferiorIzquierdo = new JPanel(new BorderLayout());
        panelInferiorIzquierdo.setBackground(Color.WHITE);

        JPanel doctoresHeader = new JPanel(new BorderLayout());
        doctoresHeader.setBackground(Color.WHITE);
        JLabel lbDoctores = new JLabel("Doctores", SwingConstants.CENTER);
        lbDoctores.setBorder(new EmptyBorder(6, 0, 6, 0));
        doctoresHeader.add(lbDoctores, BorderLayout.NORTH);

        JPanel doctoresSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        doctoresSearchPanel.setBackground(Color.WHITE);
        doctoresSearchPanel.add(new JLabel("Buscar Nombre/Apellido:"));
        JTextField searchDoctores = new JTextField(15);
        doctoresSearchPanel.add(searchDoctores);
        doctoresHeader.add(doctoresSearchPanel, BorderLayout.SOUTH);

        panelInferiorIzquierdo.add(doctoresHeader, BorderLayout.NORTH);

        String[] columnasDoctores = { "ID", "Nombre", "Apellido", "Especialidad" };
        modelDoctores = new DefaultTableModel(columnasDoctores, 0);
        cargarTablaDoctores();
        tablaDoctores = new JTable(modelDoctores);
        tablaDoctores.setDefaultEditor(Object.class, null);
        tablaDoctores.setShowGrid(false);

        tablaDoctores.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaDoctores.getColumnModel().getColumn(0).setPreferredWidth(40);

        TableRowSorter<DefaultTableModel> sorterDoctores = new TableRowSorter<>(modelDoctores);
        tablaDoctores.setRowSorter(sorterDoctores);

        searchDoctores.getDocument().addDocumentListener(new DocumentListener() {
            Runnable update = () -> {
                String q = searchDoctores.getText();
                if (q.isBlank())
                    sorterDoctores.setRowFilter(null);
                else
                    sorterDoctores.setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q), 1, 2));
            };

            public void insertUpdate(DocumentEvent e) {
                update.run();
            }

            public void removeUpdate(DocumentEvent e) {
                update.run();
            }

            public void changedUpdate(DocumentEvent e) {
                update.run();
            }
        });

        JScrollPane scrollDoctores = new JScrollPane(tablaDoctores);
        panelInferiorIzquierdo.add(scrollDoctores, BorderLayout.CENTER);

        JPanel doctoresControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        doctoresControls.setBackground(Color.WHITE);
        JButton btnEditarDoctor = new JButton("Editar Doctor");
        btnEditarDoctor.setEnabled(false);
        JButton btnEliminarDoctor = new JButton("Eliminar Doctor");
        btnEliminarDoctor.setEnabled(false);
        doctoresControls.add(btnEditarDoctor);
        doctoresControls.add(btnEliminarDoctor);
        panelInferiorIzquierdo.add(doctoresControls, BorderLayout.SOUTH);

        tablaDoctores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean canEdit = tablaDoctores.getSelectedRow() >= 0;
                    btnEditarDoctor.setEnabled(canEdit);
                    btnEliminarDoctor.setEnabled(canEdit);
                }
            }
        });

        btnEditarDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tablaDoctores.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tablaDoctores.convertRowIndexToModel(row);
                    String id = String.valueOf(tablaDoctores.getModel().getValueAt(modelRow, 0));
                    EditarMedico pantallaEditarMedico = new EditarMedico(id);
                    pantallaEditarMedico.setLocationRelativeTo(DashboardAdmin.this);
                    pantallaEditarMedico.setVisible(true);
                    lbDoctoresNum.setText(String.valueOf(contarNumMedicos()));
                    cargarTablaDoctores();
                }
            }
        });

        btnEliminarDoctor.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tablaDoctores.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tablaDoctores.convertRowIndexToModel(row);
                    String id = String.valueOf(tablaDoctores.getModel().getValueAt(modelRow, 0));
                    dao.MedicoDAO.eliminarMedico(id);
                    lbDoctoresNum.setText(String.valueOf(contarNumMedicos()));
                    cargarTablaDoctores();
                }
            }
        });

        gridPanelBottom.add(panelInferiorIzquierdo);

        // ================ PANEL PACIENTES ================
        JPanel panelInferiorDerecho = new JPanel(new BorderLayout());
        panelInferiorDerecho.setBackground(Color.WHITE);

        JPanel pacientesHeader = new JPanel(new BorderLayout());
        pacientesHeader.setBackground(Color.WHITE);
        JLabel lbPacientes = new JLabel("Pacientes", SwingConstants.CENTER);
        lbPacientes.setBorder(new EmptyBorder(6, 0, 6, 0));
        pacientesHeader.add(lbPacientes, BorderLayout.NORTH);

        JPanel pacientesSearchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        pacientesSearchPanel.setBackground(Color.WHITE);
        pacientesSearchPanel.add(new JLabel("Buscar Nombre/Tel:"));
        JTextField searchPacientes = new JTextField(15);
        pacientesSearchPanel.add(searchPacientes);
        pacientesHeader.add(pacientesSearchPanel, BorderLayout.SOUTH);

        panelInferiorDerecho.add(pacientesHeader, BorderLayout.NORTH);

        String[] columnasPacientes = { "ID", "Nombre", "Edad", "Telefono", "Direccion" };
        modelPacientes = new DefaultTableModel(columnasPacientes, 0);
        tablaPacientes = new JTable(modelPacientes);
        tablaPacientes.setDefaultEditor(Object.class, null);
        tablaPacientes.setShowGrid(false);

        tablaPacientes.getColumnModel().getColumn(0).setMaxWidth(40);
        tablaPacientes.getColumnModel().getColumn(0).setPreferredWidth(40);

        TableRowSorter<DefaultTableModel> sorterPacientes = new TableRowSorter<>(modelPacientes);
        tablaPacientes.setRowSorter(sorterPacientes);

        searchPacientes.getDocument().addDocumentListener(new DocumentListener() {
            Runnable update = () -> {
                String q = searchPacientes.getText();
                if (q.isBlank())
                    sorterPacientes.setRowFilter(null);
                // Filtrar por Nombre (col 1) o Telefono (col 3)
                else
                    sorterPacientes
                            .setRowFilter(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(q), 1, 3));
            };

            public void insertUpdate(DocumentEvent e) {
                update.run();
            }

            public void removeUpdate(DocumentEvent e) {
                update.run();
            }

            public void changedUpdate(DocumentEvent e) {
                update.run();
            }
        });

        JScrollPane scrollPacientes = new JScrollPane(tablaPacientes);
        panelInferiorDerecho.add(scrollPacientes, BorderLayout.CENTER);
        cargarTablaPacientes();

        JPanel pacientesControls = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        pacientesControls.setBackground(Color.WHITE);
        JButton btnEditarPaciente = new JButton("Editar Paciente");
        btnEditarPaciente.setEnabled(false);
        JButton btnEliminarPaciente = new JButton("Eliminar Paciente");
        btnEliminarPaciente.setEnabled(false);
        pacientesControls.add(btnEditarPaciente);
        pacientesControls.add(btnEliminarPaciente);
        panelInferiorDerecho.add(pacientesControls, BorderLayout.SOUTH);

        tablaPacientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    boolean canEdit = tablaPacientes.getSelectedRow() >= 0;
                    btnEditarPaciente.setEnabled(canEdit);
                    btnEliminarPaciente.setEnabled(canEdit);
                }
            }
        });

        btnEditarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tablaPacientes.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tablaPacientes.convertRowIndexToModel(row);
                    String id = String.valueOf(tablaPacientes.getModel().getValueAt(modelRow, 0));
                    EditarPaciente edit = new EditarPaciente(id);
                    edit.setLocationRelativeTo(DashboardAdmin.this);
                    edit.setVisible(true);
                    cargarTablaPacientes();
                }
            }
        });

        btnEliminarPaciente.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int row = tablaPacientes.getSelectedRow();
                if (row >= 0) {
                    int modelRow = tablaPacientes.convertRowIndexToModel(row);
                    String id = String.valueOf(tablaPacientes.getModel().getValueAt(modelRow, 0));
                    dao.PacienteDAO.eliminarPaciente(id);
                    if (lbPacientesNum != null) {
                        lbPacientesNum.setText(String.valueOf(contarNumPacientes()));
                    }
                    cargarTablaPacientes();
                }
            }
        });

        gridPanelBottom.add(panelInferiorDerecho);

        centerContainer.add(gridPanelBottom);

        setLocationRelativeTo(null);
    }

    private void cargarTablaPacientes() {
        modelPacientes.setRowCount(0);
        ArrayList<Paciente> pacientes = dao.PacienteDAO.listarPacientes();
        if (pacientes == null)
            return;

        for (Paciente p : pacientes) {
            String id = p.getId();
            String nombre = p.getNombre();
            String apellido = p.getApellido();
            String telefono = p.getTelefono();
            String direccion = p.getDireccion();

            if (p.isActivo()) {
                modelPacientes.addRow(new Object[] { id, nombre + " " + apellido, p.getEdad(), telefono, direccion });
            }
        }
    }

    private ImageIcon loadAndScaleIcon(String resourcePath, int width, int height) {
        java.net.URL url = getClass().getResource(resourcePath);
        if (url == null)
            return null;
        try {
            BufferedImage img = ImageIO.read(url);
            Image scaled = getScaledImage(img, width, height);
            return new ImageIcon(scaled);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Image getScaledImage(BufferedImage src, int w, int h) {
        BufferedImage resized = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resized.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return resized;
    }

    private void cargarTablaDoctores() {
        modelDoctores.setRowCount(0);
        ArrayList<Medico> medicos = dao.MedicoDAO.listarMedicos();

        if (medicos == null)
            return;

        for (Medico m : medicos) {
            String id = m.getId();
            String nombre = m.getNombre();
            String apellido = m.getApellido();
            String especialidad = m.getEspecialidad();

            if (m.isActivo()) {
                modelDoctores.addRow(new Object[] { id, nombre, apellido, especialidad });
            }
        }
    }

    private int contarNumPacientes() {
        return dao.PacienteDAO.listarPacientes().size();
    }

    private int contarNumMedicos() {
        int contador = 0;

        for (Medico m : dao.MedicoDAO.listarMedicos()) {
            if (m.isActivo()) {
                contador++;
            }
        }
        return contador;
    }

    private int contarNumVacunas() {
        return dao.VacunaDAO.listarVacunas().size();
    }

    private int contarNumEnfermedades() {
        return dao.EnfermedadDAO.listarEnfermedades().size();
    }

}