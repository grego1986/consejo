package com.consejo.daos;

import com.consejo.pojos.Usuario;

public interface IPasswordDaos {

	public void ingresarPass (Usuario user, String pass);
	public void modificarPass (Usuario user, String newPass, String antiguoPass);
	public boolean authenticate (Long user, String pass);
}
