package com.consejo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.consejo.pojos.Password;
import com.consejo.pojos.Rol;
import com.consejo.pojos.TipoCiudadano;
import com.consejo.pojos.TipoNota;
import com.consejo.pojos.Usuario;
import com.consejo.repository.IPasswordRepository;
import com.consejo.repository.IRolRepository;
import com.consejo.repository.ITipoCiudadanoRepository;
import com.consejo.repository.ITipoNotaRepository;
import com.consejo.repository.IUsuarioRepository;


@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private IUsuarioRepository usuarioRepo;
	@Autowired
	private IPasswordRepository passRepo;
	@Autowired
	private IRolRepository rolRepo;
	@Autowired
	private ITipoNotaRepository tipoNotaRepo;
	@Autowired
	private ITipoCiudadanoRepository tipoCiudadanoRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) throws Exception {
		
		if (usuarioRepo.count() == 0) {
            // Crear el usuario
            Usuario admin = new Usuario();
            Password passAdmin = new Password();
            Rol rolAdmin = new Rol();
            Usuario mesaEntrada = new Usuario();
            Password passMesa = new Password();
            Rol rolMesa = new Rol();
            Usuario consejal = new Usuario();
            Password passConsejal = new Password();
            Rol rolConsejal = new Rol();
            Usuario prensa = new Usuario();
            Password passPrensa = new Password();
            Rol rolPrensa = new Rol();
            Rol rolPresidencia = new Rol();
            Rol rolInactivo = new Rol();
            
            //Administrador
            admin.setNombre("Jose");
            admin.setApellido("Gregoretti");
            admin.setDni((long) 32274576);
            admin.setMail("joseluisgregoretti@gmail.com");
            
            passAdmin.setPass(passwordEncoder.encode("admin"));
            passAdmin.setUser(admin);
            
            admin.setContra(passAdmin);
            
            rolAdmin.setRol("ROLE_ADMIN");
            rolAdmin.getUsuarios().add(admin);
            
            admin.setRol(rolAdmin);
            admin.setEsActivo(true);
            
            rolRepo.save(rolAdmin);
            usuarioRepo.save(admin);
            passRepo.save(passAdmin);
            
            
           
            //Mesa de entrada
            mesaEntrada.setNombre("pepe");
            mesaEntrada.setApellido("argento");
            mesaEntrada.setDni((long) 35256254);
            mesaEntrada.setMail("mesaentrada@gmail.com");
            
            passMesa.setPass(passwordEncoder.encode("mesaentrada"));
            passMesa.setUser(mesaEntrada);
            
            mesaEntrada.setContra(passMesa);
            
            rolMesa.setRol("ROLE_ENTRADA");
            rolMesa.getUsuarios().add(mesaEntrada);
            
            mesaEntrada.setRol(rolMesa);
            mesaEntrada.setEsActivo(true);
            
            rolRepo.save(rolMesa);
            usuarioRepo.save(mesaEntrada);
            passRepo.save(passMesa);
            
            
            
            //Prensa
            prensa.setNombre("omar");
            prensa.setApellido("medina");
            prensa.setDni((long) 31322588);
            prensa.setMail("prensa@gmail.com");
            
            passPrensa.setPass(passwordEncoder.encode("prensa"));
            passPrensa.setUser(prensa);
            
            prensa.setContra(passPrensa);
            
            rolPrensa.setRol("ROLE_PRENSA");
            rolPrensa.getUsuarios().add(prensa);
            
            prensa.setRol(rolPrensa);
            prensa.setEsActivo(true);
            
            
            rolRepo.save(rolPrensa);
            usuarioRepo.save(prensa);
            passRepo.save(passPrensa);
            
            
            
            //Consejal
            consejal.setNombre("jorge");
            consejal.setApellido("crespo");
            consejal.setDni((long) 30256254);
            consejal.setMail("consejal@gmail.com");
            
            passConsejal.setPass(passwordEncoder.encode("consejal"));
            passConsejal.setUser(consejal);
            
            consejal.setContra(passConsejal);
            
            rolConsejal.setRol("ROLE_CONCEJAL");
            rolConsejal.getUsuarios().add(consejal);
            
            consejal.setRol(rolConsejal);
            consejal.setEsActivo(true);
            
            rolRepo.save(rolConsejal);
            usuarioRepo.save(consejal);
            passRepo.save(passConsejal);
            
            rolPresidencia.setRol("ROLE_PRESIDENTE");
            
            rolRepo.save(rolPresidencia);
            
            rolInactivo.setRol("ROLE_INACTIVO");
            
            rolRepo.save(rolInactivo);
        }
		
		if (tipoNotaRepo.count() == 0) {

			TipoNota sp= new TipoNota();
			TipoNota in= new TipoNota();
			TipoNota tt= new TipoNota();
			TipoNota se= new TipoNota();
			TipoNota ma= new TipoNota();
			TipoNota vu= new TipoNota();
			TipoNota ov= new TipoNota();
			TipoNota ec= new TipoNota();
			TipoNota og= new TipoNota();
			TipoNota ng= new TipoNota();
			TipoNota ie= new TipoNota();
			
			sp.setInicial("SP");
			sp.setTipo("Servicios Públicos");
			
			in.setInicial("IN");
			in.setTipo("Infraestructura");
			
			tt.setInicial("TT");
			tt.setTipo("Transporte y Transito");
			
			se.setInicial("SE");
			se.setTipo("Seguridad");
			
			ma.setInicial("MA");
			ma.setTipo("Medio Ambiente");
			
			vu.setInicial("VU");
			vu.setTipo("Vivienda y Urbanismo");
			
			ov.setInicial("OV");
			ov.setTipo("Organizaciones Vecinales");
			
			ec.setInicial("EC");
			ec.setTipo("Empresas y Comercios");
			
			og.setInicial("OG");
			og.setTipo("Organismos Gubernamentales");
			
			ng.setInicial("NG");
			ng.setTipo("Organizaciones No Gubernamentales (ONGs)");
			
			ie.setInicial("IE");
			ie.setTipo("Instituciones Educativas y Culturales");
			
			tipoNotaRepo.save(sp);
			tipoNotaRepo.save(in);
			tipoNotaRepo.save(tt);
			tipoNotaRepo.save(se);
			tipoNotaRepo.save(ma);
			tipoNotaRepo.save(vu);
			tipoNotaRepo.save(ov);
			tipoNotaRepo.save(ec);
			tipoNotaRepo.save(og);
			tipoNotaRepo.save(ng);
			tipoNotaRepo.save(ie);
			
		}
		
		if (tipoCiudadanoRepo.count() == 0) {
			
			TipoCiudadano individuo = new TipoCiudadano();
			TipoCiudadano privado = new TipoCiudadano();
			TipoCiudadano publico = new TipoCiudadano();
			
			individuo.setTipoUsuario("Individuo");
			individuo.setInicial("IN");
			
			privado.setTipoUsuario("Privado");
			privado.setInicial("PR");
			
			publico.setTipoUsuario("Público");
			publico.setInicial("PU");
			
			tipoCiudadanoRepo.save(individuo);
			tipoCiudadanoRepo.save(privado);
			tipoCiudadanoRepo.save(publico);
		}
		
	}

}
