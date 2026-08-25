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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Clinica;
import logico.Control;
import logico.Medico;
import logico.Usuario;

public class DashboardMedico extends JFrame {

	Clinica instancia = Clinica.getInstancia();
	Control control = Control.getInstance();
	private JPanel contentPane;
	private JTable tablaCitas;
	DefaultTableModel modelCitas;
	LocalDate hoy = LocalDate.now();
	Date fechaInicial = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());
	Usuario usuario = Control.getLoggedUsuario();
	Medico medicoActual = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel(new FlatLightLaf());
					DashboardMedico frame = new DashboardMedico();
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
	public DashboardMedico() {
		setTitle("Clinic Manager - Medico");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1392, 822);
		contentPane = new JPanel();

		setExtendedState(getExtendedState());
		setResizable(true);

		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		if (usuario != null && usuario.getLinkId() != null && !usuario.getLinkId().trim().isEmpty()) {
			String linkIdMatch = usuario.getLinkId().trim();
			medicoActual = dao.MedicoDAO.buscarMedicoPorIdPersona(linkIdMatch);
		}
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

		// Welcome Título
		String welcomeText = "Bienvenido, Doctor";
		if (medicoActual != null) {
			String prefijo = "Dr.";
			String saludo = "Bienvenido";

			if (medicoActual.getSexo() != null && medicoActual.getSexo().equalsIgnoreCase("Femenino")) {
				prefijo = "Dra.";
				saludo = "Bienvenida";
			}

			welcomeText = saludo + ", " + prefijo + " " + medicoActual.getNombre() + " " + medicoActual.getApellido()
					+ " (" + medicoActual.getEspecialidad() + ")";
		}

		centerContainer.add(Box.createVerticalStrut(20));

		JLabel lblBienvenido = new JLabel(welcomeText);
		lblBienvenido.setFont(new Font("Segoe UI", Font.BOLD, 28));
		lblBienvenido.setForeground(new Color(40, 40, 40));
		lblBienvenido.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerContainer.add(lblBienvenido);

		// Spacer
		centerContainer.add(Box.createVerticalStrut(15));

		JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
		searchPanel.setBackground(Color.WHITE);
		JLabel lblBuscarCedula = new JLabel("Buscar Cédula:");
		lblBuscarCedula.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		javax.swing.JTextField txtBuscarCedula = new javax.swing.JTextField(15);
		txtBuscarCedula.setFont(new Font("Segoe UI", Font.PLAIN, 16));
		JButton btnAbrirExpediente = new JButton("Abrir Expediente");
		btnAbrirExpediente.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		searchPanel.add(lblBuscarCedula);
		searchPanel.add(txtBuscarCedula);
		searchPanel.add(btnAbrirExpediente);
		centerContainer.add(searchPanel);

		btnAbrirExpediente.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				String cedula = txtBuscarCedula.getText().trim();
				if (cedula.isEmpty()) {
					JOptionPane.showMessageDialog(DashboardMedico.this, "Por favor ingrese una cédula", "Alerta",
							JOptionPane.WARNING_MESSAGE);
					return;
				}
				String sql = "SELECT pac.id FROM Persona p INNER JOIN Paciente pac ON p.id = pac.id_persona WHERE p.cedula = ?";
				try (java.sql.Connection con = logico.ConexionDB.getConexion();
						java.sql.PreparedStatement ps = con.prepareStatement(sql)) {
					ps.setString(1, cedula);
					try (java.sql.ResultSet rs = ps.executeQuery()) {
						if (rs.next()) {
							int idExtraido = rs.getInt(1);
							PerfilClinico expediente = new PerfilClinico(idExtraido);
							expediente.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(DashboardMedico.this, "La cédula no está registrada.",
									"Alerta", JOptionPane.WARNING_MESSAGE);
						}
					}
				} catch (java.sql.SQLException ex) {
					ex.printStackTrace();
				}
			}
		});

		centerContainer.add(Box.createVerticalStrut(15));

		// Superior
		JPanel gridPanel = new JPanel();
		gridPanel.setOpaque(false);
		Dimension gridTopSize = new Dimension(900, 60);
		gridPanel.setPreferredSize(gridTopSize);
		gridPanel.setMaximumSize(new Dimension(1200, 60));
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
		gridPanelBottom.setPreferredSize(new Dimension(1200, 420));
		gridPanelBottom.setMaximumSize(new Dimension(1200, 420));
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
		dateChooser.setPreferredSize(new Dimension(140, 24));
		headerPanel.add(dateChooser);

		headerPanel.add(Box.createHorizontalStrut(10));
		JLabel lblBuscar = new JLabel("Buscar:");
		headerPanel.add(lblBuscar);
		javax.swing.JTextField searchField = new javax.swing.JTextField(15);
		headerPanel.add(searchField);

		String[] columnasCitas = { "ID", "Nombre", "Apellido", "Edad", "Motivo" };
		modelCitas = new DefaultTableModel(columnasCitas, 0);
		tablaCitas = new JTable(modelCitas);
		tablaCitas.setDefaultEditor(Object.class, null);
		cargarTablaCitas(hoy);

		panelInferiorIzquierdo.add(headerPanel, BorderLayout.NORTH);

		javax.swing.table.TableRowSorter<DefaultTableModel> sorter = new javax.swing.table.TableRowSorter<>(modelCitas);
		tablaCitas.setRowSorter(sorter);

		searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			Runnable update = () -> {
				String text = searchField.getText();
				if (text.trim().isEmpty()) {
					sorter.setRowFilter(null);
				} else {
					sorter.setRowFilter(
							javax.swing.RowFilter.regexFilter("(?i)" + java.util.regex.Pattern.quote(text), 1, 2));
				}
			};

			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				update.run();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				update.run();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				update.run();
			}
		});

		tablaCitas.setShowGrid(false);

		JScrollPane scrollCitas = new JScrollPane(tablaCitas);
		// ajustar tamano preferido del scroll para que ocupe bien la tarjeta
		scrollCitas.setPreferredSize(new Dimension(860, 300));
		panelInferiorIzquierdo.add(scrollCitas, BorderLayout.CENTER);

		gridPanelBottom.add(panelInferiorIzquierdo);

		// Agrego el grid inferior
		centerContainer.add(gridPanelBottom);

		// Botones
		JPanel buttonBar = new JPanel(new BorderLayout());
		buttonBar.setBackground(Color.WHITE);
		buttonBar.setBorder(new EmptyBorder(8, 0, 12, 0));

		JPanel leftButtonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
		leftButtonsPanel.setBackground(Color.WHITE);
		JButton btnCancelar = new JButton("Cancelar");
		JButton btnPosponer = new JButton("Posponer");
		btnCancelar.setEnabled(false);
		btnPosponer.setEnabled(false);

		leftButtonsPanel.add(btnCancelar);
		leftButtonsPanel.add(btnPosponer);
		buttonBar.add(leftButtonsPanel, BorderLayout.WEST);

		JPanel rightButtonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
		rightButtonsPanel.setBackground(Color.WHITE);
		JButton btnVerHistorial = new JButton("Ver Historial");
		JButton btnAtender = new JButton("Atender");
		btnVerHistorial.setEnabled(false);
		btnAtender.setEnabled(false);

		btnAtender.setBackground(new Color(40, 167, 69)); // Verde éxito
		btnAtender.setForeground(Color.WHITE);
		btnAtender.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnAtender.setOpaque(true);
		btnAtender.setBorderPainted(false);

		rightButtonsPanel.add(btnVerHistorial);
		rightButtonsPanel.add(btnAtender);
		buttonBar.add(rightButtonsPanel, BorderLayout.EAST);

		contentPane.add(buttonBar, BorderLayout.SOUTH);

		tablaCitas.getSelectionModel().addListSelectionListener(new javax.swing.event.ListSelectionListener() {
			public void valueChanged(javax.swing.event.ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					boolean valid = tablaCitas.getSelectedRow() >= 0;
					btnCancelar.setEnabled(valid);
					btnPosponer.setEnabled(valid);
					btnVerHistorial.setEnabled(valid);
					btnAtender.setEnabled(valid);
				}
			}
		});

		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idCol = 0;
				if (citaTablaSeleccionado != -1) {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idCol);
					String id = String.valueOf(idTexto);
					Cita cita = dao.CitaDAO.buscarCitaPorId(id);
					if (cita != null) {
						dao.CitaDAO.cancelarCita(id);
					}
					java.util.Date date = dateChooser.getDate();
					if (date != null) {
						java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDate();
						cargarTablaCitas(localDate);
					}
					lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
					lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
					JOptionPane.showMessageDialog(DashboardMedico.this,
							"Cita cancelada para " + (cita != null ? cita.getNombrePaciente() : id), "Alerta",
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});

		btnPosponer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idCol = 0;
				if (citaTablaSeleccionado != -1) {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idCol);
					String id = String.valueOf(idTexto);
					PosponerCita pantallaPosponerCita = new PosponerCita(id);
					pantallaPosponerCita.setLocationRelativeTo(DashboardMedico.this);
					pantallaPosponerCita.setVisible(true);

					java.util.Date date = dateChooser.getDate();
					if (date != null) {
						java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDate();
						cargarTablaCitas(localDate);
					}
					lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
					lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
				}
			}
		});

		btnAtender.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idColId = 0;
				if (citaTablaSeleccionado != -1) {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idColId);
					String citaId = String.valueOf(idTexto);
					Cita c = dao.CitaDAO.buscarCitaPorId(citaId);
					if (c != null) {
						logico.Paciente p = dao.PacienteDAO.buscarPacientePorId(c.getId_paciente());
						String cedula = p != null ? p.getCedula() : "";
						boolean existe = p != null;

						if (!existe) {
							AgregarPaciente pantallaAgregarPaciente = new AgregarPaciente(citaId, cedula);
							pantallaAgregarPaciente.setLocationRelativeTo(DashboardMedico.this);
							pantallaAgregarPaciente.setModal(true);
							pantallaAgregarPaciente.setVisible(true);
							String pacienteId = pantallaAgregarPaciente.getCreatedPacienteId();
							AgregarConsulta pantallaAgregarConsulta = new AgregarConsulta(citaId, pacienteId);
							pantallaAgregarConsulta.setLocationRelativeTo(DashboardMedico.this);
							pantallaAgregarConsulta.setVisible(true);
						} else {
							String pacienteId = p.getId();
							AgregarConsulta pantallaAgregarConsulta = new AgregarConsulta(citaId, pacienteId);
							pantallaAgregarConsulta.setLocationRelativeTo(DashboardMedico.this);
							pantallaAgregarConsulta.setVisible(true);
						}
						java.util.Date date = dateChooser.getDate();
						if (date != null) {
							java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
									.toLocalDate();
							cargarTablaCitas(localDate);
							lbCitasNum.setText(String.valueOf(contarNumCitasHoy()));
							lbCitasGeneralsNum.setText(String.valueOf(contarNumCitasFuturas()));
						}
					}
				}
			}
		});

		btnVerHistorial.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int citaTablaSeleccionado = tablaCitas.getSelectedRow();
				int idColId = 0;
				if (citaTablaSeleccionado != -1) {
					int modelRow = tablaCitas.convertRowIndexToModel(citaTablaSeleccionado);
					Object idTexto = tablaCitas.getModel().getValueAt(modelRow, idColId);
					String citaId = String.valueOf(idTexto);
					Cita c = dao.CitaDAO.buscarCitaPorId(citaId);
					if (c != null) {
						logico.Paciente p = dao.PacienteDAO.buscarPacientePorId(c.getId_paciente());
						if (p != null) {
							PerfilPaciente perfil = new PerfilPaciente(p.getId());
							perfil.setVisible(true);
						} else {
							JOptionPane.showMessageDialog(DashboardMedico.this,
									"No se encontro el perfil del paciente.", "Error", JOptionPane.ERROR_MESSAGE);
						}
					}
				}
			}
		});

		setLocationRelativeTo(null);

		// ShutdownHook removido para evitar corrupcion de datos concurrentes con
		// Main.java
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
		modelCitas.setRowCount(0);
		ArrayList<Cita> citas = dao.CitaDAO.listarCitas();

		if (citas == null)
			return;

		for (Cita c : citas) {
			String id = c.getId();
			String nombre = c.getNombrePaciente();
			String apellido = c.getApellidoPaciente();
			logico.Paciente pac = dao.PacienteDAO.buscarPacientePorId(c.getId_paciente());
			String edad = pac != null ? String.valueOf(pac.getEdad()) : "N/A";
			String motivo = "Consulta General";

			boolean mismoMedico = false;
			if (c.getId_medico() != null && medicoActual != null && medicoActual.getId() != null) {
				mismoMedico = c.getId_medico().equalsIgnoreCase(medicoActual.getId());
			}

			if (c.isEsActivo() && c.getFecha().equals(fecha) && mismoMedico) {
				modelCitas.addRow(new Object[] { id, nombre, apellido, edad, motivo });
			}
		}
	}

	private int contarNumCitasHoy() {
		int contador = 0;

		for (Cita c : dao.CitaDAO.listarCitas()) {
			if (c.isEsActivo() && c.getFecha().equals(hoy) && c.getId_medico() != null && medicoActual != null
					&& c.getId_medico().equalsIgnoreCase(medicoActual.getId())) {
				contador++;
			}
		}
		return contador;
	}

	public int contarNumCitasFuturas() {
		int contador = 0;

		for (Cita c : dao.CitaDAO.listarCitas()) {
			if (c.isEsActivo() && c.getFecha().isAfter(hoy) && c.getId_medico() != null && medicoActual != null
					&& c.getId_medico().equalsIgnoreCase(medicoActual.getId())) {
				contador++;
			}
		}

		return contador;
	}
}