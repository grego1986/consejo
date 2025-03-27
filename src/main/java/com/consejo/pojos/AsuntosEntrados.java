package com.consejo.pojos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;

@Entity
public class AsuntosEntrados {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; // formato tipo string nroExpediente-numeroNota (utilizo el indice de la lista).
	@Column(name = "fecha")
	private LocalDate fecha;
	@Column(name = "titulo")
	private String titulo;
	@Lob
	@Column(name = "nota", columnDefinition = "LONGBLOB")
	private byte[] nota;
	// relacion con otros objetos
	@ManyToMany
	@JoinTable(name = "orden_expedientes", joinColumns = @JoinColumn(name = "orden_id"), inverseJoinColumns = @JoinColumn(name = "expediente_id"))
	private List<Expediente> expedientes = new ArrayList<>();
	
	public AsuntosEntrados() {
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

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public byte[] getNota() {
		return nota;
	}

	public void setNota(byte[] nota) {
		this.nota = nota;
	}

	public List<Expediente> getExpedientes() {
		return expedientes;
	}

	public void setExpedientes(List<Expediente> expedientes) {
		this.expedientes = expedientes;
	}
	
	
	
}
