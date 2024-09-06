package com.consejo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.consejo.daos.ExpedienteDaos;
import com.consejo.daos.IngresoDiarioDaos;
import com.consejo.daos.MovimientoDaos;
import com.consejo.daos.NotaDaos;
import com.consejo.daos.PersonaDaos;
import com.consejo.daos.TipoCiudadanoDaos;
import com.consejo.daos.TipoNotaDaos;
import com.consejo.form.BusquedaForm;
import com.consejo.form.ExpedienteForm;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Nota;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoNota;

import jakarta.servlet.http.HttpSession;

@Controller
public class ExpedienteController {

	@Autowired
	private PersonaDaos ciudadanoServi;
	@Autowired
	private TipoCiudadanoDaos tipoCiudadanoServi;
	@Autowired
	private ExpedienteDaos expedienteServi;
	@Autowired
	private MovimientoDaos movimientoServi;
	@Autowired
	private NotaDaos notaServi;
	@Autowired
	private TipoNotaDaos tipoNotaServi;
	@Autowired
	private IngresoDiarioDaos ingDiarioServi;

	private ExpedienteForm expedienteform;

	// prestar atencion aca en el paso de datos entre ciudadano controller y aca
	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/ingresoNota")
	public String mostrarDatosCiudadano(HttpSession session, Model modelo) {

		
		List<TipoNota> tiponotas = tipoNotaServi.listarTipoNota();
		expedienteform = (ExpedienteForm) session.getAttribute("formIngresoNota");
		modelo.addAttribute("formIngresoNota", expedienteform);
		modelo.addAttribute("dni", expedienteform.getDni());
		modelo.addAttribute("nombre", expedienteform.getNombre());
		modelo.addAttribute("direccion", expedienteform.getDireccion());
		modelo.addAttribute("telefono", expedienteform.getTelefono());
		modelo.addAttribute("mail", expedienteform.getMail());
		modelo.addAttribute("tipociudadano",tipoCiudadanoServi.buscarTipoCiudadano(expedienteform.getTipoCiudadano()).toString());
		modelo.addAttribute("organizacion", expedienteform.getOrganizacion());
		modelo.addAttribute("tiposNota", tiponotas );
		return "ingresoNota"; // Nombre de la plantilla Thymeleaf
	}

	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@PostMapping("/mesa-entrada/ingresoNota")
	public String registrarCiudadano(@ModelAttribute("formIngresoNota") ExpedienteForm registroCiudadano, Model modelo) throws IOException {

		Expediente exp = new Expediente();
		Movimiento mov = new Movimiento();
		Nota nota = new Nota ();
		TipoNota tNota;
		Persona persona;
		
		persona = ciudadanoServi.buscarPersona(expedienteform.getDni());
		tNota = tipoNotaServi.buscarTipoNota(registroCiudadano.getTipoNota());
		
		nota.setEsActiva(true);
		nota.setTitulo(registroCiudadano.getTituloNota());
		
		byte[] contenidoArchivo = registroCiudadano.getNota().getBytes();
		nota.setNota(contenidoArchivo);
		
		mov.setDetalle("Ingreso al concejo.");
		mov.setFecha(LocalDate.now());
		
	   exp.generateId(persona.getTipo().getInicial(), tNota.getInicial(), LocalDate.now(), ingDiarioServi.getNumeroDeIngreso());
	   exp.setCaratula(registroCiudadano.getCaratula());
	   exp.setDetalle(registroCiudadano.getDetalle());
	   exp.setFecha(LocalDate.now());
	   exp.setPersona(persona);
	   
	   mov.generateId(exp.getId(), exp.getMovimientos().size());
	   mov.getNotas().add(nota);
	   mov.setExpediente(exp);
	   
	   nota.setHistorial(mov);
	   
	   exp.getMovimientos().add(mov);
	   exp.setTipo(tNota);
	   
	   expedienteServi.guardarExpediente(exp);
	   movimientoServi.agregarMovimiento(mov);
	   notaServi.guardarNota(nota);
	   
	   // Agregar el número de expediente al modelo
	   

	    // Redirigir a la vista de confirmación
	    return "redirect:/mesa-entrada/expedienteConfirmacion";

	}
	
	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/expedienteConfirmacion")
	public String mostrarConfirmacion(Model modelo) {
	    return "expedienteConfirmacion";  // Nombre del archivo Thymeleaf
	}

}
