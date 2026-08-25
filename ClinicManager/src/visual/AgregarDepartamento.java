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

import logico.Departamento;

public class AgregarDepartamento extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textDescripcion;
	private JTextField textUbicacion;
	private Departamento departamentoEditar;

	public static void main(String[] args) {
		try {
			AgregarDepartamento dialog = new AgregarDepartamento(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AgregarDepartamento(Departamento dep) {
		this.departamentoEditar = dep;
		setModal(true);
		setResizable(false);
		setTitle(dep == null ? "Agregar Departamento" : "Editar Departamento");
		setBounds(100, 100, 420, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(30, 25, 100, 16);
		contentPanel.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(140, 22, 230, 25);
		contentPanel.add(textNombre);
		textNombre.setColumns(10);

		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setBounds(30, 70, 100, 16);
		contentPanel.add(lblDescripcion);

		textDescripcion = new JTextField();
		textDescripcion.setBounds(140, 67, 230, 25);
		contentPanel.add(textDescripcion);
		textDescripcion.setColumns(10);

		JLabel lblUbicacion = new JLabel("Ubicacion:");
		lblUbicacion.setBounds(30, 115, 100, 16);
		contentPanel.add(lblUbicacion);

		textUbicacion = new JTextField();
		textUbicacion.setBounds(140, 112, 230, 25);
		contentPanel.add(textUbicacion);
		textUbicacion.setColumns(10);

		if (dep != null) {
			textNombre.setText(dep.getNombre());
			textDescripcion.setText(dep.getDescripcion());
			textUbicacion.setText(dep.getUbicacion());
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textNombre.getText().trim();
				String descripcion = textDescripcion.getText().trim();
				String ubicacion = textUbicacion.getText().trim();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(AgregarDepartamento.this, "El nombre es obligatorio.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Departamento dep = new Departamento("0", nombre, descripcion, ubicacion);
				if (departamentoEditar == null) {
					if (dao.DepartamentoDAO.registrarDepartamento(dep)) {
						JOptionPane.showMessageDialog(AgregarDepartamento.this, "Departamento agregado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarDepartamento.this, "Error guardando en BD.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					dep.setId(departamentoEditar.getId());
					if (dao.DepartamentoDAO.actualizarDepartamento(dep)) {
						JOptionPane.showMessageDialog(AgregarDepartamento.this,
								"Departamento actualizado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarDepartamento.this, "Error guardando en BD.", "Error",
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
