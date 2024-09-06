package com.consejo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.consejo.daos.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {


	 @Autowired
	 private CustomUserDetailsService userDetailsService;
	 
	 @Bean
	    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/administrador/**").hasAuthority("ROLE_ADMIN")
	                .requestMatchers("/consejal/**").hasAuthority("ROLE_CONSEJAL")
	                .requestMatchers("/mesa-entrada/**").hasAuthority("ROLE_ENTRADA")
	                .requestMatchers("/prensa/**").hasAuthority("ROLE_PRENSA")
	                .requestMatchers("/", "/home", "/login", "/login?error=true","/logout", "/login?logout").permitAll()
	                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll() // Permitir acceso a recursos estáticos
	            )
	            .formLogin(form -> form
	                .loginPage("/login")
	                .defaultSuccessUrl("/home", true)
	                .failureUrl("/login?error=true")  // Redirigir en caso de error
	            )
	            .logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/login?logout")
	                .invalidateHttpSession(true)  // Invalidar la sesión HTTP
	                .clearAuthentication(true)    // Limpiar la autenticación
	                .deleteCookies("JSESSIONID")  // Borrar las cookies de la sesión
	            );
	        return http.build();
	    }

	    @Bean
	    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
	        return http.getSharedObject(AuthenticationManagerBuilder.class)
	            .userDetailsService(userDetailsService)  // Usando el servicio personalizado
	            .passwordEncoder(passwordEncoder())
	            .and()
	            .build();
	    }

	    @Bean
	    PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
}
