package logico;

import java.io.Serializable;

public class SeguroMedico implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String nombre;
	private String rnc;
	private String telefono;
	private float porcentajeCobertura;
	private boolean esActivo;

	public SeguroMedico(String id, String nombre, String rnc, String telefono, float porcentajeCobertura) {
		this.id = id;
		this.nombre = nombre;
		this.rnc = rnc;
		this.telefono = telefono;
		this.porcentajeCobertura = porcentajeCobertura;
		this.esActivo = true;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getRnc() {
		return rnc;
	}

	public void setRnc(String rnc) {
		this.rnc = rnc;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public float getPorcentajeCobertura() {
		return porcentajeCobertura;
	}

	public void setPorcentajeCobertura(float porcentajeCobertura) {
		this.porcentajeCobertura = porcentajeCobertura;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

	@Override
	public String toString() {
		return nombre;
	}
}
