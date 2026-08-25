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

import logico.ExamenLaboratorio;

public class AgregarExamen extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	private JTextField textDescripcion;
	private JComboBox<String> comboCategoria;
	private JTextField textPrecio;
	private ExamenLaboratorio examenEditar;

	public static void main(String[] args) {
		try {
			AgregarExamen dialog = new AgregarExamen(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AgregarExamen(ExamenLaboratorio examen) {
		this.examenEditar = examen;
		setModal(true);
		setResizable(false);
		setTitle(examen == null ? "Agregar Examen de Laboratorio" : "Editar Examen de Laboratorio");
		setBounds(100, 100, 440, 340);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(30, 25, 110, 16);
		contentPanel.add(lblNombre);

		textNombre = new JTextField();
		textNombre.setBounds(150, 22, 240, 25);
		contentPanel.add(textNombre);

		JLabel lblDescripcion = new JLabel("Descripcion:");
		lblDescripcion.setBounds(30, 70, 110, 16);
		contentPanel.add(lblDescripcion);

		textDescripcion = new JTextField();
		textDescripcion.setBounds(150, 67, 240, 25);
		contentPanel.add(textDescripcion);

		JLabel lblCategoria = new JLabel("Categoria:");
		lblCategoria.setBounds(30, 115, 110, 16);
		contentPanel.add(lblCategoria);

		String[] categorias = { "Hematologia", "Quimica", "Microbiologia", "Imagenologia", "Uroanalisis", "Otro" };
		comboCategoria = new JComboBox<>(new DefaultComboBoxModel<>(categorias));
		comboCategoria.setBounds(150, 112, 240, 25);
		contentPanel.add(comboCategoria);

		JLabel lblPrecio = new JLabel("Precio Base (RD$):");
		lblPrecio.setBounds(30, 160, 120, 16);
		contentPanel.add(lblPrecio);

		textPrecio = new JTextField();
		textPrecio.setBounds(150, 157, 240, 25);
		contentPanel.add(textPrecio);

		if (examen != null) {
			textNombre.setText(examen.getNombre());
			textDescripcion.setText(examen.getDescripcion());
			comboCategoria.setSelectedItem(examen.getCategoria());
			textPrecio.setText(String.valueOf(examen.getPrecioBase()));
		}

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnGuardar = new JButton("Guardar");
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nombre = textNombre.getText().trim();
				String descripcion = textDescripcion.getText().trim();
				String categoria = (String) comboCategoria.getSelectedItem();
				String precioStr = textPrecio.getText().trim();

				if (nombre.isEmpty()) {
					JOptionPane.showMessageDialog(AgregarExamen.this, "El nombre es obligatorio.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				float precio;
				try {
					precio = Float.parseFloat(precioStr);
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(AgregarExamen.this, "Ingrese un precio valido.", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				ExamenLaboratorio ex = new ExamenLaboratorio("0", nombre, descripcion, categoria, precio);
				if (examenEditar == null) {
					if (dao.ExamenLaboratorioDAO.registrarExamen(ex)) {
						JOptionPane.showMessageDialog(AgregarExamen.this, "Examen agregado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarExamen.this, "Error guardando en BD.", "Error",
								JOptionPane.ERROR_MESSAGE);
					}
				} else {
					ex.setId(examenEditar.getId());
					if (dao.ExamenLaboratorioDAO.actualizarExamen(ex)) {
						JOptionPane.showMessageDialog(AgregarExamen.this, "Examen actualizado exitosamente.");
					} else {
						JOptionPane.showMessageDialog(AgregarExamen.this, "Error guardando en BD.", "Error",
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
