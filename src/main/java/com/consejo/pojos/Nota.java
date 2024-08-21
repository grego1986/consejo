package com.consejo.pojos;


import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table (name="NOTA")
public class Nota {

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
	//Relaciones entre objetos
	@ManyToOne
    @JoinColumn(name = "historial_id")
    private Movimiento historial;
	@OneToMany(mappedBy = "notaOriginal", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NotaModificacion> modificaciones;
	
	public Nota() {
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

	public void setNota(byte[] bs) {
		this.nota = bs;
	}

	public boolean isEsActiva() {
		return esActiva;
	}

	public void setEsActiva(boolean esActiva) {
		this.esActiva = esActiva;
	}

	public Movimiento getHistorial() {
		return historial;
	}

	public void setHistorial(Movimiento historial) {
		this.historial = historial;
	}

	public List<NotaModificacion> getModificaciones() {
		return modificaciones;
	}

	public void setModificaciones(List<NotaModificacion> modificaciones) {
		this.modificaciones = modificaciones;
	}

	
	
}
