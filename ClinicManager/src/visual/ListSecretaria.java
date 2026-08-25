package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

public class ListSecretaria extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableVacunas;
	DefaultTableModel modelSecretaria;
	private JComboBox<String> comboFiltro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			ListSecretaria dialog = new ListSecretaria();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListSecretaria() {
		setResizable(false);
		setModal(true);
		setTitle("Lista de Secretarias");
		setBounds(100, 100, 898, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel headerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPane.add(new javax.swing.JLabel("Mostrar: "));
		comboFiltro = new JComboBox<>(new String[] { "Todos", "Activos", "Desactivados" });
		comboFiltro.addActionListener(e -> cargarTablaSecretaria());
		headerPane.add(comboFiltro);
		contentPanel.add(headerPane, BorderLayout.NORTH);

		String[] columnas = { "ID", "Nombre", "Turno", "Salario (RD$)", "Extensión", "Activo" };
		modelSecretaria = new DefaultTableModel(columnas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false; // read-only
			}
		};
		tableVacunas = new JTable(modelSecretaria);
		tableVacunas.setFillsViewportHeight(true);
		JScrollPane scrollVacunas = new JScrollPane(tableVacunas);
		scrollVacunas.setPreferredSize(new Dimension(860, 260)); // ajusta segun tu layout
		contentPanel.add(scrollVacunas, BorderLayout.CENTER);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
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
			JButton btnDesactivar = new JButton("Desactivar");
			tableVacunas.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int fila = tableVacunas.getSelectedRow();
					if (fila >= 0) {
						String activoText = (String) modelSecretaria.getValueAt(fila, 5);
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
					int fila = tableVacunas.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListSecretaria.this, "No hay nada Seleccionado", "Alerta",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					String id = String.valueOf(tableVacunas.getModel().getValueAt(fila, 0));
					String nombreSec = String.valueOf(tableVacunas.getModel().getValueAt(fila, 1));
					String activoText = String.valueOf(tableVacunas.getModel().getValueAt(fila, 5));

					boolean isActivo = "Si".equals(activoText);
					String accion = isActivo ? "desactivar" : "activar";

					int confirm = JOptionPane.showConfirmDialog(ListSecretaria.this,
							"Desea " + accion + " la secretaria " + nombreSec + "?",
							"Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (isActivo) {
							dao.SecretariaDAO.eliminarSecretaria(id);
						} else {
							dao.SecretariaDAO.activarSecretaria(id);
						}
						cargarTablaSecretaria();
					}
				}
			});
			buttonPane.add(btnDesactivar);

			JButton btnBorrarDef = new JButton("Eliminar Definitivo");
			btnBorrarDef.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int fila = tableVacunas.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListSecretaria.this,
								"Seleccione una secretaria para borrar de la BD.");
						return;
					}
					String id = String.valueOf(tableVacunas.getModel().getValueAt(fila, 0));
					String nombreSec = String.valueOf(tableVacunas.getModel().getValueAt(fila, 1));
					int confirm = JOptionPane.showConfirmDialog(ListSecretaria.this,
							"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE a " + nombreSec
									+ " y su respectivo Login de la Base de Datos?",
							"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirm == JOptionPane.YES_OPTION) {
						if (dao.SecretariaDAO.borrarSecretariaDefinitivo(id)) {
							JOptionPane.showMessageDialog(ListSecretaria.this, "Eliminado exitosamente.");
						} else {
							JOptionPane.showMessageDialog(ListSecretaria.this,
									"Error eliminando Secretaria de Base de Datos.");
						}
						cargarTablaSecretaria();
					}
				}
			});
			buttonPane.add(btnBorrarDef);
			{
				JButton btnAgregar = new JButton("Agregar");
				btnAgregar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AgregarSecretaria pantallaAgregarSecretaria = new AgregarSecretaria();
						pantallaAgregarSecretaria.setLocationRelativeTo(ListSecretaria.this);
						pantallaAgregarSecretaria.setVisible(true);
						cargarTablaSecretaria();
					}
				});
				btnAgregar.setActionCommand("OK");
				buttonPane.add(btnAgregar);
				getRootPane().setDefaultButton(btnAgregar);
			}
		}
	}

	public void cargarTablaSecretaria() {
		modelSecretaria.setRowCount(0);
		String filtro = comboFiltro != null ? (String) comboFiltro.getSelectedItem() : "Todos";

		for (logico.Secretaria s : dao.SecretariaDAO.listarSecretarias()) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && s.isActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !s.isActivo()) {
				cumple = true;
			}

			if (cumple) {
				modelSecretaria.addRow(new Object[] {
						s.getId(),
						s.getNombre() + " " + s.getApellido(),
						s.getTurno(),
						s.getSalario(),
						s.getExtensionTelefonica(),
						s.isActivo() ? "Si" : "No"
				});
			}
		}
	}
}
