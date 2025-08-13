package com.consejo.restController.dto;

import java.time.LocalDate;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.consejo.pojos.Expediente;
import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Datos para crear o actualizar un Asunto Entrado")
public class AsuntosEntradosDto extends RepresentationModel<AsuntosEntradosDto> {

	@Schema(description = "Lista de IDs de expedientes asociados al asunto", example = "[1, 2, 3]")
	//@JsonIgnore
	private List<EntityModel<ExpedienteExportDto>> expedientes;
	@Schema(description = "Fecha de ingreso del asunto (YYYY-MM-DD)", example = "2025-08-10")
	private LocalDate fecha;
	@Schema(description = "ID del asunto (solo se usa en actualización)", example = "1")
	private Long id;
	@Schema(description = "Título del asunto", example = "Solicitud de iluminación pública")
	private String titulo;
	@Schema(description = "Indica si el asunto ya fue tratado", example = "false")
	private Boolean tratado;
	@Schema(description = "Contenido o nota asociada al asunto", example = "Pedido realizado por vecinos del barrio San Martín")
	private String nota;

	public AsuntosEntradosDto() {
		super();
	}

	public AsuntosEntradosDto(AsuntosEntradosDto asuntos) {
		super();
		this.expedientes = asuntos.getExpedientes();
		this.fecha = asuntos.getFecha();
		this.id = asuntos.getId();
		this.titulo = asuntos.getTitulo();
		this.tratado = asuntos.getTratado();
		this.nota = asuntos.getNota();
	}

	

	public List<EntityModel<ExpedienteExportDto>> getExpedientes() {
		return expedientes;
	}

	public void setExpedientes(List<EntityModel<ExpedienteExportDto>> list) {
		this.expedientes = list;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
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

	public Boolean getTratado() {
		return tratado;
	}

	public void setTratado(Boolean tratado) {
		this.tratado = tratado;
	}

	public String getNota() {
		return nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

}
