package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class Consulta implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
    private String id;
    private Paciente paciente;
    private Medico medico;
    private String sintomas;
    private String diagnostico;
    private EnfermedadBajoVigilancia enfermedadVigilada;
    private boolean esImportante;
    private LocalDate fecha;
    
    public Consulta(String id, Paciente paciente, Medico medico, String sintomas, String diagnostico, EnfermedadBajoVigilancia enfermedadVigilada,
    		boolean esImportante)
    {
    	this.id = id;
    	this.paciente = paciente;
    	this.medico = medico;
    	this.sintomas = sintomas;
    	this.diagnostico = diagnostico;
    	this.enfermedadVigilada = enfermedadVigilada;
    	this.esImportante = esImportante;
    	this.fecha = LocalDate.now();
    	
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
	public Medico getMedico() {
		return medico;
	}
	public void setMedico(Medico medico) {
		this.medico = medico;
	}
	public String getSintomas() {
		return sintomas;
	}
	public void setSintomas(String sintomas) {
		this.sintomas = sintomas;
	}
	public String getDiagnostico() {
		return diagnostico;
	}
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	public EnfermedadBajoVigilancia getEnfermedadVigilada() {
		return enfermedadVigilada;
	}
	public void setEnfermedadVigilada(EnfermedadBajoVigilancia enfermedadVigilada) {
		this.enfermedadVigilada = enfermedadVigilada;
	}
	public boolean isEsImportante() {
		return esImportante;
	}
	public void setEsImportante(boolean esImportante) {
		this.esImportante = esImportante;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
   
}