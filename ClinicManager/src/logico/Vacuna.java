package logico;

import java.io.Serializable;

public class Vacuna implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String nombre;
	private String fabricante;
	private float dosis;
	private String descripcion;
	private boolean aplicada; // solo persona usa
	private boolean esActivo;

	public Vacuna(String id, String nombre, String fabricante, float dosis, String descripcion) {
		this.id = id;
		this.nombre = nombre;
		this.fabricante = fabricante;
		this.dosis = dosis;
		this.descripcion = descripcion;
		this.esActivo = true;
		this.aplicada = false;

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

	public String getFabricante() {
		return fabricante;
	}

	public void setFabricante(String fabricante) {
		this.fabricante = fabricante;
	}

	public float getDosis() {
		return dosis;
	}

	public void setDosis(float dosis) {
		this.dosis = dosis;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

	public boolean isAplicada() {
		return aplicada;
	}

	public void setAplicada(boolean aplicada) {
		this.aplicada = aplicada;
	}

}