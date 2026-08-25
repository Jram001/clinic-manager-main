package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class OrdenExamen implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private Consulta consulta;
	private ExamenLaboratorio examen;
	private LocalDate fechaOrden;
	private LocalDate fechaResultado;
	private String resultado;
	private String estado;

	public OrdenExamen(String id, Consulta consulta, ExamenLaboratorio examen, LocalDate fechaOrden) {
		this.id = id;
		this.consulta = consulta;
		this.examen = examen;
		this.fechaOrden = fechaOrden;
		this.fechaResultado = null;
		this.resultado = "";
		this.estado = "pendiente";
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

	public ExamenLaboratorio getExamen() {
		return examen;
	}

	public void setExamen(ExamenLaboratorio examen) {
		this.examen = examen;
	}

	public LocalDate getFechaOrden() {
		return fechaOrden;
	}

	public void setFechaOrden(LocalDate fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	public LocalDate getFechaResultado() {
		return fechaResultado;
	}

	public void setFechaResultado(LocalDate fechaResultado) {
		this.fechaResultado = fechaResultado;
	}

	public String getResultado() {
		return resultado;
	}

	public void setResultado(String resultado) {
		this.resultado = resultado;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
}
