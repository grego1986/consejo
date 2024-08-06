package com.consejo.pojos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="EXPEDIENTE")
public class Expediete {
	
	@Id
	private String id; //formato codigoAsunto-codigoNota-fecha/nroOrdenDia ejemplo: 01-01-20240805/01
	@Column(name="caratula")
	private String caratula;
	@Column(name="detalle")
	private String detalle;
	@Column(name="fecha")
	private LocalDate fecha;
	
	public Expediete() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
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
	
	

}
