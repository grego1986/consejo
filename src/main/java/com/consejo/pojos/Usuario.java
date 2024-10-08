package com.consejo.pojos;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.consejo.enumeraciones.Comisiones;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
	@Column (name="comision")
	private Comisiones comision;
	//Relaciones entre Objetos
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Ingreso> ingreso = new ArrayList<>();
	@OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pass", referencedColumnName = "id")
	private Password contra;
	@ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rol_id")
    private Rol rol;
	@ManyToMany(mappedBy = "usuariosAsignados")
    private List<Expediente> expedientesAsignados = new ArrayList<>();
	@OneToOne(mappedBy = "usuario")
    private PasswordResetToken passwordResetToken;
	
	
	public Usuario(String string, String string2, boolean b, boolean c, boolean d, boolean e, List<SimpleGrantedAuthority> list) {
		super();
	}

	

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


	public List<Ingreso> getIngreso() {
		return ingreso;
	}


	public void setIngreso(List<Ingreso> ingreso) {
		this.ingreso = ingreso;
	}


	public Password getContra() {
		return contra;
	}


	public void setContra(Password contra) {
		this.contra = contra;
	}


	public Rol getRol() {
		return rol;
	}


	public void setRol(Rol rol) {
		this.rol = rol;
	}



	public List<Expediente> getExpedientesAsignados() {
		return expedientesAsignados;
	}



	public void setExpedientesAsignados(List<Expediente> expedientesAsignados) {
		this.expedientesAsignados = expedientesAsignados;
	}



	public PasswordResetToken getPasswordResetToken() {
		return passwordResetToken;
	}



	public void setPasswordResetToken(PasswordResetToken passwordResetToken) {
		this.passwordResetToken = passwordResetToken;
	}



	public Comisiones getComision() {
		return comision;
	}



	public void setComision(Comisiones comision) {
		this.comision = comision;
	}
	
	

}
