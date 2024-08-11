package com.consejo.pojos;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="USUARIOS")
public class Usuario {
	
	@Id
	private Long dni;
	@Column(name="nombre")
	private String nombre;
	@Column(name="apellido")
	private String apellido;
	@Column(name="mail")
	private String mail;
	@Column(name="activo")
	private boolean esActivo;
	//Relaciones entre Objetos
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "usuario_tipo", referencedColumnName = "id")
	private TipoUsuario tipo;
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ingreso> ingreso;
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pass", referencedColumnName = "id")
	private Password contra;
	
	
	public Usuario() {
		super();
	}


	public Long getDni() {
		return dni;
	}


	public void setDni(Long dni) {
		this.dni = dni;
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


	public boolean isEsActivo() {
		return esActivo;
	}


	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}
	
	

}
