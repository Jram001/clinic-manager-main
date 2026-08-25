package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Clinica;
import logico.Control;
import logico.Medico;
import logico.Usuario;

public class AgregarMedico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textMaxCitas;
	private JTextField textCedula;
	private final java.util.HashMap<String, String> mapaEspecialidades = new java.util.HashMap<>();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			AgregarMedico dialog = new AgregarMedico();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AgregarMedico() {
		UIManager.put("ComboBox.disabledForeground", java.awt.Color.BLACK);
		setResizable(false);
		setModal(true);
		setTitle("Agregar Medico");
		setBounds(100, 100, 453, 566);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		inicializarMapaEspecialidades();

		JTextField textEdad;
		JComboBox<String> comboBoxSexo;
		JComboBox<String> comboBoxEspecialidad;
		JComboBox<String> comboBoxDepartamento;
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_101204491147100");
			panel.setLayout(null);
			{
				JLabel lbNombre = new JLabel("Nombre");
				lbNombre.setBounds(82, 38, 56, 16);
				panel.add(lbNombre);
			}
			{
				textNombre = new JTextField();
				textNombre.setBounds(82, 56, 264, 22);
				panel.add(textNombre);
				textNombre.setColumns(10);
			}
			{
				JLabel lbApellido = new JLabel("Apellido");
				lbApellido.setBounds(82, 91, 56, 16);
				panel.add(lbApellido);
			}
			{
				textApellido = new JTextField();
				textApellido.setBounds(82, 109, 264, 22);
				panel.add(textApellido);
				textApellido.setColumns(10);
			}
			{
				JLabel lbEdad = new JLabel("Edad");
				lbEdad.setBounds(82, 245, 56, 16);
				panel.add(lbEdad);
			}
			{
				textEdad = new JTextField("25");
				textEdad.setBounds(82, 263, 56, 22);
				textEdad.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent evt) {
						if (!Character.isDigit(evt.getKeyChar())) {
							evt.consume();
						}
					}
				});
				panel.add(textEdad);
			}
			{
				JLabel lbEspecialidad = new JLabel("Especialidad");
				lbEspecialidad.setBounds(82, 298, 100, 16);
				panel.add(lbEspecialidad);
			}
			{
				comboBoxEspecialidad = new JComboBox<>();
				comboBoxEspecialidad.setBounds(82, 315, 264, 22);
				panel.add(comboBoxEspecialidad);

				String[] especialidades = {
						"Alergologia",
						"Anestesiologia",
						"Cardiologia",
						"Cirugia General",
						"Cirugia Cardiovascular",
						"Cirugia Plastica y Reconstructiva",
						"Cirugia Pediatrica",
						"Cirugia Vascular",
						"Dermatologia",
						"Endocrinologia",
						"Ginecologia y Obstetricia",
						"Gastroenterologia",
						"Geriatria",
						"Hematologia",
						"Infectologia",
						"Inmunologia",
						"Medicina Familiar y Comunitaria",
						"Medicina Interna",
						"Medicina Intensiva",
						"Medicina Nuclear",
						"Medicina Preventiva y Salud Publica",
						"Nefrologia",
						"Neumologia",
						"Neurologia",
						"Neurocirugia",
						"Nutricion Clinica",
						"Oftalmologia",
						"Oncologia Medica",
						"Oncologia Radioterapica",
						"Otorrinolaringologia",
						"Pediatria",
						"Psiquiatria",
						"Rehabilitacion y Medicina Fisica",
						"Reumatologia",
						"Traumatologia y Ortopedia",
						"Urologia",
						"Coloproctologia",
						"Cirugia Toracica",
						"Medicina del Trabajo",
						"Medicina del Deporte",
						"Genetica Medica",
						"Patologia (Anatomia Patologica)",
						"Radiologia",
						"Radiologia Intervencionista",
						"Urgencias y Medicina de Emergencias",
						"Medicina Paliativa",
						"Toxicologia",
						"Medicina Forense",
						"Salud Sexual y Reproductiva",
						"Medicina Estetica"
				};

				comboBoxEspecialidad.setModel(new DefaultComboBoxModel<>(especialidades));
			}
			{
				JLabel lbDepartamento = new JLabel("Departamento");
				lbDepartamento.setBounds(82, 350, 100, 16);
				panel.add(lbDepartamento);
			}
			{
				comboBoxDepartamento = new JComboBox<>();
				comboBoxDepartamento.setBounds(82, 368, 264, 22);
				comboBoxDepartamento.setEnabled(false);
				panel.add(comboBoxDepartamento);

				java.util.ArrayList<logico.Departamento> deptos = dao.DepartamentoDAO.listarDepartamentos();
				if (deptos != null) {
					for (logico.Departamento d : deptos) {
						if (d.isEsActivo()) {
							comboBoxDepartamento.addItem(d.getNombre());
						}
					}
				}
			}

			comboBoxEspecialidad.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String selected = (String) comboBoxEspecialidad.getSelectedItem();
					if (selected != null) {
						String departamentoDestino = mapaEspecialidades.get(selected);
						if (departamentoDestino != null) {
							comboBoxDepartamento.setSelectedItem(departamentoDestino);
						}
					}
				}
			});
			{
				JLabel lbMaxCitas = new JLabel("Cantidad de Citas");
				lbMaxCitas.setBounds(82, 400, 107, 16);
				panel.add(lbMaxCitas);
			}
			{
				textMaxCitas = new JTextField("1");
				textMaxCitas.setBounds(82, 419, 56, 28);
				textMaxCitas.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyTyped(java.awt.event.KeyEvent evt) {
						if (!Character.isDigit(evt.getKeyChar())) {
							evt.consume();
						}
					}
				});
				panel.add(textMaxCitas);
			}

			JLabel lbCedula = new JLabel("Cedula");
			lbCedula.setBounds(82, 141, 56, 16);
			panel.add(lbCedula);

			try {
				javax.swing.text.MaskFormatter formatter = new javax.swing.text.MaskFormatter("###-#######-#");
				formatter.setPlaceholderCharacter('_');
				textCedula = new javax.swing.JFormattedTextField(formatter);
			} catch (Exception e) {
				textCedula = new javax.swing.JFormattedTextField();
			}
			textCedula.setBounds(82, 159, 264, 22);
			textCedula.setColumns(10);

			panel.add(textCedula);

			JLabel lbSexo = new JLabel("Sexo");
			lbSexo.setBounds(82, 194, 56, 16);
			panel.add(lbSexo);

			String[] sexo = { "Masculino", "Femenino" };
			comboBoxSexo = new JComboBox<>();
			comboBoxSexo.setBounds(82, 210, 264, 22);
			comboBoxSexo.setModel(new DefaultComboBoxModel<>(sexo));
			panel.add(comboBoxSexo);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Agregar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String nombre = textNombre.getText().trim();
						String apellido = textApellido.getText().trim();
						int edad = !textEdad.getText().trim().isEmpty() ? Integer.parseInt(textEdad.getText().trim())
								: 0;
						String cedula = textCedula.getText().trim();
						String sexo = comboBoxSexo.getSelectedItem().toString();
						String especialidad = comboBoxEspecialidad.getSelectedItem().toString();
						int maxCitas = !textMaxCitas.getText().trim().isEmpty()
								? Integer.parseInt(textMaxCitas.getText().trim())
								: 0;
						boolean repetido = Clinica.getInstancia().verificarCedula(cedula);

						if (nombre.isEmpty() || apellido.isEmpty() || edad < 25 || cedula.isEmpty() || sexo.isEmpty() ||
								especialidad.isEmpty() || maxCitas < 1 || repetido) {
							if (edad < 25) {
								JOptionPane.showMessageDialog(AgregarMedico.this, "Tienes que tener almenos 25 anios",
										"Alerta", JOptionPane.ERROR_MESSAGE);

							}

							if (maxCitas < 1) {
								JOptionPane.showMessageDialog(AgregarMedico.this,
										"Tienes que tener almenos 1 cita al dia", "Alerta", JOptionPane.ERROR_MESSAGE);
							}

							if (repetido) {
								JOptionPane.showMessageDialog(AgregarMedico.this, "Ya hay alguien con esa cedula",
										"Alerta", JOptionPane.ERROR_MESSAGE);
							}

							else {
								JOptionPane.showMessageDialog(AgregarMedico.this, "Hay Campos Faltantes", "Alerta",
										JOptionPane.ERROR_MESSAGE);
							}
						}

						else {
							Medico medico = new Medico(java.util.UUID.randomUUID().toString(), nombre, apellido, edad,
									cedula, sexo, especialidad, maxCitas);
							dao.MedicoDAO.registrarMedico(medico);

							Usuario nuevoMedico = new Usuario("Med" + nombre,
									Control.md5("123456"), "medico", medico.getId());
							Control.getInstance().regUser(nuevoMedico);
							Control.getInstance().guardarAlDisco();
							JOptionPane.showMessageDialog(AgregarMedico.this, "Medico Creado \n Usuario: " +
									nuevoMedico.getNombreUsuario() + "  Clave: 123456", "Exito",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
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

	private void inicializarMapaEspecialidades() {
		mapaEspecialidades.put("Cardiologia", "Cardiología");
		mapaEspecialidades.put("Cirugia Cardiovascular", "Cardiología");

		mapaEspecialidades.put("Pediatria", "Pediatría");
		mapaEspecialidades.put("Cirugia Pediatrica", "Pediatría");

		mapaEspecialidades.put("Cirugia General", "Cirugía");
		mapaEspecialidades.put("Cirugia Plastica y Reconstructiva", "Cirugía");
		mapaEspecialidades.put("Cirugia Vascular", "Cirugía");
		mapaEspecialidades.put("Cirugia Toracica", "Cirugía");
		mapaEspecialidades.put("Neurocirugia", "Cirugía");
		mapaEspecialidades.put("Coloproctologia", "Cirugía");
		mapaEspecialidades.put("Anestesiologia", "Cirugía");
		mapaEspecialidades.put("Urologia", "Cirugía");

		mapaEspecialidades.put("Ginecologia y Obstetricia", "Ginecología y Obstetricia");

		mapaEspecialidades.put("Neurologia", "Neurología y Psiquiatría");
		mapaEspecialidades.put("Psiquiatria", "Neurología y Psiquiatría");

		mapaEspecialidades.put("Oncologia Medica", "Oncología");
		mapaEspecialidades.put("Oncologia Radioterapica", "Oncología");

		mapaEspecialidades.put("Traumatologia y Ortopedia", "Traumatología");
		mapaEspecialidades.put("Rehabilitacion y Medicina Fisica", "Traumatología");

		mapaEspecialidades.put("Urgencias y Medicina de Emergencias", "Urgencias");
		mapaEspecialidades.put("Toxicologia", "Urgencias");

		mapaEspecialidades.put("Patologia (Anatomia Patologica)", "Laboratorio Clínico");
		mapaEspecialidades.put("Genetica Medica", "Laboratorio Clínico");
		mapaEspecialidades.put("Medicina Forense", "Laboratorio Clínico");

		mapaEspecialidades.put("Hematologia", "Banco de Sangre");

		mapaEspecialidades.put("Medicina Familiar y Comunitaria", "Medicina General");
		mapaEspecialidades.put("Medicina Preventiva y Salud Publica", "Medicina General");
		mapaEspecialidades.put("Medicina del Trabajo", "Medicina General");
		mapaEspecialidades.put("Medicina del Deporte", "Medicina General");
		mapaEspecialidades.put("Medicina Estetica", "Medicina General");
		mapaEspecialidades.put("Salud Sexual y Reproductiva", "Medicina General");
		mapaEspecialidades.put("Oftalmologia", "Medicina General");
		mapaEspecialidades.put("Otorrinolaringologia", "Medicina General");

		mapaEspecialidades.put("Medicina Interna", "Medicina Interna");
		mapaEspecialidades.put("Alergologia", "Medicina Interna");
		mapaEspecialidades.put("Endocrinologia", "Medicina Interna");
		mapaEspecialidades.put("Gastroenterologia", "Medicina Interna");
		mapaEspecialidades.put("Geriatria", "Medicina Interna");
		mapaEspecialidades.put("Infectologia", "Medicina Interna");
		mapaEspecialidades.put("Inmunologia", "Medicina Interna");
		mapaEspecialidades.put("Medicina Intensiva", "Medicina Interna");
		mapaEspecialidades.put("Nefrologia", "Medicina Interna");
		mapaEspecialidades.put("Neumologia", "Medicina Interna");
		mapaEspecialidades.put("Reumatologia", "Medicina Interna");
		mapaEspecialidades.put("Nutricion Clinica", "Medicina Interna");
		mapaEspecialidades.put("Dermatologia", "Medicina Interna");
		mapaEspecialidades.put("Medicina Nuclear", "Medicina Interna");
		mapaEspecialidades.put("Radiologia", "Medicina Interna");
		mapaEspecialidades.put("Radiologia Intervencionista", "Medicina Interna");
		mapaEspecialidades.put("Medicina Paliativa", "Medicina Interna");
	}

}