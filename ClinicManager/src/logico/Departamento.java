package logico;

import java.io.Serializable;

public class Departamento implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String nombre;
	private String descripcion;
	private String ubicacion;
	private boolean esActivo;

	public Departamento(String id, String nombre, String descripcion, String ubicacion) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.ubicacion = ubicacion;
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

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
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
