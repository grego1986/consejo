package com.consejo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.consejo.pojos.PasswordResetToken;
import com.consejo.pojos.Usuario;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
	
	    Optional<PasswordResetToken> findByToken(String token);
	    void deleteByUsuario(Usuario usuario);
	    Optional<PasswordResetToken> findByUsuario(Usuario usuario);
	    //PasswordResetToken findByToken(String token);
}
