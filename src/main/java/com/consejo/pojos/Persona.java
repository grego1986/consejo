package com.consejo.pojos;

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
@Table(name="PERSONAS")
public class Persona {

	@Id
	private Long dni_Cuit;
	@Column(name="nombre")
	private String nombre;
	@Column(name="organizacion")
	private boolean esOrganizacion;
	@Column(name="telefono")
	private String telefono;
	@Column(name="direccion")
	private String direccion;
	@Column(name="mail")
	private String mail;
	//Relaciones entre objetos
	@OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expediente> expedientes;
	@ManyToOne
    @JoinColumn(name = "tipo_ciudadano")
    private TipoCiudadano tipo;
	
	public Persona() {
		super();
	}


	public Long getDni_Cuit() {
		return dni_Cuit;
	}


	public void setDni_Cuit(Long dni_Cuit) {
		this.dni_Cuit = dni_Cuit;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public boolean isEsOrganizacion() {
		return esOrganizacion;
	}


	public void setEsOrganizacion(boolean esOrganizacion) {
		this.esOrganizacion = esOrganizacion;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public List<Expediente> getExpedientes() {
		return expedientes;
	}


	public void setExpedientes(List<Expediente> expedientes) {
		this.expedientes = expedientes;
	}


	public TipoCiudadano getTipo() {
		return tipo;
	}


	public void setTipo(TipoCiudadano tipo) {
		this.tipo = tipo;
	}


	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getDireccion() {
		return direccion;
	}


	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	
}
