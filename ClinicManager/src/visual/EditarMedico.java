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

import logico.Medico;

public class EditarMedico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textMaxCitas;
	private JTextField textCedula;
	static String idMedico;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			EditarMedico dialog = new EditarMedico(idMedico);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditarMedico(String idMedico) {
		setResizable(false);
		setModal(true);
		setTitle("Editar Medico");
		setBounds(100, 100, 453, 516);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		Medico medico = null;
		for (Medico m : dao.MedicoDAO.listarMedicos()) {
			if (m.getId() != null && m.getId().trim().equals(String.valueOf(idMedico).trim())) {
				medico = m;
				break;
			}
		}

		if (medico == null) {
			JOptionPane.showMessageDialog(null,
					"Error critico: El Medico ha sido eliminado de forma remota o no se encontro en DB. \nID solicitado: "
							+ idMedico,
					"Error de concurrencia", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		JTextField textEdad;
		JComboBox<String> comboBoxSexo;
		JComboBox<String> comboBoxEspecialidad;
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
				textNombre.setEnabled(true);
				textNombre.setEditable(true);
				textNombre.setBounds(82, 56, 264, 22);
				textNombre.setText(medico.getNombre());
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
				textApellido.setEnabled(true);
				textApellido.setEditable(true);
				textApellido.setBounds(82, 109, 264, 22);
				textApellido.setText(medico.getApellido());
				panel.add(textApellido);
				textApellido.setColumns(10);
			}
			{
				JLabel lbEdad = new JLabel("Edad");
				lbEdad.setBounds(82, 245, 56, 16);
				panel.add(lbEdad);
			}
			{
				textEdad = new JTextField();
				textEdad.setBounds(82, 263, 56, 22);
				textEdad.setText(String.valueOf(medico.getEdad()));
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
				panel.add(comboBoxEspecialidad);
				selectStringItem(comboBoxEspecialidad, medico.getEspecialidad());
			}
			{
				JLabel lbMaxCitas = new JLabel("Cantidad de Citas");
				lbMaxCitas.setBounds(82, 350, 107, 16);
				panel.add(lbMaxCitas);
			}
			{
				textMaxCitas = new JTextField();
				textMaxCitas.setBounds(82, 371, 56, 22);
				textMaxCitas.setText(String.valueOf(medico.getMaxCitas()));
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
			textCedula.setEditable(true);
			textCedula.setEnabled(true);
			textCedula.setBounds(82, 159, 264, 22);
			textCedula.setText(medico.getCedula());
			panel.add(textCedula);
			textCedula.setColumns(10);

			JLabel lbSexo = new JLabel("Sexo");
			lbSexo.setBounds(82, 194, 56, 16);
			panel.add(lbSexo);

			String[] sexo = { "Masculino", "Femenino" };
			comboBoxSexo = new JComboBox<>();
			comboBoxSexo.setEnabled(true);
			comboBoxSexo.setBounds(82, 210, 264, 22);
			comboBoxSexo.setModel(new DefaultComboBoxModel<>(sexo));
			panel.add(comboBoxSexo);

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						String nombre = textNombre.getText().trim();
						String apellido = textApellido.getText().trim();
						String cedula = textCedula.getText().trim();
						String sexo = comboBoxSexo.getSelectedItem().toString();
						int edad = !textEdad.getText().trim().isEmpty() ? Integer.parseInt(textEdad.getText().trim())
								: 0;
						String especialidad = comboBoxEspecialidad.getSelectedItem().toString();
						int maxCitas = !textMaxCitas.getText().trim().isEmpty()
								? Integer.parseInt(textMaxCitas.getText().trim())
								: 0;

						Medico medico = null;
						for (Medico m : dao.MedicoDAO.listarMedicos()) {
							if (m.getId() != null && m.getId().trim().equals(String.valueOf(idMedico).trim())) {
								medico = m;
								break;
							}
						}

						if (medico == null) {
							JOptionPane.showMessageDialog(EditarMedico.this,
									"Error de concurrencia: el medico no existe", "Error", JOptionPane.ERROR_MESSAGE);
							return;
						}

						if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || sexo.isEmpty()
								|| especialidad.isEmpty() || edad < 25 || maxCitas < 1) {

							JOptionPane.showMessageDialog(EditarMedico.this, "hay campos vacios o invalidos", "Alerta",
									JOptionPane.ERROR_MESSAGE);

						}

						else {
							medico.setNombre(nombre);
							medico.setApellido(apellido);
							medico.setCedula(cedula);
							medico.setSexo(sexo);
							medico.setEdad(edad);
							medico.setEspecialidad(especialidad);
							medico.setMaxCitas(maxCitas);
							dao.MedicoDAO.actualizarMedico(medico);
							JOptionPane.showMessageDialog(EditarMedico.this, "Cambios Hechos", "Alerta",
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

	public static void selectStringItem(JComboBox<String> comboBox, String item) {
		if (item == null || comboBox == null)
			return;
		comboBox.setSelectedItem(item.trim());
	}
}
