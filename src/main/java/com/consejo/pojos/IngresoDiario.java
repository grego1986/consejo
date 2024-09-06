package com.consejo.pojos;

import java.time.LocalDate;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class IngresoDiario {

	@Id
	private Integer id;
	@Column (name="fecha")
	private LocalDate fecha;
	@Column (name="contador")
    private Integer contador;
	
	public IngresoDiario() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public Integer getContador() {
		return contador;
	}

	public void setContador(Integer contador) {
		this.contador = contador;
	}
	
	
	
}
