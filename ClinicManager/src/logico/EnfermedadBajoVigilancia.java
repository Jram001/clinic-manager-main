package logico;

import java.io.Serializable;

public class EnfermedadBajoVigilancia implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
    private String id;
    private String nombre;
    private String descripcion;
    private String gravedad;
    private boolean esActivo;
    
    public EnfermedadBajoVigilancia(String id, String nombre, String descripcion, String gravedad)
    {
    	this.id = id;
    	this.nombre = nombre;
    	this.descripcion = descripcion;
    	this.gravedad = gravedad;
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
	public String getGravedad() {
		return gravedad;
	}
	public void setGravedad(String gravedad) {
		this.gravedad = gravedad;
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