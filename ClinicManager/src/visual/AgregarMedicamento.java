package visual;

import java.awt.BorderLayout;
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
import javax.swing.border.EmptyBorder;

import logico.Medicamento;

public class AgregarMedicamento extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textPrincipioActivo;
	private JComboBox<String> comboPresentacion;
	private JTextField textConcentracion;
	private Medicamento medicamentoEditar;

	public static void main(String[] args) {
		try {
			AgregarMedicamento dialog = new AgregarMedicamento(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AgregarMedicamento(Medicamento med) {
		this.medicamentoEditar = med;
		setModal(true);
		setResizable(false);
		setTitle(med == null ? "Agregar Medicamento" : "Editar Medicamento");
		setBounds(100, 100, 440, 340);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(30, 25, 120, 16);
		contentPanel.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(160, 22, 230, 25);
		contentPanel.add(textNombre);

		JLabel lblPrincipio = new JLabel("Principio Activo:");
		lblPrincipio.setBounds(30, 70, 120, 16);
		contentPanel.add(lblPrincipio);

		textPrincipioActivo = new JTextField();
		textPrincipioActivo.setBounds(160, 67, 230, 25);
		contentPanel.add(textPrincipioActivo);

		JLabel lblPresentacion = new JLabel("Presentacion:");
		lblPresentacion.setBounds(30, 115, 120, 16);
		contentPanel.add(lblPresentacion);

		String[] presentaciones = { "Tableta", "Capsula", "Jarabe", "Inyeccion", "Crema", "Gotas", "Supositorios",
				"Otro" };
		comboPresentacion = new JComboBox<>(new DefaultComboBoxModel<>(presentaciones));
		comboPresentacion.setBounds(160, 112, 230, 25);
		contentPanel.add(comboPresentacion);

		JLabel lblConcentracion = new JLabel("Concentracion:");
		lblConcentracion.setBounds(30, 160, 120, 16);
		contentPanel.add(lblConcentracion);

		textConcentracion = new JTextField();
		textConcentracion.setBounds(160, 157, 230, 25);
		contentPanel.add(textConcentracion);

		if (med != null) {
			textNombre.setText(med.getNombre());
			textPrincipioActivo.setText(med.getPrincipioActivo());
			comboPresentacion.setSelectedItem(med.getPresentacion());
			textConcentracion.setText(med.getConcentracion());
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textNombre.getText().trim();
				String principio = textPrincipioActivo.getText().trim();
				String presentacion = (String) comboPresentacion.getSelectedItem();
				String concentracion = textConcentracion.getText().trim();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(AgregarMedicamento.this, "El nombre es obligatorio.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Medicamento m = new Medicamento("0", nombre, principio, presentacion, concentracion);
				if (medicamentoEditar == null) {
					if (dao.MedicamentoDAO.registrarMedicamento(m)) {
						JOptionPane.showMessageDialog(AgregarMedicamento.this, "Medicamento agregado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarMedicamento.this, "Error guardando en BD.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					m.setId(medicamentoEditar.getId());
					if (dao.MedicamentoDAO.actualizarMedicamento(m)) {
						JOptionPane.showMessageDialog(AgregarMedicamento.this, "Medicamento actualizado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarMedicamento.this, "Error guardando en BD.", "Error",
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
