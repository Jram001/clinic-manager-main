package logico;

import java.io.Serializable;

public class DetalleFactura implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private String concepto;
	private int cantidad;
	private float precioUnitario;
	private float subtotal;

	public DetalleFactura(String id, String concepto, int cantidad, float precioUnitario) {
		this.id = id;
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.precioUnitario = precioUnitario;
		this.subtotal = cantidad * precioUnitario;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
		this.subtotal = cantidad * precioUnitario;
	}

	public float getPrecioUnitario() {
		return precioUnitario;
	}

	public void setPrecioUnitario(float precioUnitario) {
		this.precioUnitario = precioUnitario;
		this.subtotal = cantidad * precioUnitario;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}
}
