package com.consejo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.consejo.daos.IPersonaDaos;
import com.consejo.daos.PersonaDaos;
import com.consejo.daos.TipoCiudadanoDaos;
import com.consejo.form.BusquedaForm;
import com.consejo.form.ExpedienteForm;
import com.consejo.form.FrmRegistroUsuario;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoCiudadano;

import jakarta.servlet.http.HttpSession;

@Controller
public class CiudadanoController {

	@Autowired
	private PersonaDaos personaServi;
	@Autowired
	private TipoCiudadanoDaos tipoCiudadanoServi;
	List <Persona> ciudadanos = new ArrayList<>();
	
	
	private BusquedaForm form = new BusquedaForm();
	public ExpedienteForm frmExpediente = new ExpedienteForm();
	
	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/ciudadanoBuscar")
	public String abrirIndex(Model modelo) {
		
		modelo.addAttribute("formBean", form);
        return "ciudadanoBuscar";
    }

	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@PostMapping("/mesa-entrada/ciudadanoBuscar")
	public String buscarCiudadano (Model modelo, @RequestParam("nombre") String nombre, HttpSession session) {
		
		modelo.addAttribute("formBean", form);
        ciudadanos = personaServi.listarPersonas(nombre);
        
        modelo.addAttribute("personas", ciudadanos);
        
        return "ciudadanoBuscar";
	}
	
	
	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/registroCiudadano")
	public String registroCiudadano(Model modelo) {
		
		List<TipoCiudadano> tipos = tipoCiudadanoServi.listarTipoCiudadano();
		modelo.addAttribute("formCiudadano", new ExpedienteForm ());
		modelo.addAttribute("dni", frmExpediente.getDni());
		modelo.addAttribute("tipoCiudadanos", tipos);
        return "registroCiudadano";
    }
	
	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@PostMapping("/mesa-entrada/ciudadanoRegistrado")
	public String registrarCiudadano(@ModelAttribute("formCiudadano") ExpedienteForm registroCiudadano, Model modelo,  HttpSession session) {
		
		Persona ciudadano = new Persona();
		
		ciudadano.setDni_Cuit(registroCiudadano.getDni());
		ciudadano.setNombre(registroCiudadano.getNombre());
		ciudadano.setDireccion(registroCiudadano.getDireccion());
		ciudadano.setMail(registroCiudadano.getMail());
		ciudadano.setTelefono(registroCiudadano.getTelefono());
		ciudadano.setTipo(tipoCiudadanoServi.buscarTipoCiudadano(registroCiudadano.getTipoCiudadano()));
		
		personaServi.guardarPersona(ciudadano);
				
		session.setAttribute("formIngresoNota", registroCiudadano);
    	return "redirect:/mesa-entrada/ingresoNota/" + ciudadano.getDni_Cuit();

	}
	
	
}
