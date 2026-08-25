package logico;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Factura implements Serializable {
	private static final long serialVersionUID = 8332914665293201379L;
	private String id;
	private Consulta consulta;
	private SeguroMedico seguro;
	private LocalDate fecha;
	private float subtotal;
	private float descuentoSeguro;
	private float total;
	private String estado;
	private ArrayList<DetalleFactura> detalles;

	public Factura(String id, Consulta consulta, SeguroMedico seguro, LocalDate fecha) {
		this.id = id;
		this.consulta = consulta;
		this.seguro = seguro;
		this.fecha = fecha;
		this.subtotal = 0;
		this.descuentoSeguro = 0;
		this.total = 0;
		this.estado = "pendiente";
		this.detalles = new ArrayList<>();
	}

	public void agregarDetalle(DetalleFactura detalle) {
		detalles.add(detalle);
		recalcularTotales();
	}

	public void recalcularTotales() {
		this.subtotal = 0;
		for (DetalleFactura d : detalles) {
			this.subtotal += d.getSubtotal();
		}
		if (seguro != null) {
			this.descuentoSeguro = subtotal * (seguro.getPorcentajeCobertura() / 100f);
		} else {
			this.descuentoSeguro = 0;
		}
		this.total = subtotal - descuentoSeguro;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Consulta getConsulta() {
		return consulta;
	}

	public void setConsulta(Consulta consulta) {
		this.consulta = consulta;
	}

	public Paciente getPaciente() {
		return this.consulta != null ? this.consulta.getPaciente() : null;
	}

	public SeguroMedico getSeguro() {
		return seguro;
	}

	public void setSeguro(SeguroMedico seguro) {
		this.seguro = seguro;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public float getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(float subtotal) {
		this.subtotal = subtotal;
	}

	public float getDescuentoSeguro() {
		return descuentoSeguro;
	}

	public void setDescuentoSeguro(float descuentoSeguro) {
		this.descuentoSeguro = descuentoSeguro;
	}

	public float getTotal() {
		return total;
	}

	public void setTotal(float total) {
		this.total = total;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList<DetalleFactura> getDetalles() {
		return detalles;
	}

	public void setDetalles(ArrayList<DetalleFactura> detalles) {
		this.detalles = detalles;
	}
}
