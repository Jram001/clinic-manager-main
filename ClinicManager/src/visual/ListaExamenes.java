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
import logico.ExamenLaboratorio;

public class ListaExamenes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> comboFiltro;

	public static void main(String[] args) {
		try {
			ListaExamenes dialog = new ListaExamenes();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListaExamenes() {
		setModal(true);
		setResizable(false);
		setTitle("Lista de Examenes de Laboratorio");
		setBounds(100, 100, 700, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		String[] columnas = { "ID", "Nombre", "Descripcion", "Categoria", "Precio Base", "Activo" };
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
				AgregarExamen dialog = new AgregarExamen(null);
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
					JOptionPane.showMessageDialog(ListaExamenes.this, "Seleccione un examen.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				ExamenLaboratorio examen = null;
				for (ExamenLaboratorio eLab : dao.ExamenLaboratorioDAO.listarExamenes()) {
					if (eLab.getId().equals(id)) {
						examen = eLab;
						break;
					}
				}
				if (examen != null) {
					AgregarExamen dialog = new AgregarExamen(examen);
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
					String activoText = (String) model.getValueAt(fila, 5);
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
					JOptionPane.showMessageDialog(ListaExamenes.this, "Seleccione un examen.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreExamen = (String) model.getValueAt(fila, 1);
				String activoText = (String) model.getValueAt(fila, 5);

				boolean isActivo = "Si".equals(activoText);
				String accion = isActivo ? "desactivar" : "activar";

				int confirm = JOptionPane.showConfirmDialog(ListaExamenes.this,
						"Desea " + accion + " el examen " + nombreExamen + "?",
						"Confirmar", JOptionPane.YES_NO_OPTION);
				if (confirm == JOptionPane.YES_OPTION) {
					if (isActivo) {
						dao.ExamenLaboratorioDAO.eliminarExamen(id);
					} else {
						dao.ExamenLaboratorioDAO.activarExamen(id);
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
					JOptionPane.showMessageDialog(ListaExamenes.this, "Seleccione un examen para borrar de la BD.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreExamen = (String) model.getValueAt(fila, 1);
				int confirm = JOptionPane.showConfirmDialog(ListaExamenes.this,
						"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE el examen " + nombreExamen
								+ " de la Base de Datos?",
						"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					dao.ExamenLaboratorioDAO.borrarExamenDefinitivo(id);
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

		for (ExamenLaboratorio eLab : dao.ExamenLaboratorioDAO.listarExamenes()) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && eLab.isEsActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !eLab.isEsActivo()) {
				cumple = true;
			}

			if (cumple) {
				model.addRow(new Object[] {
						eLab.getId(),
						eLab.getNombre(),
						eLab.getDescripcion(),
						eLab.getCategoria(),
						String.format("%.2f", eLab.getPrecioBase()),
						eLab.isEsActivo() ? "Si" : "No"
				});
			}
		}
	}
}
