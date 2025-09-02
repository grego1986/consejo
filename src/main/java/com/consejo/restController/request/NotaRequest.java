package com.consejo.restController.request;

import org.springframework.web.multipart.MultipartFile;

public class NotaRequest {

	private String titulo;
	private MultipartFile nota;
	
	public NotaRequest() {
		super();
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public MultipartFile getNota() {
		return nota;
	}

	public void setNota(MultipartFile nota) {
		this.nota = nota;
	}
	
	
	
}
