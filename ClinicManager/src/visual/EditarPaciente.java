package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.FlowLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Clinica;
import logico.Paciente;

public class EditarPaciente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	Clinica instancia = Clinica.getInstancia();
	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textCedula;
	private JTextField textEdad;
	private JComboBox<String> comboSexo;
	private javax.swing.JFormattedTextField textTelefono;
	private JTextField textDireccion;
	private JTextField textEstatura;
	private JTextField textPeso;
	private JComboBox<String> comboBoxTipoSangre;
	static String idPaciente;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			EditarPaciente dialog = new EditarPaciente(idPaciente);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditarPaciente(String idPaciente) {
		setModal(true);
		setResizable(false);
		setTitle("Editar Paciente");
		setBounds(100, 100, 508, 654);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		Paciente paciente = dao.PacienteDAO.buscarPacientePorId(idPaciente);

		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_9953132040000");
			panel.setLayout(null);

			textNombre = new JTextField();
			textNombre.setText(paciente.getNombre());
			textNombre.setEditable(true);
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
				textApellido.setEnabled(true);
				textApellido.setText(paciente.getApellido());
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
					javax.swing.text.MaskFormatter formatter = new javax.swing.text.MaskFormatter("###-#######-#");
					formatter.setPlaceholderCharacter('_');
					textCedula = new javax.swing.JFormattedTextField(formatter);
				} catch (Exception e) {
					textCedula = new javax.swing.JFormattedTextField();
				}
				textCedula.setEditable(true);
				textCedula.setEnabled(true);
				textCedula.setText(paciente.getCedula());
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
			textEdad.setText(String.valueOf(paciente.getEdad()));
			textEdad.setEnabled(true);
			textEdad.setBounds(119, 207, 66, 22);
			textEdad.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent evt) {
					if (!Character.isDigit(evt.getKeyChar())) {
						evt.consume();
					}
				}
			});
			panel.add(textEdad);

			JLabel lblSexo = new JLabel("Sexo");
			lblSexo.setBounds(119, 242, 56, 16);
			panel.add(lblSexo);

			String[] opciones = { "Masculino", "Femenino" };
			DefaultComboBoxModel<String> modelo = new DefaultComboBoxModel<>(opciones);
			this.comboSexo = new JComboBox<>();
			comboSexo.setEnabled(true);
			this.comboSexo.setModel(modelo);
			this.comboSexo.setBounds(119, 258, 270, 22);
			panel.add(this.comboSexo);
			selectStringItem(comboSexo, paciente.getSexo());

			JLabel lbTelefono = new JLabel("Telefono");
			lbTelefono.setBounds(120, 452, 56, 16);
			panel.add(lbTelefono);

			try {
				javax.swing.text.MaskFormatter formatterTel = new javax.swing.text.MaskFormatter("###-###-####");
				formatterTel.setPlaceholderCharacter('_');
				textTelefono = new javax.swing.JFormattedTextField(formatterTel);
			} catch (Exception e) {
				textTelefono = new javax.swing.JFormattedTextField();
			}
			textTelefono.setText(paciente.getTelefono());
			textTelefono.setEnabled(true);
			textTelefono.setBounds(120, 468, 270, 22);
			panel.add(textTelefono);
			textTelefono.setColumns(10);

			JLabel lbDireccion = new JLabel("Direccion");
			lbDireccion.setBounds(120, 503, 56, 16);
			panel.add(lbDireccion);

			textDireccion = new JTextField();
			textDireccion.setText(paciente.getDireccion());
			textDireccion.setEnabled(true);
			textDireccion.setBounds(120, 521, 270, 22);
			panel.add(textDireccion);
			textDireccion.setColumns(10);

			JLabel lbTipoSangre = new JLabel("Tipo de Sangre");
			lbTipoSangre.setBounds(119, 293, 97, 16);
			panel.add(lbTipoSangre);

			String[] opcionesTipoSangre = { "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" };
			DefaultComboBoxModel<String> modeloComboTipoSangre = new DefaultComboBoxModel<>(opcionesTipoSangre);
			comboBoxTipoSangre = new JComboBox<>(modeloComboTipoSangre);
			comboBoxTipoSangre.setEnabled(true);
			comboBoxTipoSangre.setBounds(119, 310, 270, 22);
			panel.add(comboBoxTipoSangre);
			selectStringItem(comboBoxTipoSangre, paciente.getTipoSangre());

			JLabel lbEstatura = new JLabel("Estatura");
			lbEstatura.setBounds(119, 345, 56, 16);
			panel.add(lbEstatura);

			textEstatura = new JTextField();
			textEstatura.setText(String.valueOf(paciente.getEstatura()));
			textEstatura.setEnabled(true);
			textEstatura.setBounds(119, 363, 66, 22);
			textEstatura.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent evt) {
					char c = evt.getKeyChar();
					if (!Character.isDigit(c) && c != '.') {
						evt.consume();
					}
				}
			});
			panel.add(textEstatura);

			JLabel lbPeso = new JLabel("Peso");
			lbPeso.setBounds(119, 398, 56, 16);
			panel.add(lbPeso);

			textPeso = new JTextField();
			textPeso.setText(String.valueOf(paciente.getPeso()));
			textPeso.setEnabled(true);
			textPeso.setBounds(118, 414, 67, 22);
			textPeso.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent evt) {
					char c = evt.getKeyChar();
					if (!Character.isDigit(c) && c != '.') {
						evt.consume();
					}
				}
			});
			panel.add(textPeso);

		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		javax.swing.JButton okButton = new javax.swing.JButton("Guardar");
		okButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				paciente.setNombre(textNombre.getText());
				paciente.setApellido(textApellido.getText());
				paciente.setCedula(textCedula.getText());
				paciente.setEdad(
						!textEdad.getText().trim().isEmpty() ? Integer.parseInt(textEdad.getText().trim()) : 0);
				paciente.setSexo(comboSexo.getSelectedItem().toString());
				paciente.setTelefono(textTelefono.getText());
				paciente.setDireccion(textDireccion.getText());
				paciente.setEstatura(
						!textEstatura.getText().trim().isEmpty() ? Float.parseFloat(textEstatura.getText().trim())
								: 0f);
				paciente.setPeso(
						!textPeso.getText().trim().isEmpty() ? Float.parseFloat(textPeso.getText().trim()) : 0f);
				paciente.setTipoSangre(comboBoxTipoSangre.getSelectedItem().toString());

				boolean actualizado = dao.PacienteDAO.actualizarPaciente(paciente);
				if (actualizado) {
					javax.swing.JOptionPane.showMessageDialog(null, "Paciente actualizado!", "Exito",
							javax.swing.JOptionPane.INFORMATION_MESSAGE);
					dispose();
				} else {
					javax.swing.JOptionPane.showMessageDialog(null, "Error al actualizar", "Error",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		javax.swing.JButton cancelButton = new javax.swing.JButton("Cancelar");
		cancelButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	public static void selectStringItem(JComboBox<String> comboBox, String item) {
		if (item == null || comboBox == null)
			return;
		comboBox.setSelectedItem(item.trim());
	}
}
