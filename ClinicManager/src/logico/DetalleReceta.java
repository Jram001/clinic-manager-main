package logico;

import java.io.Serializable;

public class DetalleReceta implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private Medicamento medicamento;
	private String dosis;
	private String frecuencia;
	private int duracionDias;
	private String instrucciones;

	public DetalleReceta(String id, Medicamento medicamento, String dosis, String frecuencia, int duracionDias, String instrucciones) {
		this.id = id;
		this.medicamento = medicamento;
		this.dosis = dosis;
		this.frecuencia = frecuencia;
		this.duracionDias = duracionDias;
		this.instrucciones = instrucciones;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public String getDosis() {
		return dosis;
	}

	public void setDosis(String dosis) {
		this.dosis = dosis;
	}

	public String getFrecuencia() {
		return frecuencia;
	}

	public void setFrecuencia(String frecuencia) {
		this.frecuencia = frecuencia;
	}

	public int getDuracionDias() {
		return duracionDias;
	}

	public void setDuracionDias(int duracionDias) {
		this.duracionDias = duracionDias;
	}

	public String getInstrucciones() {
		return instrucciones;
	}

	public void setInstrucciones(String instrucciones) {
		this.instrucciones = instrucciones;
	}
}
