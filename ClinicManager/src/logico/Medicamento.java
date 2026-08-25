package logico;

import java.io.Serializable;

public class Medicamento implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String nombre;
	private String principioActivo;
	private String presentacion;
	private String concentracion;
	private boolean esActivo;

	public Medicamento(String id, String nombre, String principioActivo, String presentacion, String concentracion) {
		this.id = id;
		this.nombre = nombre;
		this.principioActivo = principioActivo;
		this.presentacion = presentacion;
		this.concentracion = concentracion;
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

	public String getPrincipioActivo() {
		return principioActivo;
	}

	public void setPrincipioActivo(String principioActivo) {
		this.principioActivo = principioActivo;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public String getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

	@Override
	public String toString() {
		return nombre + " " + concentracion;
	}
}
