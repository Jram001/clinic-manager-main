package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;
import com.toedter.calendar.JDateChooser;

import logico.Cita;
import logico.Clinica;

public class PosponerCita extends JDialog {

	Clinica instancia = Clinica.getInstancia();
	static String citaId;
	private final JPanel contentPanel = new JPanel();
	private JDateChooser dateChooserFecha;
	private LocalDate hoy = LocalDate.now();
	private java.util.Date fechaInicial = Date.from(hoy.atStartOfDay(ZoneId.systemDefault()).toInstant());

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			PosponerCita dialog = new PosponerCita(citaId);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PosponerCita(String id) {
		setModal(true);
		setTitle("Posponer Cita");
		setBounds(100, 100, 405, 236);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new GridLayout(0, 1, 0, 0));
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel);
			panel.setLayout(null);
			{
				JLabel lbFecha = new JLabel("Fecha");
				lbFecha.setBounds(77, 52, 56, 16);
				panel.add(lbFecha);
			}
			{
				dateChooserFecha = new JDateChooser(fechaInicial);
				dateChooserFecha.setDateFormatString("dd/MM/yyyy");
				dateChooserFecha.setBounds(75, 76, 230, 20);
				panel.add(dateChooserFecha);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						java.util.Date date = dateChooserFecha.getDate();
						if (date == null) {
							JOptionPane.showMessageDialog(PosponerCita.this, "Debe seleccionar una fecha", "Alerta",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
						java.time.LocalDate localDate = date.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDate();

						Cita cita = dao.CitaDAO.buscarCitaPorId(id);

						if (cita == null) {
							JOptionPane.showMessageDialog(PosponerCita.this, "Cita no encontrada", "Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}

						if (localDate.equals(cita.getFecha())) {
							JOptionPane.showMessageDialog(PosponerCita.this, "La fecha no puede ser el mismo dia",
									"Alerta", JOptionPane.ERROR_MESSAGE);
						}

						else if (localDate.isBefore(hoy)) {
							JOptionPane.showMessageDialog(PosponerCita.this, "La fecha no puede antes que hoy",
									"Alerta", JOptionPane.ERROR_MESSAGE);
						}

						else if (instancia.medicoPuedeAceptarCita(cita.getId_medico(), localDate)) {
							dao.CitaDAO.posponerCita(id, localDate);
							JOptionPane.showMessageDialog(PosponerCita.this, "Cita fue cambiada para: " + localDate,
									"Alerta", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						} else {
							JOptionPane.showMessageDialog(PosponerCita.this, "El doctor no puede en esa fecha",
									"Alerta", JOptionPane.ERROR_MESSAGE);
						}

					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}

}
