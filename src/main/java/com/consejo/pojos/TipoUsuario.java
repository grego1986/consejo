package com.consejo.pojos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="TIPO_USUARIO")
public class TipoUsuario {

	@Id
	private Integer id;
	@Column(name="tipo")
	private String tipo;
	//Relaciones entre Objetos
	@OneToOne(mappedBy = "tipo")
	private Usuario usuario;
	
	public TipoUsuario() {
		super();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	

}
