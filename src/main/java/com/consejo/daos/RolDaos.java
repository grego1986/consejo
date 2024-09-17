package com.consejo.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.consejo.pojos.Rol;
import com.consejo.repository.IRolRepository;

@Service
public class RolDaos implements IRolDaos{

	@Autowired
	private IRolRepository rolRepo;
	
	@Override
	public List<Rol> listarRoles() {
		
		return rolRepo.findAll();
	}

	@Override
	public Rol buscarRol(Integer id) {
		
		return rolRepo.findById(id).orElse(null);
	}

	@Override
	public void agregarRol(Rol rol) {
		
		rolRepo.save(rol);	
	}

	@Override
	public void eliminarRol(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rol buscarRolPorNombre(String rol) {
		return rolRepo.findByRol(rol);
	}

}
