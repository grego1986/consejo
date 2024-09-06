package com.consejo.pojos;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class TipoCiudadano {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column (name="tipo-usuario")
	private String tipoUsuario;
	@Column(name="inicial", length=2)
	private String inicial;
	@OneToMany(mappedBy = "tipo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Persona> ciudadanos = new ArrayList<>();
	
	
	public TipoCiudadano() {
		super();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getTipoUsuario() {
		return tipoUsuario;
	}


	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	public List<Persona> getCiudadanos() {
		return ciudadanos;
	}


	public void setCiudadanos(List<Persona> ciudadanos) {
		this.ciudadanos = ciudadanos;
	}


	@Override
	public String toString() {
		return tipoUsuario;
	}


	public String getInicial() {
		return inicial;
	}


	public void setInicial(String inicial) {
		this.inicial = inicial;
	}
	
	
}
