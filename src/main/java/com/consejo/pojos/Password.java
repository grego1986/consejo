package com.consejo.pojos;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="PASS")
public class Password {

	
	@Id
	private Integer id;
	@Column(name="pass")
	private String pass;
	//relaciones entre Objetos
	@OneToOne(mappedBy = "contra")
	private Usuario user;
	
	public Password() {
		super();
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	
	
}
