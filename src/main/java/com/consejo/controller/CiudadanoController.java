package com.consejo.controller;

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
	public String buscarCiudadano (Model modelo, @RequestParam("dni") Long dni, HttpSession session) {
		
		modelo.addAttribute("formBean", form);
		// Buscar en la base de datos usando el DNI
        Persona ciudadano = personaServi.buscarPersona(dni);
        
        
        if (ciudadano != null) {
        	frmExpediente.setDni(ciudadano.getDni_Cuit());
        	frmExpediente.setDireccion(ciudadano.getDireccion());
        	frmExpediente.setTelefono(ciudadano.getTelefono());
        	frmExpediente.setMail(ciudadano.getMail());
        	frmExpediente.setNombre(ciudadano.getNombre());
        	frmExpediente.setTipoCiudadano(ciudadano.getTipo().getId());
        	
        	//redirectAttributes.addFlashAttribute("formIngresoNota", frmExpediente);
        	session.setAttribute("formIngresoNota", frmExpediente);
        	
        	return "redirect:/mesa-entrada/ingresoNota";
        	
        	
        } else {
            // Si no existe, pasamos un ciudadano vacío¿
            modelo.addAttribute("formCiudadano", new ExpedienteForm ());
            frmExpediente.setDni(dni);
            return "redirect:/mesa-entrada/registroCiudadano";
        }
		
		
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
		
		ciudadano.setDni_Cuit(frmExpediente.getDni());
		ciudadano.setNombre(registroCiudadano.getNombre());
		ciudadano.setDireccion(registroCiudadano.getDireccion());
		ciudadano.setMail(registroCiudadano.getMail());
		ciudadano.setTelefono(registroCiudadano.getTelefono());
		ciudadano.setTipo(tipoCiudadanoServi.buscarTipoCiudadano(registroCiudadano.getTipoCiudadano()));
		
		personaServi.guardarPersona(ciudadano);
		
		registroCiudadano.setDni(frmExpediente.getDni());
		
		session.setAttribute("formIngresoNota", registroCiudadano);
    	return "redirect:/mesa-entrada/ingresoNota";

	}
	
	
}
