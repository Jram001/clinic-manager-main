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
import logico.SeguroMedico;

public class ListaSeguros extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel model;
	private JComboBox<String> comboFiltro;

	public static void main(String[] args) {
		try {
			ListaSeguros dialog = new ListaSeguros();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ListaSeguros() {
		setModal(true);
		setResizable(false);
		setTitle("Lista de Seguros Medicos");
		setBounds(100, 100, 650, 400);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		String[] columnas = { "ID", "Nombre", "RNC", "Telefono", "Cobertura %", "Activo" };
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
				AgregarSeguro dialog = new AgregarSeguro(null);
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
					JOptionPane.showMessageDialog(ListaSeguros.this, "Seleccione un seguro.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				SeguroMedico seguro = null;
				for (SeguroMedico s : dao.SeguroMedicoDAO.listarSeguros()) {
					if (s.getId().equals(id)) {
						seguro = s;
						break;
					}
				}

				if (seguro != null) {
					AgregarSeguro dialog = new AgregarSeguro(seguro);
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
					JOptionPane.showMessageDialog(ListaSeguros.this, "Seleccione un seguro.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreSeguro = (String) model.getValueAt(fila, 1);
				String activoText = (String) model.getValueAt(fila, 5);

				boolean isActivo = "Si".equals(activoText);
				String accion = isActivo ? "desactivar" : "activar";

				int confirm = JOptionPane.showConfirmDialog(ListaSeguros.this,
						"Desea " + accion + " el seguro " + nombreSeguro + "?",
						"Confirmar", JOptionPane.YES_NO_OPTION);

				if (confirm == JOptionPane.YES_OPTION) {
					if (isActivo) {
						dao.SeguroMedicoDAO.eliminarSeguro(id);
					} else {
						dao.SeguroMedicoDAO.activarSeguro(id);
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
					JOptionPane.showMessageDialog(ListaSeguros.this, "Seleccione un seguro para borrar de la BD.");
					return;
				}
				String id = (String) model.getValueAt(fila, 0);
				String nombreSeguro = (String) model.getValueAt(fila, 1);
				int confirm = JOptionPane.showConfirmDialog(ListaSeguros.this,
						"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE el seguro " + nombreSeguro
								+ " de la Base de Datos?",
						"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
				if (confirm == JOptionPane.YES_OPTION) {
					dao.SeguroMedicoDAO.borrarSeguroDefinitivo(id);
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

		for (SeguroMedico s : dao.SeguroMedicoDAO.listarSeguros()) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && s.isEsActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !s.isEsActivo()) {
				cumple = true;
			}

			if (cumple) {
				model.addRow(new Object[] {
						s.getId(),
						s.getNombre(),
						s.getRnc(),
						s.getTelefono(),
						s.getPorcentajeCobertura(),
						s.isEsActivo() ? "Si" : "No"
				});
			}
		}
	}
}
