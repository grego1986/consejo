package com.consejo.pojos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="EXPEDIENTE")
public class Expediente {
	
	@Id
	private String id; //formato codigoAsunto-tipo-fecha/nroOrdenDia ejemplo: 01-01-20240805/01
	@Column(name="caratula")
	private String caratula;
	@Column(name="detalle")
	private String detalle;
	@Column(name="fecha")
	private LocalDate fecha;
	//Relaciones entre objetos
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos;
    @ManyToOne
    @JoinColumn(name = "tipo_expediente")
    private TipoNota tipo;
    @ManyToOne
    @JoinColumn(name = "ciudadano")
    private Persona persona;
    
	
	public Expediente() {
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
	
	 public List<Movimiento> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<Movimiento> movimientos) {
		this.movimientos = movimientos;
	}

	public TipoNota getTipo() {
		return tipo;
	}

	public void setTipo(TipoNota tipo) {
		this.tipo = tipo;
	}

	public Persona getPersona() {
		return persona;
	}

	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public void generateId(String codigoAsunto, String tipo, LocalDate fecha, String nroOrdenDia) {
        this.id = String.format("%s-%s-%s-%s", codigoAsunto, tipo, fecha.format(DateTimeFormatter.BASIC_ISO_DATE), nroOrdenDia);
    }

}
