package com.consejo.form;

import java.time.LocalDate;


import org.springframework.web.multipart.MultipartFile;

public class ExpedienteForm {


	private Long dni;
	private String nombre;
    private String caratula;
    private String detalle;
    private Boolean organizacion = false;
    private String direccion;
    private String telefono;
    private String mail;
    private Integer tipoCiudadano;
    private Integer tipoNota;
    private String tituloNota;
    private MultipartFile nota;

    // Getters y setters
    
    
    public ExpedienteForm() {
		super();
	}


    public String getCaratula() {
        return caratula;
    }

	public void setCaratula(String caratula) {
        this.caratula = caratula;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public boolean isOrganizacion() {
        return organizacion;
    }

    public void setOrganizacion(boolean organizacion) {
        this.organizacion = organizacion;
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

    public Integer getTipoNota() {
        return tipoNota;
    }

    public void setTipoNota(Integer tipoNota) {
        this.tipoNota = tipoNota;
    }

    public String getTituloNota() {
        return tituloNota;
    }

    public void setTituloNota(String tituloNota) {
        this.tituloNota = tituloNota;
    }

    public MultipartFile getNota() {
        return nota;
    }

    public void setNota(MultipartFile nota) {
        this.nota = nota;
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
    
    
}
