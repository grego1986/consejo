package com.consejo.pojos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OrderBy;
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
	//Relacion entre Objetos
	@ManyToOne
    @JoinColumn(name = "expediente_id")
    private Expediente expediente;
	@OneToOne (cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "nota_id") // Define la columna que guarda la clave foránea de Nota
    private Nota nota;
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


	public Expediente getExpediente() {
		return expediente;
	}


	public void setExpediente(Expediente expediente) {
		this.expediente = expediente;
	}


	public Nota getNota() {
		return nota;
	}


	public void setNota(Nota notas) {
		this.nota = notas;
	}
	
	public void generateId(String nroExpediente, Integer nroMovimiento) {
		int numeroMovimiento = nroMovimiento;
        this.id = String.format("%s/" + numeroMovimiento, nroExpediente );
    }
	

}
