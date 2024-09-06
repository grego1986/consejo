package com.consejo.form;

public class BuscarUsuarioForm {

	
	private String nombre;
	private String apellido;
	private String mail;
	private Long dni;
	private String rol;
	private boolean esActivo;
	
	public BuscarUsuarioForm() {
		super();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Long getDni() {
		return dni;
	}

	public void setDni(Long dni) {
		this.dni = dni;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}
	
	
}
