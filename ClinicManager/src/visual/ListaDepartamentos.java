package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import javax.swing.JComboBox;
import logico.Departamento;

public class ListaDepartamentos extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> comboFiltro;

	public static void main(String[] args) {
		try {
			ListaDepartamentos dialog = new ListaDepartamentos();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListaDepartamentos() {
		setModal(true);
		setResizable(false);
		setTitle("Lista de Departamentos");
		setBounds(100, 100, 600, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		String[] columnas = { "ID", "Nombre", "Descripcion", "Ubicacion", "Activo" };
		model = new DefaultTableModel(columnas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table = new JTable(model);
		JScrollPane scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		JPanel headerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPane.add(new javax.swing.JLabel("Mostrar: "));
		comboFiltro = new JComboBox<>(new String[] { "Todos", "Activos", "Desactivados" });
		comboFiltro.addActionListener(e -> cargarDatos());
		headerPane.add(comboFiltro);
		contentPanel.add(headerPane, BorderLayout.NORTH);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnAgregar = new JButton("Agregar");
		btnAgregar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AgregarDepartamento dialog = new AgregarDepartamento(null);
				dialog.setVisible(true);
				cargarDatos();
			}
		});
		buttonPane.add(btnAgregar);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila < 0) {
					JOptionPane.showMessageDialog(ListaDepartamentos.this, "Seleccione un departamento.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				Departamento dep = null;
				for (Departamento d : dao.DepartamentoDAO.listarDepartamentos()) {
					if (d.getId().equals(id)) {
						dep = d;
						break;
					}
				}
				if (dep != null) {
					AgregarDepartamento dialog = new AgregarDepartamento(dep);
					dialog.setVisible(true);
					cargarDatos();
				}
			}
		});
		buttonPane.add(btnEditar);

		JButton btnDesactivar = new JButton("Desactivar");
		table.getSelectionModel().addListSelectionListener(e -> {
			if (!e.getValueIsAdjusting()) {
				int fila = table.getSelectedRow();
				if (fila >= 0) {
					String activoText = (String) model.getValueAt(fila, 4);
					if ("Si".equals(activoText)) {
						btnDesactivar.setText("Desactivar");
					} else {
						btnDesactivar.setText("Activar");
					}
				}
			}
		});

		btnDesactivar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila < 0) {
					JOptionPane.showMessageDialog(ListaDepartamentos.this, "Seleccione un departamento.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreDep = (String) model.getValueAt(fila, 1);
				String activoText = (String) model.getValueAt(fila, 4);

				boolean isActivo = "Si".equals(activoText);
				String accion = isActivo ? "desactivar" : "activar";

				int confirm = JOptionPane.showConfirmDialog(ListaDepartamentos.this,
						"Desea " + accion + " el departamento " + nombreDep + "?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					if (isActivo) {
						dao.DepartamentoDAO.eliminarDepartamento(id);
					} else {
						dao.DepartamentoDAO.activarDepartamento(id);
					}
					cargarDatos();
				}
			}
		});
		buttonPane.add(btnDesactivar);

		JButton btnBorrarDef = new JButton("Eliminar Definitivo");
		btnBorrarDef.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int fila = table.getSelectedRow();
				if (fila < 0) {
					JOptionPane.showMessageDialog(ListaDepartamentos.this,
							"Seleccione un departamento para borrar de la BD.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreDep = (String) model.getValueAt(fila, 1);
				int confirm = JOptionPane.showConfirmDialog(ListaDepartamentos.this,
						"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE el departamento " + nombreDep
								+ " de la Base de Datos?",
						"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					dao.DepartamentoDAO.borrarDepartamentoDefinitivo(id);
					cargarDatos();
				}
			}
		});
		buttonPane.add(btnBorrarDef);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);

		cargarDatos();
	}

	private void cargarDatos() {
		model.setRowCount(0);
		String filtro = comboFiltro != null ? (String) comboFiltro.getSelectedItem() : "Todos";

		for (Departamento d : dao.DepartamentoDAO.listarDepartamentos()) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && d.isEsActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !d.isEsActivo()) {
				cumple = true;
			}

			if (cumple) {
				model.addRow(new Object[] {
						d.getId(),
						d.getNombre(),
						d.getDescripcion(),
						d.getUbicacion(),
						d.isEsActivo() ? "Si" : "No"
				});
			}
		}
	}
}
