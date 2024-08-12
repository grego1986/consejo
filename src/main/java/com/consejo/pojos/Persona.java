package com.consejo.pojos;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
	@Column(name="Contacto")
	private String mail;
	//Relaciones entre objetos
	@OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Expediente> expedientes;
	
	
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
	
	
}
