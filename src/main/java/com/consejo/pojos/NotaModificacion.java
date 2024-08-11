package com.consejo.pojos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="NOTA_MODIFICACION")
public class NotaModificacion {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id; //formato tipo string nroExpediente-numeroNota (utilizo el indice de la lista).
	@Column(name="titulo") 
	private String titulo;
	@Lob
	@Column(name="nota", columnDefinition = "LONGBLOB")
	private byte[] nota;
	@Column(name="activa")
	private boolean esActiva;
	//Relaciones entre Objetos
	@ManyToOne
    @JoinColumn(name = "nota_id")
    private Nota notaOriginal;
	
	public NotaModificacion() {
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


	public boolean isEsActiva() {
		return esActiva;
	}


	public void setEsActiva(boolean esActiva) {
		this.esActiva = esActiva;
	}
	
	
	
}
