package com.consejo.form;

public class CiudadanoAgregar {

	private Long dni;
	private Boolean organizacion;
    private String direccion;
    private String telefono;
    private String mail;
    private String nombre;
    private Integer tipoCiudadano;
    
	public CiudadanoAgregar() {
		super();
	}

	public Long getDni() {
		return dni;
	}

	public void setDni(Long dni) {
		this.dni = dni;
	}

	public Boolean getOrganizacion() {
		return organizacion;
	}

	public void setOrganizacion(Boolean organizacion) {
		this.organizacion = organizacion;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getTipoCiudadano() {
		return tipoCiudadano;
	}

	public void setTipoCiudadano(Integer tipoCiudadano) {
		this.tipoCiudadano = tipoCiudadano;
	}
    
    
}
