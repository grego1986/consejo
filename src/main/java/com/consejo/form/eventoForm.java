package com.consejo.form;

import java.time.LocalDate;
import java.time.LocalTime;

public class eventoForm {

	private String evento;
	private LocalDate fecha;
	private LocalTime hora;
	private String direccion;
	
	public eventoForm() {
		super();
	}
	
	public String getEvento() {
		return evento;
	}

	public void setEvento(String evento) {
		this.evento = evento;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public LocalTime getHora() {
		return hora;
	}

	public void setHora(LocalTime hora) {
		this.hora = hora;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	
}
