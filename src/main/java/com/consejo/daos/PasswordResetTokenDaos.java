package com.consejo.daos;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.PasswordResetToken;
import com.consejo.pojos.Usuario;
import com.consejo.repository.PasswordResetTokenRepository;

import jakarta.transaction.Transactional;


@Service
public class PasswordResetTokenDaos implements IPasswordResetTokenDaos {

	@Autowired
    private PasswordResetTokenRepository tokenRepo;
	
	@Override
	public Optional<PasswordResetToken> getTokenByUsuario(Usuario usuario) {
		
		return tokenRepo.findByUsuario(usuario);
	}

	@Override
	public Optional<PasswordResetToken> getTokenByTokenOptional(String token) {
		return tokenRepo.findByToken(token);
	}

	@Override
	public void createPasswordResetToken(Usuario usuario, String token) {
		PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUsuario(usuario);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(24)); // Expira en 24 horas
        tokenRepo.save(passwordResetToken);
	}

	@Override
	public PasswordResetToken getTokenByToken(String token) {
		
		Optional <PasswordResetToken> tokenOp = tokenRepo.findByToken(token);
		if (tokenOp.isPresent()) {
			PasswordResetToken pass = tokenOp.get();
			return pass;
		}
		return null;
	}

	@Override
	@Transactional
	public void eliminarToken(Usuario usuario) {
		
		tokenRepo.deleteByUsuario(usuario);
	}
}
