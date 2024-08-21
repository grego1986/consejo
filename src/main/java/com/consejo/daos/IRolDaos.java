package com.consejo.daos;

import java.util.List;

import com.consejo.pojos.Rol;

public interface IRolDaos {

	public List<Rol> listarRoles();
	public Rol buscarRol (Integer id);
	public void agregarRol(Rol rol);
	public void eliminarRol (Integer id);
	
}
