package com.consejo.pojos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="ROLES")
public class Rol {

	
	@Id
	private Integer id;
	@Column(name="rol")
	private String rol;
	@Column(name="activo")
	private boolean esActivo;
	//relaciones entre clases
	@OneToOne(mappedBy = "rol")
    private Usuario usuario;
	
	public Rol() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getrol() {
		return rol;
	}

	public void setrol(String rol) {
		this.rol = rol;
	}

	public boolean isEsActivo() {
		return esActivo;
	}

	public void setEsActivo(boolean esActivo) {
		this.esActivo = esActivo;
	}
	
	
}
