package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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

import logico.Vacuna;

public class ListaVacunas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableVacunas;
	DefaultTableModel modelVacunas;
	private JComboBox<String> comboFiltro;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			ListaVacunas dialog = new ListaVacunas();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ListaVacunas() {
		setResizable(false);
		setModal(true);
		setTitle("Vacunas");
		setBounds(100, 100, 898, 350);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JPanel headerPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
		headerPane.add(new javax.swing.JLabel("Mostrar: "));
		comboFiltro = new JComboBox<>(new String[] { "Todos", "Activos", "Desactivados" });
		comboFiltro.addActionListener(e -> cargarTablaVacunas());
		headerPane.add(comboFiltro);
		contentPanel.add(headerPane, BorderLayout.NORTH);

		String[] columnaVacunas = { "ID", "Nombre", "Fabricante", "Descripcion", "Activo" };
		modelVacunas = new DefaultTableModel(columnaVacunas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		tableVacunas = new JTable(modelVacunas);
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
					int fila = tableVacunas.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListaVacunas.this, "No hay nada Seleccionado", "Alerta",
								JOptionPane.ERROR_MESSAGE);
						return;
					}

					String id = String.valueOf(tableVacunas.getModel().getValueAt(fila, 0));
					String nombreVacuna = String.valueOf(tableVacunas.getModel().getValueAt(fila, 1));
					String activoText = String.valueOf(tableVacunas.getModel().getValueAt(fila, 4));

					boolean isActivo = "Si".equals(activoText);
					String accion = isActivo ? "desactivar" : "activar";

					int confirm = JOptionPane.showConfirmDialog(ListaVacunas.this,
							"Desea " + accion + " la vacuna " + nombreVacuna + "?",
							"Confirmar", JOptionPane.YES_NO_OPTION);
					if (confirm == JOptionPane.YES_OPTION) {
						if (isActivo) {
							dao.VacunaDAO.eliminarVacuna(id);
						} else {
							dao.VacunaDAO.activarVacuna(id);
						}
						cargarTablaVacunas();
					}
				}
			});
			buttonPane.add(btnDesactivar);

			JButton btnBorrarDef = new JButton("Eliminar Definitivo");
			btnBorrarDef.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int fila = tableVacunas.getSelectedRow();
					if (fila < 0) {
						JOptionPane.showMessageDialog(ListaVacunas.this, "Seleccione una vacuna para borrar de la BD.");
						return;
					}
					String id = String.valueOf(tableVacunas.getModel().getValueAt(fila, 0));
					String nombreVacuna = String.valueOf(tableVacunas.getModel().getValueAt(fila, 1));
					int confirm = JOptionPane.showConfirmDialog(ListaVacunas.this,
							"ADVERTENCIA: ¿Desea borrar PERMANENTEMENTE " + nombreVacuna + " de la Base de Datos?",
							"Confirmación Crítica", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
					if (confirm == JOptionPane.YES_OPTION) {
						dao.VacunaDAO.borrarVacunaDefinitivo(id);
						cargarTablaVacunas();
					}
				}
			});
			buttonPane.add(btnBorrarDef);
			{
				JButton btnEditar = new JButton("Editar");
				btnEditar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						int seleccionado = tableVacunas.getSelectedRow();
						int idCol = 0;
						if (seleccionado == -1) {
							JOptionPane.showMessageDialog(ListaVacunas.this, "No hay nada Seleccionado", "Alerta",
									JOptionPane.ERROR_MESSAGE);
						}

						Object idTexto = tableVacunas.getModel().getValueAt(seleccionado, idCol);
						String id = String.valueOf(idTexto);
						EditarVacuna pantallaEditarVacuna = new EditarVacuna(id);
						pantallaEditarVacuna.setLocationRelativeTo(ListaVacunas.this);
						pantallaEditarVacuna.setVisible(true);
						cargarTablaVacunas();

					}
				});
				buttonPane.add(btnEditar);
			}
			{
				JButton btnAgregar = new JButton("Agregar");
				btnAgregar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						AgregarVacuna pantallaAgregarVacuna = new AgregarVacuna();
						pantallaAgregarVacuna.setLocationRelativeTo(ListaVacunas.this);
						pantallaAgregarVacuna.setVisible(true);
						cargarTablaVacunas();
					}
				});
				btnAgregar.setActionCommand("OK");
				buttonPane.add(btnAgregar);
				getRootPane().setDefaultButton(btnAgregar);
			}
		}
	}

	public void cargarTablaVacunas() {
		modelVacunas.setRowCount(0);
		String filtro = comboFiltro != null ? (String) comboFiltro.getSelectedItem() : "Todos";
		ArrayList<Vacuna> vacunas = dao.VacunaDAO.listarVacunas();

		for (Vacuna v : vacunas) {
			boolean cumple = false;
			if ("Todos".equals(filtro)) {
				cumple = true;
			} else if ("Activos".equals(filtro) && v.isEsActivo()) {
				cumple = true;
			} else if ("Desactivados".equals(filtro) && !v.isEsActivo()) {
				cumple = true;
			}

			if (cumple) {
				modelVacunas.addRow(new Object[] {
						v.getId(),
						v.getNombre(),
						v.getFabricante(),
						v.getDescripcion(),
						v.isEsActivo() ? "Si" : "No"
				});
			}
		}
	}
}
