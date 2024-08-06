package com.consejo.pojos;

import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="HISTORIAL")
public class Movimiento {
	
	@Id
	private String id;//formato tipo string nroExpediente-numeroMovimiento (utilizo el indice de la lista).
	@Column(name="fecha")
	private LocalDate fecha;
	@Column(name="detalle")
	private String detalle;
	//quien hizo el movimiento
	
	
	public Movimiento() {
		super();
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


	public String getDetalle() {
		return detalle;
	}


	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}
	
	
	

}
