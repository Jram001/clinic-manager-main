package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class PacienteVacuna implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private Paciente paciente;
	private Vacuna vacuna;
	private LocalDate fechaAplicacion;
	private String lote;
	private int numeroDosis;

	public PacienteVacuna(String id, Paciente paciente, Vacuna vacuna, LocalDate fechaAplicacion, String lote, int numeroDosis) {
		this.id = id;
		this.paciente = paciente;
		this.vacuna = vacuna;
		this.fechaAplicacion = fechaAplicacion;
		this.lote = lote;
		this.numeroDosis = numeroDosis;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public void setPaciente(Paciente paciente) {
		this.paciente = paciente;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

	public LocalDate getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(LocalDate fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public String getLote() {
		return lote;
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public int getNumeroDosis() {
		return numeroDosis;
	}

	public void setNumeroDosis(int numeroDosis) {
		this.numeroDosis = numeroDosis;
	}
}
