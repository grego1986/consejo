package com.consejo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.consejo.daos.EventoDaos;
import com.consejo.form.eventoForm;
import com.consejo.pojos.Evento;

@Controller
public class EventoController {

	@Autowired
	private EventoDaos eventoServi;
	
	@PreAuthorize("hasRole('ENTRADA')")
	@GetMapping("/mesa-entrada/eventos/agregar")
	public String evento (Model modelo) {
		
		eventoForm evento = new eventoForm();
		modelo.addAttribute("evento", evento);
		return "eventosingresar";
	}
	
	@PreAuthorize("hasRole('ENTRADA')")
	@PostMapping("/mesa-entrada/eventos/agregar")
	public String evento (@ModelAttribute("evento") eventoForm evento) {
		
		Evento e = new Evento();
		e.setEvento(evento.getEvento());
		e.setFecha(evento.getFecha());
		e.setHora(evento.getHora());
		e.setLugar(evento.getDireccion());
		eventoServi.agregarEvento(e);
		
		return "redirect:/home";
		
	}
}
