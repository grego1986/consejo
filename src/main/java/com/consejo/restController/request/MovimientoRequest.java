package com.consejo.restController.request;

import java.time.LocalDate;

import com.consejo.enumeraciones.CircuitoExpediente;

public class MovimientoRequest {

	private String detalle;
	private LocalDate fecha;
	private NotaRequest nota;
	private CircuitoExpediente estado;
	
	public MovimientoRequest() {
		super();
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public NotaRequest getNota() {
		return nota;
	}

	public void setNota(NotaRequest nota) {
		this.nota = nota;
	}

	public CircuitoExpediente getEstado() {
		return estado;
	}

	public void setEstado(CircuitoExpediente estado) {
		this.estado = estado;
	}
	
	
}
