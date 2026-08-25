package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import logico.SeguroMedico;

public class AgregarSeguro extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textRnc;
	private javax.swing.JFormattedTextField textTelefono;
	private JTextField textCobertura;
	private SeguroMedico seguroEditar;

	public static void main(String[] args) {
		try {
			AgregarSeguro dialog = new AgregarSeguro(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AgregarSeguro(SeguroMedico seguro) {
		this.seguroEditar = seguro;
		setModal(true);
		setResizable(false);
		setTitle(seguro == null ? "Agregar Seguro Medico" : "Editar Seguro Medico");
		setBounds(100, 100, 420, 340);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(30, 25, 100, 16);
		contentPanel.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(150, 22, 220, 25);
		contentPanel.add(textNombre);

		JLabel lblRnc = new JLabel("RNC:");
		lblRnc.setBounds(30, 70, 100, 16);
		contentPanel.add(lblRnc);

		textRnc = new JTextField();
		textRnc.setBounds(150, 67, 220, 25);
		contentPanel.add(textRnc);

		JLabel lblTelefono = new JLabel("Telefono:");
		lblTelefono.setBounds(30, 115, 100, 16);
		contentPanel.add(lblTelefono);

		try {
			javax.swing.text.MaskFormatter formatterTel = new javax.swing.text.MaskFormatter("###-###-####");
			formatterTel.setPlaceholderCharacter('_');
			textTelefono = new javax.swing.JFormattedTextField(formatterTel);
		} catch (Exception e) {
			textTelefono = new javax.swing.JFormattedTextField();
		}
		textTelefono.setBounds(150, 112, 220, 25);
		contentPanel.add(textTelefono);

		JLabel lblCobertura = new JLabel("Cobertura (%):");
		lblCobertura.setBounds(30, 160, 110, 16);
		contentPanel.add(lblCobertura);

		textCobertura = new JTextField();
		textCobertura.setBounds(150, 157, 220, 25);
		contentPanel.add(textCobertura);

		if (seguro != null) {
			textNombre.setText(seguro.getNombre());
			textRnc.setText(seguro.getRnc());
			textTelefono.setText(seguro.getTelefono());
			textCobertura.setText(String.valueOf(seguro.getPorcentajeCobertura()));
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textNombre.getText().trim();
				String rnc = textRnc.getText().trim();
				String telefono = textTelefono.getText().trim();
				String coberturaStr = textCobertura.getText().trim();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(AgregarSeguro.this, "El nombre es obligatorio.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				float cobertura;
				try {
					cobertura = Float.parseFloat(coberturaStr);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(AgregarSeguro.this, "Ingrese un porcentaje valido.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				SeguroMedico s = new SeguroMedico("0", nombre, rnc, telefono, cobertura);
				if (seguroEditar == null) {
					if (dao.SeguroMedicoDAO.registrarSeguro(s)) {
						JOptionPane.showMessageDialog(AgregarSeguro.this, "Seguro agregado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarSeguro.this, "Error guardando en BD.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					s.setId(seguroEditar.getId());
					if (dao.SeguroMedicoDAO.actualizarSeguro(s)) {
						JOptionPane.showMessageDialog(AgregarSeguro.this, "Seguro actualizado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarSeguro.this, "Error guardando en BD.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				}
				dispose();
			}
		});
		buttonPane.add(btnGuardar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCancelar);
	}
}
