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
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.consejo.daos.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig  {


	 @Autowired
	 private CustomUserDetailsService userDetailsService;
	 
	 @Bean
	    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
	        http
	            .authorizeHttpRequests(authorize -> authorize
	                .requestMatchers("/administrador/**").hasRole("ADMIN")
	                .requestMatchers("/consejal/**").hasAnyRole("CONCEJAL", "PRESIDENTE")
	                .requestMatchers("/presidente/**").hasRole("PRESIDENTE")
	                .requestMatchers("/mesa-entrada/**").hasRole("ENTRADA")
	                .requestMatchers("/prensa/**").hasRole("PRENSA")
	                .requestMatchers("/notas/**").hasAnyRole("CONCEJAL", "PRESIDENTE", "ADMIN")
	                .requestMatchers("/", "/home", "/login", "/login?error=true", "/logout", "/login?logout").permitAll()
	                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
	            )
	            .formLogin(form -> form
	                .loginPage("/login")
	                .defaultSuccessUrl("/home", true)
	                .failureUrl("/login?error=true")
	            )
	            .logout(logout -> logout
	                .logoutUrl("/logout")
	                .logoutSuccessUrl("/login?logout")
	                .invalidateHttpSession(true)
	                .clearAuthentication(true)
	                .deleteCookies("JSESSIONID")
	            )
	            .csrf(csrf -> csrf
	                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
	            );
	        return http.build();
	    }

	    @Bean
	    public PasswordEncoder passwordEncoder() {
	        return new BCryptPasswordEncoder();
	    }
	}