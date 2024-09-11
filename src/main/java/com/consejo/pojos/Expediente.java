package com.consejo.pojos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.consejo.enumeraciones.CircuitoExpediente;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
	@Enumerated(EnumType.STRING)
    @Column(name="estado")
    private CircuitoExpediente estado;
	//Relaciones entre objetos
    @OneToMany(mappedBy = "expediente", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "tipo_expediente")
    private TipoNota tipo;
    @ManyToOne
    @JoinColumn(name = "ciudadano")
    private Persona persona;
    @ManyToMany
    @JoinTable(
        name = "expediente_usuarios",
        joinColumns = @JoinColumn(name = "expediente_id"),
        inverseJoinColumns = @JoinColumn(name = "usuario_id")
    )
    private List<Usuario> usuariosAsignados = new ArrayList<>();
    
	
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

	
	public CircuitoExpediente getEstado() {
		return estado;
	}

	public void setEstado(CircuitoExpediente estado) {
		this.estado = estado;
	}

	
	public List<Usuario> getUsuariosAsignados() {
		return usuariosAsignados;
	}

	public void setUsuariosAsignados(List<Usuario> usuariosAsignados) {
		this.usuariosAsignados = usuariosAsignados;
	}

	public void generateId(String codigoAsunto, String tipo, LocalDate fecha, Integer nroOrdenDia) {
        this.id = String.format("%s-%s-%s-%s", codigoAsunto, tipo, fecha.format(DateTimeFormatter.BASIC_ISO_DATE), nroOrdenDia);
    }

}
