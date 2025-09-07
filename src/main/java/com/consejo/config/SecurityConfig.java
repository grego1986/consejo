package com.consejo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;


@Configuration
@EnableWebSecurity
public class SecurityConfig {


	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(authorize -> authorize.requestMatchers("/secParlamentario/**")
				.hasRole("SEC_PARLAMENTARIO")
				.requestMatchers("/expediente/**").hasAnyRole("CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRESIDENTE", "SEC_PARLAMENTARIO",
						"CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRENSA")
				.requestMatchers("/concejal/**").hasAnyRole("CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRESIDENTE",
						"CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA")
				.requestMatchers("/eventos/**").hasAnyRole("CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRESIDENTE", "SEC_PARLAMENTARIO",
						"CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRENSA")
				.requestMatchers("/expediente/ver/**").hasAnyRole("CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRESIDENTE", "SEC_PARLAMENTARIO",
						"CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRENSA")
				.requestMatchers("/comision/gobiernoYDesarrolloSocial/**").hasAnyRole("PRESIDENTE", "SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRENSA")
				.requestMatchers("/secretarioAdministrativo/**").hasAnyRole("SEC_ADMINISTRATIVO")
				.requestMatchers("/comision/desarrolloUrbanoAmbientalYEconomia/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "PRENSA")
				.requestMatchers("/ordenDelDia/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL")
				.requestMatchers("/asuntosEntrados/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRENSA")
				.requestMatchers("/expediente/archivo/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRENSA")
				.requestMatchers("/comision/ambasComisiones/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRENSA")
				.requestMatchers("/comision/**").hasAnyRole("CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "PRESIDENTE",
						"SEC_PARLAMENTARIO", "CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRENSA")
				.requestMatchers("/mesa-entrada/**").hasRole("ENTRADA")
				.requestMatchers("/prensa/**").hasRole("PRENSA")
				.requestMatchers("/notas/**").hasAnyRole("CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL", "PRESIDENTE", "SEC_PARLAMENTARIO",
						"CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA", "ENTRADA", "PRENSA", "SEC_ADMINISTRATIVO")
				.requestMatchers("/", "/home", "/login", "/login?error=true", "/logout", "/login?logout",
						"/password/cambiarPassword", "/forgot-password", "/resetearPassword/**")
				.permitAll().requestMatchers("/css/**", "/js/**", "/images/**").permitAll())
				.formLogin(form -> form.loginPage("/login").defaultSuccessUrl("/home", true)
						.failureUrl("/login?error=true"))
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login?logout")
						.invalidateHttpSession(true).clearAuthentication(true).deleteCookies("JSESSIONID"))
				.csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()));
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}