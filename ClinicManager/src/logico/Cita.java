package logico;

import java.time.LocalDate;

public class Cita {
	private String id;
	private String id_paciente;
	private String id_medico;
	private LocalDate fecha;
	private boolean esActivo;

	// Support interfaces graphic
	private String nombrePaciente;
	private String apellidoPaciente;
	private String nombreMedico;
	private String apellidoMedico;

	public Cita(String id_paciente, String id_medico, LocalDate fecha) {
		this.id_paciente = id_paciente;
		this.id_medico = id_medico;
		this.fecha = fecha;
		this.esActivo = true;
	}

	public Cita(String id, LocalDate fecha, String nombrePaciente, String apellidoPaciente, String nombreMedico,
			String apellidoMedico) {
		this.id = id;
		this.fecha = fecha;
		this.nombrePaciente = nombrePaciente;
		this.apellidoPaciente = apellidoPaciente;
		this.nombreMedico = nombreMedico;
		this.apellidoMedico = apellidoMedico;
		this.esActivo = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId_paciente() {
		return id_paciente;
	}

	public void setId_paciente(String id_paciente) {
		this.id_paciente = id_paciente;
	}

	public String getId_medico() {
		return id_medico;
	}

	public void setId_medico(String id_medico) {
		this.id_medico = id_medico;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

	public String getNombrePaciente() {
		return nombrePaciente;
	}

	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}

	public String getApellidoPaciente() {
		return apellidoPaciente;
	}

	public void setApellidoPaciente(String apellidoPaciente) {
		this.apellidoPaciente = apellidoPaciente;
	}

	public String getNombreMedico() {
		return nombreMedico;
	}

	public void setNombreMedico(String nombreMedico) {
		this.nombreMedico = nombreMedico;
	}

	public String getApellidoMedico() {
		return apellidoMedico;
	}

	public void setApellidoMedico(String apellidoMedico) {
		this.apellidoMedico = apellidoMedico;
	}
}