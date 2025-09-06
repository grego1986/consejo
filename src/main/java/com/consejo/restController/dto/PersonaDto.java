package com.consejo.restController.dto;

import org.springframework.hateoas.RepresentationModel;

import com.consejo.pojos.Persona;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = Persona.class)
public class PersonaDto extends RepresentationModel<PersonaDto> {

	private Long dni_Cuit;
	private String nombre;
	private String telefono;
	private String direccion;
	private String mail;
    //private List<Expediente> expedientes;
   // private TipoCiudadano tipo;
	
	public PersonaDto() {
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}
	
	
	
}
