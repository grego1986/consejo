package com.consejo.daos;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.el.stream.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Password;
import com.consejo.pojos.PasswordResetToken;
import com.consejo.pojos.Rol;
import com.consejo.pojos.Usuario;
import com.consejo.repository.IRolRepository;
import com.consejo.repository.IUsuarioRepository;
import com.consejo.repository.PasswordResetTokenRepository;

@Service
public class UsuarioDaos implements IUsuarioDaos {

	@Autowired
	private IUsuarioRepository usuarioRepo;
	@Autowired
	private IRolRepository rolRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	

	@Override
	public List<Usuario> listarUsuarios() {

		return usuarioRepo.findAll();
	}

	@Override
	public Usuario buscarUsuario(Long dni) {

		return usuarioRepo.findById(dni).orElse(null);
	}

	@Override
	public boolean agregarUsuario(Usuario usuario) {
		try {
			// Guardar el usuario en la base de datos
			if (!usuario.getRol().getrol().equals("ROLE_INACTIVO")) {
				usuario.setEsActivo(true);
			}
			usuarioRepo.save(usuario);
			return true; // Registro exitoso
		} catch (Exception e) {
			// Manejar cualquier excepción que pueda ocurrir
			e.printStackTrace();
			return false; // Registro fallido
		}
	}

	@Override
	public void elimuinarUsuario(Long dni) {

	}

	public boolean existe(Long dni) {

		if (usuarioRepo.existsById(dni)) {
			return true;
		}
		return false;
	}

	@Override
	public List<Usuario> buscarUsuarios(String nombre, String apellido) {
		if ((nombre == null || nombre.isEmpty()) && (apellido == null || apellido.isEmpty())) {
			return usuarioRepo.findAll(); // Si no hay nombre ni apellido, listar todos
		}
		return usuarioRepo.findByNombreOrApellido(nombre, apellido); // Buscar por nombre o apellido
	}

	@Override
	public boolean modificarUsuario(Usuario usuario) {

		Rol inactivo = rolRepo.findByRol("ROLE_INACTIVO");

		if (usuarioRepo.existsById(usuario.getDni())) {
			if (usuario.getRol().equals(inactivo)) {
				usuario.setEsActivo(false);
			} else {
				usuario.setEsActivo(true);
			}
			usuarioRepo.save(usuario);
			return true;
		}
		return false;
	}

	@Override
	public Usuario buscarPorMail(String mail) {
		return usuarioRepo.findByMail(mail);
	}

	@Override
	public void recuperarAcceso(Usuario user, String pass) {
		
		Password passw = user.getContra();
		passw.setPass(passwordEncoder.encode(pass));
		usuarioRepo.save(user);
		
	}

	
}
