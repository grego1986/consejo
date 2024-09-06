package com.consejo.daos;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Usuario;
import com.consejo.repository.IUsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	 @Autowired
	 private IUsuarioRepository usuarioRepository;
	 
	@Override
	public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
		
		Usuario usuario;
		
		usuario = usuarioRepository.findByMail(mail);
		
        if (usuario == null) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
        return new User(usuario.getMail(), usuario.getContra().getPass() , getAuthorities(usuario));
    }

	private Collection<? extends GrantedAuthority> getAuthorities(Usuario usuario) {
	    return List.of(new SimpleGrantedAuthority(usuario.getRol().getrol()));
	}

	
}
