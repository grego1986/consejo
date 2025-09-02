package com.consejo.restController.dto;

import org.springframework.hateoas.RepresentationModel;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = NotaDto.class)
public class NotaDto extends RepresentationModel<NotaDto> {

	private Long id;
	private String titulo;
	private String verNota;
	private String descargarNota;
	
	public NotaDto() {
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

	public String getVerNota() {
		return verNota;
	}

	public void setVerNota(String verNota) {
		this.verNota = verNota;
	}

	public String getDescargarNota() {
		return descargarNota;
	}

	public void setDescargarNota(String descargarNota) {
		this.descargarNota = descargarNota;
	}
	
}
