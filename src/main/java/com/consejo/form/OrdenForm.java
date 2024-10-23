package com.consejo.form;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;


public class OrdenForm {

	private LocalDate fecha;
	private String titulo;
	private MultipartFile nota;
	private List<String> expedienteIds = new ArrayList<>();
	
	public OrdenForm() {
		super();
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<String> getExpedienteIds() {
		return expedienteIds;
	}

	public void setExpedienteIds(List<String> expedienteIds) {
		this.expedienteIds = expedienteIds;
	}

	public MultipartFile getNota() {
		return nota;
	}

	public void setNota(MultipartFile nota) {
		this.nota = nota;
	}
	
	
}
