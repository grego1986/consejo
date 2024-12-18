package com.consejo.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Persona;
import com.consejo.repository.IPersonaRepository;

@Service
public class PersonaDaos implements IPersonaDaos {

	@Autowired
	private IPersonaRepository personaRepo;
	
	@Override
	public List<Persona> listarPersonas(String nombre) {
		
		return personaRepo.findByNombreContaining(nombre);
	}

	@Override
	public Persona buscarPersona(Long id) {
		
		return personaRepo.findById(id).orElse(null);
	}

	@Override
	public void guardarPersona(Persona ciudadano) {
		personaRepo.save(ciudadano);
		
	}

	@Override
	public void eliminaPersona(Persona ciudadano) {
		// TODO Auto-generated method stub
		
	}

}
