package logico;

import java.io.Serializable;

public class Usuario implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String nombreUsuario;
	private String clave;
	private String linkId;
	private String rol;
	private boolean esActivo;

	public Usuario(String nombreUsuario, String clave, String rol, String linkId) {
		this.nombreUsuario = nombreUsuario;
		this.clave = clave;
		this.rol = rol;
		this.linkId = linkId;
		this.esActivo = true;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public String getLinkId() {
		return linkId;
	}

	public void setLinkId(String linkId) {
		this.linkId = linkId;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean getEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}

}
