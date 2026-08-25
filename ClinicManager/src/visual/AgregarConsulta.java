package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Cita;
import logico.Clinica;
import logico.Consulta;
import logico.DetalleFactura;
import logico.DetalleReceta;
import logico.EnfermedadBajoVigilancia;
import logico.ExamenLaboratorio;
import logico.Factura;
import logico.Medicamento;
import logico.Medico;
import logico.Paciente;
import logico.Receta;
import logico.SeguroMedico;
import logico.Vacuna;

public class AgregarConsulta extends JDialog {

	Clinica instancia = Clinica.getInstancia();
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JTextArea textSintomas;
	private JTextArea textDiagnostico;
	private final Map<JCheckBox, Vacuna> vacunaChecks = new LinkedHashMap<>();
	private final Map<JCheckBox, Medicamento> medicamentoChecks = new LinkedHashMap<>();
	private final Map<JCheckBox, ExamenLaboratorio> examenChecks = new LinkedHashMap<>();
	JComboBox<EnfermedadBajoVigilancia> comboBoxEnfermedades;
	JCheckBox chckbxNewCheckBox_1;
	DefaultTableModel modelHistorial;
	private static String citaId;
	private static String idPaciente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			AgregarConsulta dialog = new AgregarConsulta(citaId, idPaciente);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AgregarConsulta(String citaId, String idPaciente) {
		setModal(true);
		setTitle("Consulta");
		setBounds(100, 100, 962, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		Paciente paciente = dao.PacienteDAO.buscarPacientePorId(idPaciente);
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_112449224634200");
			panel.setLayout(null);
			{
				JLabel lbNombre = new JLabel("Nombre");
				lbNombre.setBounds(23, 28, 56, 16);
				panel.add(lbNombre);
			}
			{
				textField = new JTextField();
				textField.setEditable(false);
				textField.setText(paciente.getNombre() + " " + paciente.getApellido());
				textField.setBounds(23, 45, 285, 22);
				panel.add(textField);
				textField.setColumns(10);
			}
			{
				JLabel lblSintomas = new JLabel("Sintomas");
				lblSintomas.setBounds(23, 80, 56, 16);
				panel.add(lblSintomas);
			}
			{
				textSintomas = new JTextArea();
				textSintomas.setLineWrap(true);
				textSintomas.setWrapStyleWord(true);
				JScrollPane scrollSintomas = new JScrollPane(textSintomas);
				scrollSintomas.setBounds(23, 99, 892, 32);
				panel.add(scrollSintomas);
			}
			{
				JLabel lbDiagnostico = new JLabel("Diagnostico");
				lbDiagnostico.setBounds(23, 134, 96, 16);
				panel.add(lbDiagnostico);
			}
			{
				textDiagnostico = new JTextArea();
				textDiagnostico.setLineWrap(true);
				textDiagnostico.setWrapStyleWord(true);
				JScrollPane scrollDiagnostico = new JScrollPane(textDiagnostico);
				scrollDiagnostico.setBounds(23, 152, 892, 32);
				panel.add(scrollDiagnostico);
			}
			{
				JLabel lbEnfermedades = new JLabel("Enfermedades Bajo Vigilancia");
				lbEnfermedades.setBounds(23, 187, 180, 16);
				panel.add(lbEnfermedades);
			}
			{
				comboBoxEnfermedades = new JComboBox<>();
				cargarComboEnfermedades();
				comboBoxEnfermedades.setBounds(23, 211, 180, 22);
				panel.add(comboBoxEnfermedades);
			}
			{
				JLabel lbVacunas = new JLabel("Vacunas");
				lbVacunas.setBounds(23, 246, 56, 16);
				panel.add(lbVacunas);

			}
			{
				chckbxNewCheckBox_1 = new JCheckBox("Importante");
				chckbxNewCheckBox_1.setBounds(216, 210, 92, 25);
				panel.add(chckbxNewCheckBox_1);

				JPanel vaccinesPanel = new JPanel();
				vaccinesPanel.setLayout(new BoxLayout(vaccinesPanel, BoxLayout.Y_AXIS));
				vaccinesPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
				vaccinesPanel.setPreferredSize(new Dimension(250, 300));

				JScrollPane scrollVacunas = new JScrollPane(vaccinesPanel);
				scrollVacunas.setBounds(23, 270, 285, 61);
				panel.add(scrollVacunas);

				ArrayList<Vacuna> catalogoVacunas = dao.VacunaDAO.listarVacunas();
				int y = 700;
				int x = 120;
				int espaciado = 32;

				if (catalogoVacunas == null || catalogoVacunas.isEmpty()) {
					JCheckBox checkBox = new JCheckBox("No hay Vacunas");
					checkBox.setEnabled(false);
					checkBox.setBounds(x, y, 300, 25);
					vaccinesPanel.add(checkBox);
				} else {
					for (Vacuna v : catalogoVacunas) {
						if (v == null || !v.isEsActivo())
							continue;

						JCheckBox checkBox = new JCheckBox(v.getNombre());
						boolean aplicadaPorPaciente = false;
						if (paciente != null && paciente.getVacunas() != null) {

							for (Vacuna pv : paciente.getVacunas()) {
								if (pv.getId().equalsIgnoreCase(v.getId()) && pv.isAplicada()) {
									aplicadaPorPaciente = true;
								}
							}
						}
						checkBox.setBounds(x, y, 300, 25);
						vacunaChecks.put(checkBox, v);

						if (aplicadaPorPaciente) {
							checkBox.setEnabled(false);
							checkBox.setSelected(true);
						}

						vaccinesPanel.add(checkBox);

						y += espaciado;
					}

				}

			}

			{
				JLabel lbMedicamentos = new JLabel("Medicamentos");
				lbMedicamentos.setBounds(23, 340, 150, 16);
				panel.add(lbMedicamentos);

				JPanel medsPanel = new JPanel();
				medsPanel.setLayout(new BoxLayout(medsPanel, BoxLayout.Y_AXIS));
				medsPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
				JScrollPane scrollMeds = new JScrollPane(medsPanel);
				scrollMeds.setBounds(23, 360, 285, 61);
				panel.add(scrollMeds);

				ArrayList<Medicamento> medicamentos = dao.MedicamentoDAO.listarMedicamentos();
				if (medicamentos != null) {
					for (Medicamento m : medicamentos) {
						if (m != null && m.isEsActivo()) {
							JCheckBox cb = new JCheckBox(m.getNombre());
							medsPanel.add(cb);
							medicamentoChecks.put(cb, m);
						}
					}
				}
			}

			{
				JLabel lbExamenes = new JLabel("Exámenes de Laboratorio");
				lbExamenes.setBounds(23, 430, 200, 16);
				panel.add(lbExamenes);

				JPanel examsPanel = new JPanel();
				examsPanel.setLayout(new BoxLayout(examsPanel, BoxLayout.Y_AXIS));
				examsPanel.setBorder(new EmptyBorder(4, 4, 4, 4));
				JScrollPane scrollExams = new JScrollPane(examsPanel);
				scrollExams.setBounds(23, 450, 285, 61);
				panel.add(scrollExams);

				ArrayList<ExamenLaboratorio> examenes = dao.ExamenLaboratorioDAO.listarExamenes();
				if (examenes != null) {
					for (ExamenLaboratorio e : examenes) {
						if (e != null && e.isEsActivo()) {
							JCheckBox cb = new JCheckBox(e.getNombre());
							examsPanel.add(cb);
							examenChecks.put(cb, e);
						}
					}
				}
			}

			panel.setLayout(null);

			JButton btnDetalesPacientes = new JButton("Expediente Clínico");
			btnDetalesPacientes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PerfilClinico perfil = new PerfilClinico(Integer.parseInt(idPaciente));
					perfil.setVisible(true);
				}
			});
			btnDetalesPacientes.setBounds(715, 306, 200, 25);
			panel.add(btnDetalesPacientes);
		}

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Terminar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String sintomas = textSintomas.getText().trim();
						String diagnostico = textDiagnostico.getText().trim();
						EnfermedadBajoVigilancia enfermedad = (EnfermedadBajoVigilancia) comboBoxEnfermedades
								.getSelectedItem();
						if (enfermedad != null
								&& (enfermedad.getId() == null || "Ninguno".equalsIgnoreCase(enfermedad.getNombre()))) {
							enfermedad = null;
						}
						boolean check = chckbxNewCheckBox_1.isEnabled();
						if (enfermedad != null)
							check = true;
						Cita cita = dao.CitaDAO.buscarCitaPorId(citaId);
						Medico medico = cita != null ? dao.MedicoDAO.buscarMedicoPorId(cita.getId_medico()) : null;

						if (sintomas.isEmpty() || diagnostico.isEmpty() || diagnostico == null) {
							JOptionPane.showMessageDialog(AgregarConsulta.this, "No hay Suministradores Creados",
									"Alerta", JOptionPane.ERROR_MESSAGE);
						} else {
							Consulta consulta = instancia.agregarConsulta(paciente, medico, sintomas, diagnostico,
									enfermedad, check);
							paciente.agregarConsulta(consulta);

							java.util.HashMap<Integer, String[]> vacunasInfo = new java.util.HashMap<>();
							for (Map.Entry<JCheckBox, Vacuna> entry : vacunaChecks.entrySet()) {
								if (entry.getKey().isSelected()) {
									Vacuna v = entry.getValue();
									int idVacuna = Integer.parseInt(v.getId());
									String nombreVacuna = v.getNombre();

									String lote = JOptionPane.showInputDialog(AgregarConsulta.this,
											"Ingrese el número de lote para la vacuna: " + nombreVacuna);
									String dosis = JOptionPane.showInputDialog(AgregarConsulta.this,
											"Ingrese el número de dosis (ej. 1, 2) para: " + nombreVacuna);

									if (lote != null && !lote.trim().isEmpty() && dosis != null
											&& !dosis.trim().isEmpty()) {
										vacunasInfo.put(idVacuna, new String[] { lote, dosis });
									}
								}
							}

							java.util.HashMap<Integer, String> medicamentosInfo = new java.util.HashMap<>();
							for (Map.Entry<JCheckBox, Medicamento> entry : medicamentoChecks.entrySet()) {
								if (entry.getKey().isSelected()) {
									Medicamento m = entry.getValue();
									int idMedicamento = Integer.parseInt(m.getId());
									String indicaciones = JOptionPane.showInputDialog(AgregarConsulta.this,
											"Ingrese la dosis y frecuencia para: " + m.getNombre());
									if (indicaciones != null && !indicaciones.trim().isEmpty()) {
										medicamentosInfo.put(idMedicamento, indicaciones);
									}
								}
							}

							ArrayList<Integer> examenesIds = new ArrayList<>();
							for (Map.Entry<JCheckBox, ExamenLaboratorio> entry : examenChecks.entrySet()) {
								if (entry.getKey().isSelected()) {
									ExamenLaboratorio ex = entry.getValue();
									examenesIds.add(Integer.parseInt(ex.getId()));
								}
							}

							if (dao.ConsultaDAO.registrarConsulta(consulta, vacunasInfo, medicamentosInfo,
									examenesIds)) {

								dao.CitaDAO.cancelarCita(citaId);

								for (Map.Entry<JCheckBox, Vacuna> entry : vacunaChecks.entrySet()) {
									JCheckBox checkbox = entry.getKey();
									if (checkbox.isSelected()) {
										Vacuna v = entry.getValue();
										Vacuna copia = new Vacuna(v.getId(), v.getNombre(), v.getFabricante(),
												v.getDosis(),
												v.getDescripcion());
										copia.setAplicada(true);

										if (!instancia.verificarVacunaRepetida(paciente, v.getId())) {
											paciente.agregarVacuna(copia);
										}

									}
								}
								// --- Crear Receta con medicamentos disponibles ---
								java.util.ArrayList<Medicamento> meds = instancia.getMedicamentos();
								if (meds != null && !meds.isEmpty()) {
									Receta receta = instancia.agregarReceta(consulta,
											java.time.LocalDate.now(), "Receta generada automaticamente");
									for (Medicamento med : meds) {
										if (med.isEsActivo()) {
											DetalleReceta detalle = new DetalleReceta("DR" + System.currentTimeMillis(),
													med, "1 unidad", "Cada 8 horas", 7, "Tomar con alimentos");
											receta.agregarDetalle(detalle);
											break; // Solo agregar el primer medicamento como ejemplo
										}
									}
								}

								// --- Crear Ordenes de Examen si hay examenes disponibles ---
								java.util.ArrayList<ExamenLaboratorio> exams = instancia.getCatalogoExamenes();
								if (exams != null && !exams.isEmpty()) {
									for (ExamenLaboratorio ex : exams) {
										if (ex.isEsActivo()) {
											instancia.agregarOrdenExamen(consulta, ex, java.time.LocalDate.now());
											break; // Solo ordenar el primer examen como ejemplo
										}
									}
								}

								// --- Crear Factura ---
								SeguroMedico seguroPaciente = paciente.getSeguro();
								Factura factura = instancia.agregarFactura(consulta, seguroPaciente,
										java.time.LocalDate.now());
								factura.agregarDetalle(new DetalleFactura("DF1", "Consulta Medica", 1, 1500.00f));

								// --- Registrar vacunas en tabla PacienteVacuna ---
								for (Map.Entry<JCheckBox, Vacuna> entry2 : vacunaChecks.entrySet()) {
									JCheckBox cb = entry2.getKey();
									if (cb.isSelected()) {
										Vacuna vac = entry2.getValue();
										if (!instancia.verificarVacunaRepetida(paciente, vac.getId())) {
											instancia.agregarPacienteVacuna(paciente, vac, java.time.LocalDate.now(),
													"LOTE-" + vac.getId(), 1);
										}
									}
								}

								JOptionPane.showMessageDialog(AgregarConsulta.this, "Consulta fue completada",
										"Informacion", JOptionPane.INFORMATION_MESSAGE);
								dispose();
							} else {
								JOptionPane.showMessageDialog(AgregarConsulta.this, "Error al guardar en base de datos",
										"Error", JOptionPane.ERROR_MESSAGE);
							}
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

	public void cargarComboEnfermedades() {
		ArrayList<EnfermedadBajoVigilancia> enfermedades = dao.EnfermedadDAO.listarEnfermedades();
		DefaultComboBoxModel<EnfermedadBajoVigilancia> model = new DefaultComboBoxModel<>();
		EnfermedadBajoVigilancia defaultOpt = new EnfermedadBajoVigilancia(null, "Ninguno", null, null);

		model.addElement(defaultOpt);

		if (enfermedades != null) {
			for (EnfermedadBajoVigilancia e : enfermedades) {
				model.addElement(e);
			}
		}
		comboBoxEnfermedades.setModel(model);
	}
}
