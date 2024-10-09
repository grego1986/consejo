package com.consejo.pojos;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name="ordenDia")
public class OrdenDia {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //formato tipo string nroExpediente-numeroNota (utilizo el indice de la lista).
	@Column (name="fecha")
	private LocalDate fecha;
	@Column(name="titulo") 
	private String titulo;
	@Lob
	@Column(name="nota", columnDefinition = "LONGBLOB")
	private byte[] nota;
	
	public OrdenDia() {
		super();
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
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


	public LocalDate getFecha() {
		return fecha;
	}


	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}


	
	
}
