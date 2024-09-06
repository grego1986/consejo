package com.consejo.form;

public class FrmRegistroUsuario {

	private long dni;
	private String nombre;
	private String apellido;
	private String mail;
	private String pass;
	private Integer rol;
	private boolean esActivo;
	
	public FrmRegistroUsuario(long dni, String nombre, String apellido, String mail, String pass, Integer rol,
			boolean esActivo) {
		super();
		this.dni = dni;
		this.nombre = nombre;
		this.apellido = apellido;
		this.mail = mail;
		this.pass = pass;
		this.rol = rol;
		this.esActivo = esActivo;
	}
	
	

	public FrmRegistroUsuario() {
		super();
	}



	public long getDni() {
		return dni;
	}

	public void setDni(long dni) {
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

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public Integer getRol() {
		return rol;
	}

	public void setRol(Integer rol) {
		this.rol = rol;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}
	
	
}
