package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Receta implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private Consulta consulta;
	private LocalDate fecha;
	private String observaciones;
	private ArrayList<DetalleReceta> detalles;

	public Receta(String id, Consulta consulta, LocalDate fecha, String observaciones) {
		this.id = id;
		this.consulta = consulta;
		this.fecha = fecha;
		this.observaciones = observaciones;
		this.detalles = new ArrayList<>();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Paciente getPaciente() {
		return this.consulta != null ? this.consulta.getPaciente() : null;
	}

	public Medico getMedico() {
		return this.consulta != null ? this.consulta.getMedico() : null;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public ArrayList<DetalleReceta> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleReceta> detalles) {
		this.detalles = detalles;
	}

	public void agregarDetalle(DetalleReceta detalle) {
		detalles.add(detalle);
	}
}
