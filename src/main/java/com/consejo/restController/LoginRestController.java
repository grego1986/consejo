package com.consejo.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.consejo.config.JwtUtil;
import com.consejo.restController.dto.LoginRequest;
import com.consejo.restController.dto.LoginResponse;

@RestController
@RequestMapping("/api/auth")
public class LoginRestController {

	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtUtil jwtutil;
	
	@PostMapping("/login")
	private ResponseEntity<?> login (@RequestBody LoginRequest request){
		try {
			Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
			
			String token = jwtutil.generateToken(request.getUsername());
			
			return ResponseEntity.ok(new LoginResponse(token));
		} catch (BadCredentialsException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales Inválidas");
		}
	}
	
}
