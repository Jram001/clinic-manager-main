package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.JComboBox;

public class ListaEnfermedades extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableEnfermedades;
	DefaultTableModel modelVacunas;
	private JComboBox<String> comboFiltro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			ListaEnfermedades dialog = new ListaEnfermedades();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListaEnfermedades() {
		setModal(true);
		setResizable(false);
		setTitle("Enfermedades Bajo Vigilancia");
		setBounds(100, 100, 898, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel headerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPane.add(new javax.swing.JLabel("Mostrar: "));
		comboFiltro = new JComboBox<>(new String[] { "Todos", "Activos", "Desactivados" });
		comboFiltro.addActionListener(e -> cargarTablaEnfermedades());
		headerPane.add(comboFiltro);
		contentPanel.add(headerPane, BorderLayout.NORTH);

		String[] columnaVacunas = { "ID", "Nombre", "Gravedad", "Descripcion", "Activo" };
		modelVacunas = new DefaultTableModel(columnaVacunas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableEnfermedades = new JTable(modelVacunas);
		tableEnfermedades.setFillsViewportHeight(true);
		JScrollPane scrollEnfermedades = new JScrollPane(tableEnfermedades);
		scrollEnfermedades.setPreferredSize(new Dimension(860, 260)); // ajusta segun tu layout
		contentPanel.add(scrollEnfermedades, BorderLayout.CENTER);
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
			tableEnfermedades.getSelectionModel().addListSelectionListener(e -> {
				if (!e.getValueIsAdjusting()) {
					int fila = tableEnfermedades.getSelectedRow();
					if (fila >= 0) {
						String activoText = (String) modelVacunas.getValueAt(fila, 4);
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
					int fila = tableEnfermedades.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListaEnfermedades.this, "No hay nada Seleccionado", "Alerta",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					String id = String.valueOf(tableEnfermedades.getModel().getValueAt(fila, 0));
					String nombreEnf = String.valueOf(tableEnfermedades.getModel().getValueAt(fila, 1));
					String activoText = String.valueOf(tableEnfermedades.getModel().getValueAt(fila, 4));

					boolean isActivo = "Si".equals(activoText);
					String accion = isActivo ? "desactivar" : "activar";

					int confirm = JOptionPane.showConfirmDialog(ListaEnfermedades.this,
							"Desea " + accion + " la enfermedad " + nombreEnf + "?",
							"Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (isActivo) {
							dao.EnfermedadDAO.eliminarEnfermedad(id);
						} else {
							dao.EnfermedadDAO.activarEnfermedad(id);
						}
						cargarTablaEnfermedades();
					}
				}
			});
			buttonPane.add(btnDesactivar);

			JButton btnBorrarDef = new JButton("Eliminar Definitivo");
			btnBorrarDef.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int fila = tableEnfermedades.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListaEnfermedades.this,
								"Seleccione una enfermedad para borrar de la BD.");
						return;
					}
					String id = String.valueOf(tableEnfermedades.getModel().getValueAt(fila, 0));
					String nombreEnf = String.valueOf(tableEnfermedades.getModel().getValueAt(fila, 1));
					int confirm = JOptionPane.showConfirmDialog(ListaEnfermedades.this,
							"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE " + nombreEnf + " de la Base de Datos?",
							"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirm == JOptionPane.YES_OPTION) {
						dao.EnfermedadDAO.borrarEnfermedadDefinitivo(id);
						cargarTablaEnfermedades();
					}
				}
			});
			buttonPane.add(btnBorrarDef);
			{
				JButton btnEditar = new JButton("Editar");
				btnEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int seleccionado = tableEnfermedades.getSelectedRow();
						int idCol = 0;
						if (seleccionado == -1) {
							JOptionPane.showMessageDialog(ListaEnfermedades.this, "No hay nada Seleccionado", "Alerta",
									JOptionPane.ERROR_MESSAGE);
						}

						Object idTexto = tableEnfermedades.getModel().getValueAt(seleccionado, idCol);
						String id = String.valueOf(idTexto);
						EditarEnfermedad pantallaAgregarEnfermedad = new EditarEnfermedad(id);
						pantallaAgregarEnfermedad.setLocationRelativeTo(ListaEnfermedades.this);
						pantallaAgregarEnfermedad.setVisible(true);
						cargarTablaEnfermedades();
					}
				});
				buttonPane.add(btnEditar);
			}
			{
				JButton btnAgregar = new JButton("Agregar");
				btnAgregar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AgregarEnfermedad pantallaAgregarEnfermedad = new AgregarEnfermedad();
						pantallaAgregarEnfermedad.setLocationRelativeTo(ListaEnfermedades.this);
						pantallaAgregarEnfermedad.setVisible(true);
						cargarTablaEnfermedades();
					}
				});
				btnAgregar.setActionCommand("OK");
				buttonPane.add(btnAgregar);
				getRootPane().setDefaultButton(btnAgregar);
			}
		}
	}

	public void cargarTablaEnfermedades() {
		modelVacunas.setRowCount(0);
		String filtro = comboFiltro != null ? (String) comboFiltro.getSelectedItem() : "Todos";

		ArrayList<logico.EnfermedadBajoVigilancia> enfermedadesVigiladas = dao.EnfermedadDAO.listarEnfermedades();

		for (logico.EnfermedadBajoVigilancia e : enfermedadesVigiladas) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && e.isEsActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !e.isEsActivo()) {
				cumple = true;
			}

			if (cumple) {
				modelVacunas.addRow(new Object[] {
						e.getId(),
						e.getNombre(),
						e.getGravedad(),
						e.getDescripcion(),
						e.isEsActivo() ? "Si" : "No"
				});
			}
		}
	}

}
