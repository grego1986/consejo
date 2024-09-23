package com.consejo.daos;

import java.util.List;

import com.consejo.pojos.Password;
import com.consejo.pojos.Usuario;

public interface IUsuarioDaos {

	public List<Usuario> listarUsuarios();
	public Usuario buscarUsuario (Long dni);
	public boolean agregarUsuario (Usuario usuario);
	public void elimuinarUsuario (Long dni);
	public List<Usuario> buscarUsuarios(String nombre, String apellido);
	public boolean modificarUsuario (Usuario usuario);
	public Usuario buscarPorMail (String mail);
	public void recuperarAcceso (Usuario user, String pass);
	
}
