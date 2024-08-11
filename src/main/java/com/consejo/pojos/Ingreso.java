package com.consejo.pojos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table
public class Ingreso {

	@Id
	private Long id;
	@Column(name="fecha")
	private LocalDate fecha;
	@Column(name="hora")
	private LocalDate hora;
	//Relaciones entre Objetos
	@ManyToOne
    @JoinColumn(name = "ingreso_id")
	private Usuario usuario;
	
	
	public Ingreso() {
		super();
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}
	public LocalDate getHora() {
		return hora;
	}
	public void setHora(LocalDate hora) {
		this.hora = hora;
	}
	
	
	
	
}
