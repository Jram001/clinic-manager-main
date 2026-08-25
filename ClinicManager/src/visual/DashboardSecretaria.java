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
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Clinica;
import logico.Control;
import logico.Medico;

public class DashboardSecretaria extends JFrame {

	Clinica instancia = Clinica.getInstancia();
	private JPanel contentPane;
	private JTable tablaCitas;
	DefaultTableModel modelCitas;
	LocalDate hoy = LocalDate.now();
	Date fechaInicial = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					DashboardSecretaria frame = new DashboardSecretaria();
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
	public DashboardSecretaria() {
		setTitle("Clinic Manager - Secretaria");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1392, 822);
		contentPane = new JPanel();

		setExtendedState(getExtendedState());
		setResizable(true);
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
		JLabel labelAvatar = new JLabel((avatarIcon));
		labelAvatar.setBorder(new EmptyBorder(8, 12, 8, 12));
		JPanel avatarPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 8));
		avatarPanel.setOpaque(false);

		JLabel lblUserName = new JLabel(Control.getLoggedUsuario().getNombreUsuario());
		lblUserName.setForeground(Color.WHITE);
		lblUserName.setFont(new Font("Segoe UI", Font.BOLD, 15));
		avatarPanel.add(lblUserName);
		avatarPanel.add(labelAvatar);

		// Botón de Salir movido a la barra superior
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
		Dimension gridTopSize = new Dimension(900, 60); // Reducido a la mitad (120 -> 60)
		gridPanel.setPreferredSize(gridTopSize);
		gridPanel.setMaximumSize(new Dimension(1200, 80));
		gridPanel.setLayout(new GridLayout(1, 0, 8, 8));
		gridPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		// KPI 1
		JPanel panelPacienteKPI = new JPanel();
		panelPacienteKPI.setBackground(SystemColor.inactiveCaptionBorder);
		panelPacienteKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelPacienteKPI.setBorder(new EmptyBorder(12, 12, 12, 12)); // padding interno

		JLabel lblTitleCitas = new JLabel("Citas Hoy:");
		lblTitleCitas.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelPacienteKPI.add(lblTitleCitas);

		String citasHoy = String.valueOf(contarNumCitasHoy());
		JLabel lbCitasNum = new JLabel(citasHoy);
		lbCitasNum.setForeground(SystemColor.textHighlight);
		lbCitasNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
		panelPacienteKPI.add(lbCitasNum);

		gridPanel.add(panelPacienteKPI);

		// KPI 2
		JPanel panelDoctoresKPI = new JPanel();
		panelDoctoresKPI.setBackground(SystemColor.inactiveCaptionBorder);
		panelDoctoresKPI.setLayout(new FlowLayout(FlowLayout.CENTER));
		panelDoctoresKPI.setBorder(new EmptyBorder(12, 12, 12, 12));

		JLabel lblCitasGeneral = new JLabel("Citas Futuras:");
		lblCitasGeneral.setFont(new Font("Segoe UI", Font.PLAIN, 18));
		panelDoctoresKPI.add(lblCitasGeneral);

		String citasFuturas = String.valueOf(contarNumCitasFuturas());
		JLabel lbCitasGeneralsNum = new JLabel(citasFuturas);
		lbCitasGeneralsNum.setForeground(SystemColor.textHighlight);
		lbCitasGeneralsNum.setFont(new Font("Segoe UI", Font.BOLD, 24));
		panelDoctoresKPI.add(lbCitasGeneralsNum);

		gridPanel.add(panelDoctoresKPI);

		// Agrego el grid superior
		centerContainer.add(gridPanel);

		// Spacer del Medio
		centerContainer.add(Box.createVerticalStrut(24));

		// Grid Inferior
		JPanel gridPanelBottom = new JPanel();
		gridPanelBottom.setOpaque(false);
		gridPanelBottom.setPreferredSize(new Dimension(1200, 500));
		gridPanelBottom.setMaximumSize(new Dimension(1200, 500));
		gridPanelBottom.setLayout(new GridLayout(1, 0, 8, 8));
		gridPanelBottom.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Panel 1
		JPanel panelInferiorIzquierdo = new JPanel(new BorderLayout());
		panelInferiorIzquierdo.setBackground(Color.WHITE);

		JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
		headerPanel.setBackground(Color.WHITE);

		JLabel lbCitas = new JLabel("Citas");
		lbCitas.setBorder(new EmptyBorder(0, 0, 0, 8)); // margen a la derecha
		headerPanel.add(lbCitas);

		JDateChooser dateChooser = new JDateChooser(fechaInicial);
		dateChooser.setDateFormatString("dd/MM/yyyy");
		dateChooser.addPropertyChangeListener("date", new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent evt) {
				java.util.Date date = dateChooser.getDate();
				if (date != null) {
					java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
							.toLocalDate();
					cargarTablaCitas(localDate);
				}
			}
		});
		dateChooser.setPreferredSize(new Dimension(120, 24));
		headerPanel.add(dateChooser);

		headerPanel.add(Box.createHorizontalStrut(10));

		JLabel lblBuscar = new JLabel("Buscar Paciente:");
		headerPanel.add(lblBuscar);
		JTextField searchField = new JTextField(15);
		headerPanel.add(searchField);

		JLabel lblMedico = new JLabel("Médico:");
		headerPanel.add(lblMedico);
		JComboBox<String> doctorCombo = new JComboBox<String>();
		doctorCombo.addItem("Todos los médicos");
		ArrayList<Medico> medicos = dao.MedicoDAO.obtenerMedicosParaConsultas();
		for (Medico m : medicos) {
			doctorCombo.addItem(m.getNombre() + " " + m.getApellido());
		}
		headerPanel.add(doctorCombo);

		headerPanel.add(Box.createHorizontalStrut(10));

		JButton btnAgregar = new JButton("Agregar Cita");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CrearCita pantallaCrearCita = new CrearCita();
				pantallaCrearCita.setLocationRelativeTo(DashboardSecretaria.this);
				pantallaCrearCita.setVisible(true);

				java.util.Date date = dateChooser.getDate();
				if (date != null) {
					java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
							.toLocalDate();
					lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
					lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
					cargarTablaCitas(localDate);
				}
			}
		});
		headerPanel.add(btnAgregar);

		String[] columnasDoctores = { "ID", "Fecha", "Paciente", "Médico" };
		modelCitas = new DefaultTableModel(columnasDoctores, 0);

		tablaCitas = new JTable(modelCitas);
		tablaCitas.setDefaultEditor(Object.class, null);
		tablaCitas.setShowGrid(false);

		TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(modelCitas);
		tablaCitas.setRowSorter(sorter);

		Runnable updateFilter = () -> {
			String text = searchField.getText().trim();
			String doc = (String) doctorCombo.getSelectedItem();

			java.util.List<RowFilter<Object, Object>> filters = new java.util.ArrayList<>();
			if (text.length() > 0) {
				filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 2));
			}
			if (doc != null && !doc.equals("Todos los médicos")) {
				filters.add(RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(doc), 7));
			}
			if (filters.isEmpty()) {
				sorter.setRowFilter(null);
			} else {
				sorter.setRowFilter(RowFilter.andFilter(filters));
			}
		};

		searchField.getDocument().addDocumentListener(new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				updateFilter.run();
			}

			public void removeUpdate(DocumentEvent e) {
				updateFilter.run();
			}

			public void changedUpdate(DocumentEvent e) {
				updateFilter.run();
			}
		});

		doctorCombo.addActionListener(e -> updateFilter.run());

		cargarTablaCitas(hoy);

		panelInferiorIzquierdo.add(headerPanel, BorderLayout.NORTH);

		JScrollPane scrollCitas = new JScrollPane(tablaCitas);
		scrollCitas.setPreferredSize(new Dimension(860, 350));
		panelInferiorIzquierdo.add(scrollCitas, BorderLayout.CENTER);

		// Botones Cancelar y Posponer (antes abajo, movidos ligeramente y agregando
		// ListSelectionListener)
		JPanel buttonBar = new JPanel(new FlowLayout(FlowLayout.CENTER, 8, 12));
		buttonBar.setBackground(Color.WHITE);
		buttonBar.setBorder(new EmptyBorder(8, 0, 12, 0));

		JButton btnCancelar = new JButton("Cancelar cita");
		btnCancelar.setEnabled(false);
		JButton btnPosponer = new JButton("Posponer cita");
		btnPosponer.setEnabled(false);

		tablaCitas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					boolean canEdit = tablaCitas.getSelectedRow() >= 0;
					btnCancelar.setEnabled(canEdit);
					btnPosponer.setEnabled(canEdit);
				}
			}
		});

		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idCol = 0;

				if (citaTablaSeleccionado == -1) {
					JOptionPane.showMessageDialog(DashboardSecretaria.this, "No hay Nada Seleccionado", "Alerta",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idCol);
					String id = String.valueOf(idTexto);
					Cita cita = dao.CitaDAO.buscarCitaPorId(id);
					dao.CitaDAO.cancelarCita(id);
					java.util.Date date = dateChooser.getDate();
					java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
							.toLocalDate();
					lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
					cargarTablaCitas(localDate);
					lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
					JOptionPane.showMessageDialog(DashboardSecretaria.this,
							"Cita Cancelada para: " + cita.getNombrePaciente(),
							"Alerta", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		btnPosponer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idCol = 0;

				if (citaTablaSeleccionado == -1) {
					JOptionPane.showMessageDialog(DashboardSecretaria.this, "No hay Nada Seleccionado", "Alerta",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idCol);
					String id = String.valueOf(idTexto);
					PosponerCita pantallaPosponerCita = new PosponerCita(id);
					pantallaPosponerCita.setLocationRelativeTo(DashboardSecretaria.this);
					pantallaPosponerCita.setVisible(true);

					java.util.Date date = dateChooser.getDate();
					java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
							.toLocalDate();
					lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
					lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
					cargarTablaCitas(localDate);
				}

			}
		});

		buttonBar.add(btnCancelar);
		buttonBar.add(btnPosponer);

		panelInferiorIzquierdo.add(buttonBar, BorderLayout.SOUTH);

		gridPanelBottom.add(panelInferiorIzquierdo);

		// Agrego el grid inferior
		centerContainer.add(gridPanelBottom);

		setLocationRelativeTo(null);
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

	private void cargarTablaCitas(LocalDate fecha) {
		DefaultTableModel newModel = dao.CitaDAO.obtenerAgendaSecretaria(fecha);
		java.util.Vector<String> columns = new java.util.Vector<>();
		for (int i = 0; i < newModel.getColumnCount(); i++) {
			columns.add(newModel.getColumnName(i));
		}
		modelCitas.setDataVector(newModel.getDataVector(), columns);
	}

	private int contarNumCitasHoy() {
		int contador = 0;

		for (Cita c : dao.CitaDAO.listarCitas()) {
			if (c.isEsActivo() && c.getFecha().equals(hoy)) {
				contador++;
			}
		}
		return contador;
	}

	public int contarNumCitasFuturas() {
		int contador = 0;

		for (Cita c : dao.CitaDAO.listarCitas()) {
			if (c.isEsActivo() && c.getFecha().isAfter(hoy)) {
				contador++;
			}
		}

		return contador;
	}
}