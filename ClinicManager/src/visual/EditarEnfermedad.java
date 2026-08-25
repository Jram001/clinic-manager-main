package visual;

import java.awt.BorderLayout;
import java.awt.CardLayout;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import logico.Clinica;
import logico.EnfermedadBajoVigilancia;

public class EditarEnfermedad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;
	static String enfermedadId;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			EditarEnfermedad dialog = new EditarEnfermedad(enfermedadId);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditarEnfermedad(String enfermedadId) {
		setResizable(false);
		setModal(true);
		setTitle("Agregar Enfermedad Bajo Vigilancia");
		setBounds(100, 100, 456, 369);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

		Clinica instancia = Clinica.getInstancia();
		EnfermedadBajoVigilancia enfermedad = instancia.buscarEnfermedadPorId(enfermedadId);
		JTextArea textDescripcion;
		JComboBox<String> comboBoxGravedad;
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, "name_102872891702400");
			panel.setLayout(null);
			{
				JLabel lblNombre = new JLabel("Nombre");
				lblNombre.setBounds(77, 38, 56, 16);
				panel.add(lblNombre);
			}
			{
				textNombre = new JTextField();
				textNombre.setBounds(77, 58, 280, 22);
				panel.add(textNombre);
				textNombre.setColumns(10);
			}
			{
				JLabel lbDescripcion = new JLabel("Descripcion");
				lbDescripcion.setBounds(76, 148, 90, 16);
				panel.add(lbDescripcion);
			}
			{
				textDescripcion = new JTextArea();
				textDescripcion.setBounds(76, 165, 285, 64);
				panel.add(textDescripcion);
			}
			{
				JLabel lbGravedad = new JLabel("Gravedad");
				lbGravedad.setBounds(79, 92, 56, 16);
				panel.add(lbGravedad);
			}
			{
				comboBoxGravedad = new JComboBox<>();
				comboBoxGravedad.setBounds(79, 112, 282, 22);
				panel.add(comboBoxGravedad);
				String[] gravedad = { "Baja", "Media", "Alta" };
				comboBoxGravedad.setModel(new DefaultComboBoxModel<>(gravedad));
			}
			textNombre.setText(enfermedad.getNombre());
			textDescripcion.setText(enfermedad.getDescripcion());
			selectStringItem(comboBoxGravedad, enfermedad.getGravedad());

		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						EnfermedadBajoVigilancia enfermedad = instancia.buscarEnfermedadPorId(enfermedadId);

						String nombre = textNombre.getText().trim();
						String descripcion = textDescripcion.getText().trim();
						String gravedad = comboBoxGravedad.getSelectedItem().toString();

						if (nombre.isEmpty() || descripcion.isEmpty() || gravedad.isEmpty()) {
							JOptionPane.showMessageDialog(EditarEnfermedad.this, "Hay Campos Faltantes", "Alerta",
									JOptionPane.ERROR_MESSAGE);
						} else {
							enfermedad.setDescripcion(descripcion);
							enfermedad.setNombre(nombre);
							enfermedad.setGravedad(gravedad);
							JOptionPane.showMessageDialog(EditarEnfermedad.this, "Cambios fueron hechos", "Informacion",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
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

	public static void selectStringItem(JComboBox<String> comboBox, String item) {
		if (item == null || comboBox == null)
			return;
		comboBox.setSelectedItem(item.trim());
	}
}
