package com.consejo.restController.dto;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = MovimientoDto.class)
public class MovimientoDto extends RepresentationModel<MovimientoDto>  {

	private String id;
	private LocalDate fecha;
	private String detalle;
	private NotaDto nota;
	
	public MovimientoDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public NotaDto getNota() {
		return nota;
	}

	public void setNota(NotaDto nota) {
		this.nota = nota;
	}

}
