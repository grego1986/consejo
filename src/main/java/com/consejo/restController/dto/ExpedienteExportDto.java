package com.consejo.restController.dto;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos básicos del expediente con link para consulta")
public class ExpedienteExportDto extends RepresentationModel<ExpedienteExportDto>  {

	@Schema(description = "ID del expediente", example = "IN-IE-20241023-5")
	private String id;
	@Schema(description = "Carátula del Expediente", example = "zoonosis barrio las mercedes")
	private String caratula;
	
	public ExpedienteExportDto() {
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
	
	
}
