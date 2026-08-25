package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Clinica;
import logico.Paciente;
import logico.SeguroMedico;
import logico.Vacuna;

public class AgregarPaciente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Clinica instancia = Clinica.getInstancia();
	private final Map<JCheckBox, Vacuna> vacunaChecks = new LinkedHashMap<>();
	private JTextField textNombre;
	private JTextField textApellido;
	private JFormattedTextField textCedula;
	private JTextField textEdad;
	private JComboBox<String> comboSexo;
	private JFormattedTextField textTelefono;
	private JTextField textDireccion;
	private JTextField textEstatura;
	private JTextField textPeso;
	private static String citaId;
	private static String cedula;
	private String createdPacienteId = null;
	private JComboBox<String> comboSeguro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			AgregarPaciente dialog = new AgregarPaciente(citaId, cedula);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AgregarPaciente(String citaId, String cedula) {
		setModal(true);
		setResizable(false);
		setTitle("Agregar Paciente");
		setBounds(100, 100, 514, 800);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_9953132040000");
			panel.setLayout(null);

			textNombre = new JTextField();
			textNombre.setBounds(119, 50, 270, 22);
			panel.add(textNombre);
			textNombre.setColumns(10);

			JLabel lblNewLabel = new JLabel("Nombre");
			lblNewLabel.setBounds(119, 32, 56, 16);
			panel.add(lblNewLabel);
			{
				JLabel lblApellido = new JLabel("Apellido");
				lblApellido.setBounds(119, 85, 56, 16);
				panel.add(lblApellido);
			}
			{
				textApellido = new JTextField();
				textApellido.setBounds(119, 102, 270, 22);
				panel.add(textApellido);
				textApellido.setColumns(10);
			}
			{
				JLabel lblCedula = new JLabel("Cedula");
				lblCedula.setBounds(119, 137, 56, 16);
				panel.add(lblCedula);
			}
			{
				try {
					MaskFormatter formatter = new MaskFormatter("###-#######-#");
					formatter.setPlaceholderCharacter('_');
					textCedula = new JFormattedTextField(formatter);
				} catch (Exception e) {
					textCedula = new JFormattedTextField();
				}
				if (cedula != null && (!cedula.trim().isEmpty() && !cedula.trim().equals("___-_______-_"))) {
					textCedula.setText(cedula);
					textCedula.setEnabled(false);
				}
				textCedula.setBounds(119, 154, 270, 22);
				panel.add(textCedula);
				textCedula.setColumns(10);
			}
			{
				JLabel lblEdad = new JLabel("Edad");
				lblEdad.setBounds(119, 189, 56, 16);
				panel.add(lblEdad);
			}

			textEdad = new JTextField();
			textEdad.setBounds(119, 207, 66, 22);
			panel.add(textEdad);
			textEdad.setColumns(10);

			JLabel lblSexo = new JLabel("Sexo");
			lblSexo.setBounds(119, 242, 56, 16);
			panel.add(lblSexo);

			String[] opciones = { "Masculino", "Femenino" };
			DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>(opciones);
			this.comboSexo = new JComboBox<>();
			this.comboSexo.setModel(modelo);
			this.comboSexo.setBounds(119, 258, 270, 22);
			panel.add(this.comboSexo);

			JLabel lbTelefono = new JLabel("Telefono");
			lbTelefono.setBounds(120, 452, 56, 16);
			panel.add(lbTelefono);

			try {
				MaskFormatter formatterTel = new MaskFormatter("###-###-####");
				formatterTel.setPlaceholderCharacter('_');
				textTelefono = new JFormattedTextField(formatterTel);
			} catch (Exception e) {
				textTelefono = new JFormattedTextField();
			}
			textTelefono.setBounds(120, 468, 270, 22);
			textTelefono.setColumns(10);
			panel.add(textTelefono);

			JLabel lbDireccion = new JLabel("Direccion");
			lbDireccion.setBounds(120, 503, 56, 16);
			panel.add(lbDireccion);

			textDireccion = new JTextField();
			textDireccion.setBounds(120, 521, 270, 22);
			panel.add(textDireccion);
			textDireccion.setColumns(10);

			JLabel lbSeguro = new JLabel("Seguro Médico");
			lbSeguro.setBounds(120, 550, 100, 16);
			panel.add(lbSeguro);

			comboSeguro = new JComboBox<>();
			comboSeguro.setBounds(120, 568, 270, 22);
			panel.add(comboSeguro);
			cargarSeguros();

			JLabel lbTipoSangre = new JLabel("Tipo de Sangre");
			lbTipoSangre.setBounds(119, 293, 97, 16);
			panel.add(lbTipoSangre);

			String[] opcionesTipoSangre = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
			DefaultComboBoxModel<String> modeloComboTipoSangre = new DefaultComboBoxModel<>(opcionesTipoSangre);
			JComboBox<String> comboBoxTipoSangre = new JComboBox<>(modeloComboTipoSangre);
			comboBoxTipoSangre.setBounds(119, 310, 270, 22);
			panel.add(comboBoxTipoSangre);

			JLabel lbEstatura = new JLabel("Estatura (ft)");
			lbEstatura.setBounds(119, 345, 100, 16);
			panel.add(lbEstatura);

			textEstatura = new JTextField();
			textEstatura.setBounds(119, 363, 66, 22);
			panel.add(textEstatura);
			textEstatura.setColumns(10);

			JLabel lbPeso = new JLabel("Peso (lb)");
			lbPeso.setBounds(119, 398, 100, 16);
			panel.add(lbPeso);

			textPeso = new JTextField();
			textPeso.setBounds(119, 414, 66, 22);
			panel.add(textPeso);
			textPeso.setColumns(10);

			JLabel lbVacunas = new JLabel("Vacunas");
			lbVacunas.setBounds(119, 600, 56, 16);
			panel.add(lbVacunas);

			JPanel vaccinesPanel = new JPanel();
			vaccinesPanel.setLayout(new BoxLayout(vaccinesPanel, BoxLayout.Y_AXIS));
			vaccinesPanel.setBorder(new EmptyBorder(4, 4, 4, 4));

			JScrollPane scrollVacunas = new JScrollPane(vaccinesPanel);
			scrollVacunas.setBounds(120, 620, 270, 100);
			panel.add(scrollVacunas);

			ArrayList<Vacuna> catalogoVacunas = instancia.getCatalogoVacunas();
			int y = 700;
			int x = 120;
			int espaciado = 32;

			if (catalogoVacunas == null || catalogoVacunas.isEmpty()) {
				JCheckBox checkBox = new JCheckBox("No hay Vacunas");
				checkBox.setEnabled(false);
				checkBox.setBounds(x, y, 300, 25);
				vaccinesPanel.add(checkBox);
			} else {
				JCheckBox cbNinguna = new JCheckBox("Ninguna");
				cbNinguna.setBounds(x, y, 300, 25);
				vaccinesPanel.add(cbNinguna);
				cbNinguna.setSelected(true);

				cbNinguna.addActionListener(e -> {
					if (cbNinguna.isSelected()) {
						for (JCheckBox cb : vacunaChecks.keySet()) {
							cb.setSelected(false);
							cb.setEnabled(false);
						}
					} else {
						for (JCheckBox cb : vacunaChecks.keySet()) {
							cb.setEnabled(true);
						}
					}
				});
				y += espaciado;

				for (Vacuna v : catalogoVacunas) {
					if (v == null || !v.isEsActivo()) {
						continue;
					}

					JCheckBox checkBox = new JCheckBox(v.getNombre());
					checkBox.setBounds(x, y, 300, 25);
					checkBox.setEnabled(!cbNinguna.isSelected());

					checkBox.addActionListener(e -> {
						if (checkBox.isSelected()) {
							cbNinguna.setSelected(false);
						}
					});

					vacunaChecks.put(checkBox, v);
					vaccinesPanel.add(checkBox);
					y += espaciado;
				}
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
							String cedula = textCedula.getText().trim();
							String sexo = comboSexo.getSelectedItem().toString();
							String telefono = textTelefono.getText().trim();
							String direccion = textDireccion.getText().trim();
							String tipoSangre = comboBoxTipoSangre.getSelectedItem().toString();

							int edad = 0;
							float peso = 0;
							float estatura = 0;
							try {
								edad = Integer.parseInt(textEdad.getText().trim());
								peso = Float.parseFloat(textPeso.getText().trim().replace(',', '.'));
								estatura = Float.parseFloat(textEstatura.getText().trim().replace(',', '.'));
							} catch (NumberFormatException ex) {
								edad = -1;
								peso = -1;
								estatura = -1;
							}

							if (edad < 0 || nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || sexo.isEmpty()
									||
									telefono.isEmpty() || direccion.isEmpty() || tipoSangre.isEmpty() || peso < 0.1
									|| estatura < 0.1) {
								JOptionPane.showMessageDialog(AgregarPaciente.this, "Hay Campos vacios", "Alerta",
										JOptionPane.INFORMATION_MESSAGE);
							} else {
								Paciente paciente = new Paciente(java.util.UUID.randomUUID().toString(), nombre,
										apellido, edad, cedula, sexo, peso, estatura, tipoSangre, direccion, telefono);
								if (comboSeguro.getSelectedIndex() > 0) {
									String segName = comboSeguro.getSelectedItem().toString();
									for (SeguroMedico s : dao.SeguroMedicoDAO.listarSeguros()) {
										if (s.getNombre().equals(segName)) {
											paciente.setSeguro(s);
											break;
										}
									}
								}

								dao.PacienteDAO.registrarPaciente(paciente);

								for (Map.Entry<JCheckBox, Vacuna> entry : vacunaChecks.entrySet()) {
									JCheckBox checkbox = entry.getKey();
									if (checkbox.isSelected()) {
										Vacuna v = entry.getValue();
										Vacuna copia = new Vacuna(v.getId(), v.getNombre(), v.getFabricante(),
												v.getDosis(), v.getDescripcion());
										copia.setAplicada(true);
										paciente.agregarVacuna(copia);
									}
								}
								createdPacienteId = paciente.getId();

								JOptionPane.showMessageDialog(AgregarPaciente.this, "Paciente fue creado", "Alerta",
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
						public void actionPerformed(ActionEvent arg0) {
							dispose();
						}
					});
					cancelButton.setActionCommand("Cancel");
					buttonPane.add(cancelButton);
				}
			}
		}
	}

	public String getCreatedPacienteId() {
		return createdPacienteId;
	}

	private void cargarSeguros() {
		comboSeguro.removeAllItems();
		comboSeguro.addItem("-- Sin Seguro --");
		ArrayList<SeguroMedico> seguros = dao.SeguroMedicoDAO.listarSeguros();
		if (seguros != null) {
			for (SeguroMedico s : seguros) {
				if (s.isEsActivo()) {
					comboSeguro.addItem(s.getNombre());
				}
			}
		}
	}
}