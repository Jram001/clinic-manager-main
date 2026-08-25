package visual;

import java.awt.BorderLayout;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import dao.ReportesDAO;

public class Reportes extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();

	public static void main(String[] args) {
		try {
			Reportes dialog = new Reportes();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Reportes() {
		setTitle("Reportes Multi-Tabla");
		setBounds(100, 100, 1100, 680);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(8, 8, 8, 8));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPanel.add(tabbedPane, BorderLayout.CENTER);

		// Tab 1: Agenda y Ocupación
		JTable tableAgenda = new JTable();
		tableAgenda.setModel(ReportesDAO.obtenerOcupacionAgenda());
		tableAgenda.setRowHeight(25);
		JScrollPane scrollAgenda = new JScrollPane(tableAgenda);
		tabbedPane.addTab("Agenda y Ocupación", null, scrollAgenda, null);

		// Tab 2: Facturación Mensual
		JTable tableFacturacion = new JTable();
		tableFacturacion.setModel(ReportesDAO.obtenerFacturacionMes());
		tableFacturacion.setRowHeight(25);
		JScrollPane scrollFacturacion = new JScrollPane(tableFacturacion);
		tabbedPane.addTab("Facturación Mensual", null, scrollFacturacion, null);

		// Tab 3: Vigilancia Epidemiológica
		JTable tableVigilancia = new JTable();
		tableVigilancia.setModel(ReportesDAO.obtenerVigilanciaEpidemiologica());
		tableVigilancia.setRowHeight(25);
		JScrollPane scrollVigilancia = new JScrollPane(tableVigilancia);
		tabbedPane.addTab("Vigilancia Epidemiológica", null, scrollVigilancia, null);
	}
}