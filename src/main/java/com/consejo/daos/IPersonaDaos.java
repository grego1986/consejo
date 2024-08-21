package com.consejo.daos;


import java.util.List;

import com.consejo.pojos.Persona;


public interface IPersonaDaos {

	public List<Persona> listarPersonas();
	public Persona buscarPersona(Long id);
	public void guardarPersona(Persona ciudadano);
	public void eliminaPersona(Persona ciudadano);
}
