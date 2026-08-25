package logico;

import java.io.Serializable;

public class ExamenLaboratorio implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String nombre;
	private String descripcion;
	private String categoria;
	private float precioBase;
	private boolean esActivo;

	public ExamenLaboratorio(String id, String nombre, String descripcion, String categoria, float precioBase) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.categoria = categoria;
		this.precioBase = precioBase;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public float getPrecioBase() {
		return precioBase;
	}

	public void setPrecioBase(float precioBase) {
		this.precioBase = precioBase;
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
