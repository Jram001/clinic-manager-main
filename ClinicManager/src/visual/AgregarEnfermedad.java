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

public class AgregarEnfermedad extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textNombre;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(new FlatLightLaf());
			AgregarEnfermedad dialog = new AgregarEnfermedad();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AgregarEnfermedad() {
		setResizable(false);
		setModal(true);
		setTitle("Agregar Enfermedad Bajo Vigilancia");
		setBounds(100, 100, 456, 369);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new CardLayout(0, 0));

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
				textDescripcion.setLineWrap(true);
				textDescripcion.setWrapStyleWord(true);
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
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Agregar");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						String nombre = textNombre.getText().trim();
						String descripcion = textDescripcion.getText().trim();
						String gravedad = comboBoxGravedad.getSelectedItem().toString();

						if (nombre.isEmpty() || descripcion.isEmpty() || gravedad.isEmpty()) {
							JOptionPane.showMessageDialog(AgregarEnfermedad.this, "Hay Campos Faltantes", "Alerta",
									JOptionPane.ERROR_MESSAGE);
						} else {
							logico.EnfermedadBajoVigilancia enf = new logico.EnfermedadBajoVigilancia(
									java.util.UUID.randomUUID().toString(), nombre, descripcion, gravedad);
							dao.EnfermedadDAO.registrarEnfermedad(enf);
							JOptionPane.showMessageDialog(AgregarEnfermedad.this,
									"Enfermedad : " + nombre + " Fue Creada", "Informacion",
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

}
