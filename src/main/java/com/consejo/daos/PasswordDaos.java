package com.consejo.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Password;
import com.consejo.pojos.Usuario;
import com.consejo.repository.IPasswordRepository;
import com.consejo.repository.PasswordResetTokenRepository;

@Service
public class PasswordDaos implements IPasswordDaos{
	
	@Autowired
	private IPasswordRepository passRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UsuarioDaos userServi;
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Override
	public void ingresarPass(Usuario user, String pass) {
		Password password = new Password();
        password.setPass(passwordEncoder.encode(pass));
        password.setUser(user);  // Establece la relación con el usuario
        passRepo.save(password);
		 
	}

	@Override
	public void modificarPass(Usuario user, String newPass, String antiguoPass) {
		
		if (authenticate(user.getDni(), antiguoPass)) {
			Password password = new Password();
	        password.setPass(passwordEncoder.encode(newPass));
	        user.setContra(password);
	        userServi.modificarUsuario(user);
		}
	}

	//metodo para verificar si la contraseña ingresada por el usuario es correcta
	@Override
	public boolean authenticate(Long user, String pass) {
		
		Password storedPassword = passRepo.findByUserDni(user);
        if (storedPassword != null) {
            return passwordEncoder.matches(pass, storedPassword.getPass());
        }
        return false;
	}

	
	
}
