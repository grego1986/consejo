package com.consejo.daos;

import java.util.Optional;

import com.consejo.pojos.PasswordResetToken;
import com.consejo.pojos.Usuario;

import jakarta.transaction.Transactional;

public interface IPasswordResetTokenDaos {

	public Optional<PasswordResetToken> getTokenByUsuario(Usuario usuario);
	public Optional<PasswordResetToken> getTokenByTokenOptional(String token);
	public void createPasswordResetToken(Usuario usuario, String token);
	public PasswordResetToken getTokenByToken (String token);
	@Transactional
	public void eliminarToken (Usuario usuario);
}
