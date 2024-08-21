package com.consejo.daos;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Usuario;
import com.consejo.repository.IUsuarioRepository;

@Service
public class UsuarioDaos implements IUsuarioDaos, UserDetailsService {

	
	  @Autowired
	    private IUsuarioRepository usuarioRepo;
	  
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		 // Buscar el usuario por su mail
        Usuario usuario = usuarioRepo.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Convertir el rol en GrantedAuthority
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(usuario.getRol().getrol());

        // Crear un objeto UserDetails
        return (UserDetails) new Usuario(usuario.getMail(), usuario.getContra().getPass(), 
                usuario.isEsActivo(), true, true, true, 
                Collections.singletonList(authority));
	}

	@Override
	public List<Usuario> listarUsuarios() {
		
		return usuarioRepo.findAll();
	}

	@Override
	public Usuario buscarUsuario(Long dni) {
		
		return usuarioRepo.findById(dni).orElse(null);
	}

	@Override
	public void agregarUsuario(Usuario usuario) {
		
		usuarioRepo.save(usuario);
	}

	@Override
	public void elimuinarUsuario(Long dni) {
		
		
	}
	
	

}
