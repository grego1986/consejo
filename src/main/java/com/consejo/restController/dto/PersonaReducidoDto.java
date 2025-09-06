package com.consejo.restController.dto;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos básicos de Personas con link para consulta")
public class PersonaReducidoDto extends RepresentationModel<PersonaReducidoDto> {

	private Long dni_Cuit;
	private String nombre;
	
	public PersonaReducidoDto() {
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
	
}
