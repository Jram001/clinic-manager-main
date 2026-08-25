package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import logico.Clinica;
import logico.Medico;
import logico.Paciente;

public class CrearCita extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtBuscarPaciente;
	private JComboBox<Paciente> cbPacientes;
	private JComboBox<Medico> comboBoxDoctor;
	private JDateChooser dateChooserFecha;
	Clinica instancia = Clinica.getInstancia();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			CrearCita dialog = new CrearCita();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CrearCita() {
		setResizable(false);
		setModal(true);
		setTitle("Crear Cita");
		setBounds(100, 100, 444, 433);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));
		LocalDate hoy = LocalDate.now();
		Date fechaInicial = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_130227297469400");
			panel.setLayout(null);
			{
				JLabel labelFecha = new JLabel("Fecha");
				labelFecha.setBounds(79, 205, 56, 16);
				panel.add(labelFecha);
			}

			comboBoxDoctor = new JComboBox<Medico>();
			comboBoxDoctor.setBounds(79, 277, 274, 22);

			comboBoxDoctor.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Medico) {
						Medico m = (Medico) value;
						setText(m.getNombre() + " " + m.getApellido() + " -" + m.getEspecialidad());
					} else {
						setText("");
					}
					return this;
				}
			});

			panel.add(comboBoxDoctor);

			dateChooserFecha = new JDateChooser(fechaInicial);
			dateChooserFecha.setDateFormatString("dd/MM/yyyy");
			dateChooserFecha.setBounds(79, 221, 274, 22);
			dateChooserFecha.addPropertyChangeListener("date", new PropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent evt) {
					java.util.Date date = dateChooserFecha.getDate();
					if (date != null) {
						java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDate();
						actualizarComboMedicos(localDate);
					}
				}
			});
			panel.add(dateChooserFecha);
			{
				JLabel lbDoctor = new JLabel("Doctor");
				lbDoctor.setBounds(79, 260, 56, 16);
				panel.add(lbDoctor);
			}

			JLabel lbBuscarPaciente = new JLabel("Buscar Paciente");
			lbBuscarPaciente.setBounds(79, 48, 116, 16);
			panel.add(lbBuscarPaciente);

			txtBuscarPaciente = new JTextField();
			txtBuscarPaciente.setBounds(79, 64, 275, 22);
			panel.add(txtBuscarPaciente);
			txtBuscarPaciente.setColumns(10);

			cbPacientes = new JComboBox<Paciente>();
			cbPacientes.setBounds(79, 99, 225, 22);

			JButton btnNuevoPaciente = new JButton("+");
			btnNuevoPaciente.setBounds(309, 99, 45, 22);
			btnNuevoPaciente.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					String buscarTexto = txtBuscarPaciente.getText().trim();
					AgregarPaciente dialog = new AgregarPaciente(null, buscarTexto.matches("\\d+") ? buscarTexto : "");
					dialog.setModal(true);
					dialog.setLocationRelativeTo(CrearCita.this);
					dialog.setVisible(true);

					DefaultComboBoxModel<Paciente> modelo = new DefaultComboBoxModel<>();
					for (Paciente p : dao.PacienteDAO.listarPacientes()) {
						modelo.addElement(p);
					}
					cbPacientes.setModel(modelo);

					String newId = dialog.getCreatedPacienteId();
					if (newId != null) {
						for (int i = 0; i < cbPacientes.getItemCount(); i++) {
							Paciente p = cbPacientes.getItemAt(i);
							if (p.getId().equals(newId)) {
								cbPacientes.setSelectedIndex(i);
								break;
							}
						}
					}
				}
			});
			panel.add(btnNuevoPaciente);
			cbPacientes.setRenderer(new DefaultListCellRenderer() {
				@Override
				public Component getListCellRendererComponent(JList<?> list, Object value, int index,
						boolean isSelected, boolean cellHasFocus) {
					super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
					if (value instanceof Paciente) {
						Paciente p = (Paciente) value;
						setText(p.getCedula() + " - " + p.getNombre() + " " + p.getApellido());
					} else {
						setText("");
					}
					return this;
				}
			});
			panel.add(cbPacientes);

			txtBuscarPaciente.addKeyListener(new KeyAdapter() {
				@Override
				public void keyReleased(KeyEvent e) {
					String buscar = txtBuscarPaciente.getText().toLowerCase();
					DefaultComboBoxModel<Paciente> modelo = new DefaultComboBoxModel<>();
					for (Paciente p : dao.PacienteDAO.listarPacientes()) {
						if (p.getNombre().toLowerCase().contains(buscar) ||
								p.getApellido().toLowerCase().contains(buscar) ||
								p.getCedula().toLowerCase().contains(buscar)) {
							modelo.addElement(p);
						}
					}
					cbPacientes.setModel(modelo);
					if (modelo.getSize() > 0) {
						cbPacientes.setSelectedIndex(0);
					}
				}
			});

			DefaultComboBoxModel<Paciente> modeloInicial = new DefaultComboBoxModel<>();
			for (Paciente p : dao.PacienteDAO.listarPacientes()) {
				modeloInicial.addElement(p);
			}
			cbPacientes.setModel(modeloInicial);

			actualizarComboMedicos(hoy);

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton btnAgregar = new JButton("Agregar");
				btnAgregar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						Paciente paciente = (Paciente) cbPacientes.getSelectedItem();
						Medico medico = (Medico) comboBoxDoctor.getSelectedItem();
						java.util.Date tmpfecha = dateChooserFecha.getDate();

						LocalDate fecha = null;
						if (tmpfecha != null) {
							fecha = tmpfecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
						}

						if (paciente == null || medico == null
								|| fecha == null ||
								!fecha.isAfter(hoy.minusDays(1))) {

							if (fecha != null && !fecha.isAfter(hoy.minusDays(1))) {
								JOptionPane.showMessageDialog(CrearCita.this, "Fecha no puede ser antes de hoy",
										"Alerta", JOptionPane.ERROR_MESSAGE);
							} else {
								JOptionPane.showMessageDialog(CrearCita.this, "Hay Campos Vacios o Invalidos", "Alerta",
										JOptionPane.ERROR_MESSAGE);
							}
						}

						else {
							instancia.agregarCita(paciente, medico, fecha);
							JOptionPane.showMessageDialog(CrearCita.this, "Cita creada para: " + fecha.toString(),
									"Informacion", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}

					}
				});
				btnAgregar.setActionCommand("OK");
				buttonPane.add(btnAgregar);
				getRootPane().setDefaultButton(btnAgregar);
			}
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
		}
	}

	private void actualizarComboMedicos(LocalDate fecha) {
		ArrayList<Medico> medicosDisponibles = instancia.getMedicosDisponibles(fecha);
		DefaultComboBoxModel<Medico> model = new DefaultComboBoxModel<>();
		if (medicosDisponibles != null) {
			for (Medico m : medicosDisponibles) {
				if (m != null && m.isActivo()) {
					model.addElement(m);
				}
			}
		}
		comboBoxDoctor.setModel(model);

	}
}
