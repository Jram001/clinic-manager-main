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

import logico.Secretaria;

public class AgregarSecretaria extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textApellido;
	private JTextField textCedula;
	private JTextField textEdad;
	private JComboBox<String> comboBoxSexo;
	private JComboBox<String> comboBoxTurno;
	private JTextField textSalario;
	private JTextField textExtension;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			AgregarSecretaria dialog = new AgregarSecretaria();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AgregarSecretaria() {
		setResizable(false);
		setModal(true);
		setTitle("Agregar Secretaria");
		setBounds(100, 100, 453, 516);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_101204491147100");
			panel.setLayout(null);

			JLabel lbNombre = new JLabel("Nombre");
			lbNombre.setBounds(82, 38, 56, 16);
			panel.add(lbNombre);

			textNombre = new JTextField();
			textNombre.setBounds(82, 56, 264, 22);
			panel.add(textNombre);
			textNombre.setColumns(10);

			JLabel lbApellido = new JLabel("Apellido");
			lbApellido.setBounds(82, 91, 56, 16);
			panel.add(lbApellido);

			textApellido = new JTextField();
			textApellido.setBounds(82, 109, 264, 22);
			panel.add(textApellido);
			textApellido.setColumns(10);

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

			String[] sexo = { "Femenino", "Masculino" };
			comboBoxSexo = new JComboBox<>();
			comboBoxSexo.setBounds(82, 210, 264, 22);
			comboBoxSexo.setModel(new DefaultComboBoxModel<>(sexo));
			panel.add(comboBoxSexo);

			JLabel lbEdad = new JLabel("Edad");
			lbEdad.setBounds(82, 245, 56, 16);
			panel.add(lbEdad);

			textEdad = new JTextField("18");
			textEdad.setBounds(82, 263, 56, 22);
			textEdad.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent evt) {
					if (!Character.isDigit(evt.getKeyChar())) {
						evt.consume();
					}
				}
			});
			panel.add(textEdad);

			JLabel lbTurno = new JLabel("Turno");
			lbTurno.setBounds(82, 298, 100, 16);
			panel.add(lbTurno);

			String[] turnos = { "Mañana", "Tarde", "Noche" };
			comboBoxTurno = new JComboBox<>();
			comboBoxTurno.setBounds(82, 315, 264, 22);
			comboBoxTurno.setModel(new DefaultComboBoxModel<>(turnos));
			panel.add(comboBoxTurno);

			JLabel lbSalario = new JLabel("Salario base");
			lbSalario.setBounds(82, 350, 107, 16);
			panel.add(lbSalario);

			textSalario = new JTextField("20000.0");
			textSalario.setBounds(82, 371, 90, 22);
			textSalario.addKeyListener(new java.awt.event.KeyAdapter() {
				public void keyTyped(java.awt.event.KeyEvent evt) {
					char c = evt.getKeyChar();
					if (!Character.isDigit(c) && c != '.') {
						evt.consume();
					}
				}
			});
			panel.add(textSalario);

			JLabel lbExt = new JLabel("Ext. Telefonica");
			lbExt.setBounds(200, 350, 107, 16);
			panel.add(lbExt);

			textExtension = new JTextField();
			int maxExtLength = 3;
			textExtension.setDocument(new javax.swing.text.PlainDocument() {
				@Override
				public void insertString(int offs, String str, javax.swing.text.AttributeSet a)
						throws javax.swing.text.BadLocationException {
					if (str == null)
						return;
					StringBuilder filtered = new StringBuilder();
					for (int i = 0; i < str.length(); i++) {
						char ch = str.charAt(i);
						if (Character.isDigit(ch))
							filtered.append(ch);
					}
					int currentLength = getLength();
					String toInsert = filtered.toString();
					if (toInsert.length() + currentLength > maxExtLength) {
						int allowed = maxExtLength - currentLength;
						if (allowed <= 0)
							return;
						toInsert = toInsert.substring(0, allowed);
					}
					super.insertString(offs, toInsert, a);
				}
			});
			textExtension.setBounds(200, 371, 146, 22);
			panel.add(textExtension);

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
						String turno = comboBoxTurno.getSelectedItem().toString();
						double salario = !textSalario.getText().trim().isEmpty()
								? Double.parseDouble(textSalario.getText().trim())
								: 0.0;
						String extension = textExtension.getText().trim();

						if (nombre.isEmpty() || apellido.isEmpty() || cedula.isEmpty() || turno.isEmpty()
								|| extension.isEmpty()) {
							JOptionPane.showMessageDialog(AgregarSecretaria.this, "Hay Campos Faltantes", "Alerta",
									JOptionPane.ERROR_MESSAGE);
						} else {
							Secretaria secretaria = new Secretaria(java.util.UUID.randomUUID().toString(), cedula,
									nombre, apellido, "", "", sexo, edad, turno, salario, extension);
							boolean success = dao.SecretariaDAO.registrarSecretaria(secretaria);

							if (success) {
								JOptionPane.showMessageDialog(AgregarSecretaria.this,
										"Secretaria Creada \n Usuario: Sec" + nombre + "  Clave: 123456", "Exito",
										JOptionPane.INFORMATION_MESSAGE);
								dispose();
							} else {
								JOptionPane.showMessageDialog(AgregarSecretaria.this,
										"Fallo el registro en Base de Datos", "Alerta",
										JOptionPane.ERROR_MESSAGE);
							}
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

}
