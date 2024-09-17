package com.consejo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.consejo.daos.ExpedienteDaos;
import com.consejo.daos.IngresoDiarioDaos;
import com.consejo.daos.MovimientoDaos;
import com.consejo.daos.NotaDaos;
import com.consejo.daos.PersonaDaos;
import com.consejo.daos.TipoCiudadanoDaos;
import com.consejo.daos.TipoNotaDaos;
import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.form.BusquedaForm;
import com.consejo.form.ExpedienteForm;
import com.consejo.form.FrmExpedienteMover;
import com.consejo.form.NroExpedienteForm;
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
	private NroExpedienteForm nExpedeinteForm = new NroExpedienteForm();

	/*
	 * mesa entrada Ingreso de nota/expediente
	 */
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
		modelo.addAttribute("tipociudadano",
				tipoCiudadanoServi.buscarTipoCiudadano(expedienteform.getTipoCiudadano()).toString());
		modelo.addAttribute("organizacion", expedienteform.getOrganizacion());
		modelo.addAttribute("tiposNota", tiponotas);
		return "ingresoNota"; // Nombre de la plantilla Thymeleaf
	}

	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@PostMapping("/mesa-entrada/ingresoNota")
	public String registrarCiudadano(@ModelAttribute("formIngresoNota") ExpedienteForm registroCiudadano, Model modelo)
			throws IOException {

		Expediente exp = new Expediente();
		Movimiento mov = new Movimiento();
		Nota nota = new Nota();
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

		exp.generateId(persona.getTipo().getInicial(), tNota.getInicial(), LocalDate.now(),
				ingDiarioServi.getNumeroDeIngreso());
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
		exp.setEstado(CircuitoExpediente.INGRESO);

		expedienteServi.guardarExpediente(exp);
		movimientoServi.agregarMovimiento(mov);

		// Agregar el número de expediente al modelo
		nExpedeinteForm.setnExpediente(exp.getId());

		// Redirigir a la vista de confirmación
		return "redirect:/mesa-entrada/expedienteConfirmacion";

	}

	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/expedienteConfirmacion")
	public String mostrarConfirmacion(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	
	/*
	 * administracion movimiento de expediente
	 */
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/expediente/ingresos")
	public String expedienteIngresos(Model modelo) {
		
        String url = "/administrador/expediente/mover/{id}";
        String s = "Expedientes - Ingresos";
		
		modelo.addAttribute("titulo", s);
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.INGRESO));
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/expediente/mover/{id}")
	public String moverExpediente(@PathVariable String id, Model modelo) {

		
	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	            CircuitoExpediente.OFICINA_PARLAMENTARIA,
	            CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
	            CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
	            CircuitoExpediente.AMBAS_COMISIONES,
	            CircuitoExpediente.ARCHIVO,
	            CircuitoExpediente.PRESIDENCIA,
	            CircuitoExpediente.BLOQUE_A,
	            CircuitoExpediente.BLOQUE_B,
	            CircuitoExpediente.BLOQUE_C,
	            CircuitoExpediente.TODOS_LOS_BLOQUES
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/administrador/expediente/mover/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);
	        

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/administrador/expediente/mover/{id}")
	public String circuitoExpedienteInicial(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/administrador/expediente/mover/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/expediente/mover/expedienteConfirmacion")
	public String mostrarConfirmacionMovimientoInicial(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	
	/*
	 * concejal comision de gobierno y desarrollo social
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/gobiernoYDesarrolloSocial")
	public String gobiernoYDesarrolloSocial(Model modelo) {

		String url = "/concejal/comision/gobiernoYDesarrolloSocial/{id}";
		String s = "Expedientes - Comisión de Gobierno y Desarrollo Social";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL));
		modelo.addAttribute("titulo", s);
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/gobiernoYDesarrolloSocial/{id}")
	public String gobiernoYDesarrolloSocialMovimiento(@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	        		CircuitoExpediente.DESPACHOS_DE_COMISION,
		            CircuitoExpediente.NOTAS_DE_COMISION
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/comision/gobiernoYDesarrolloSocial/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/comision/gobiernoYDesarrolloSocial/{id}")
	public String gobiernoYDesarrolloSocialMovimientoPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/comision/gobiernoYDesarrolloSocial/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/gobiernoYDesarrolloSocial/expedienteConfirmacion")
	public String confirmacionDesarrolloSocial(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	
	/*
	 * consejal comision de desarrollo urbano y economia
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/desarrolloUrbanoyEconomia")
	public String desarrolloUrbanoyEconomia(Model modelo) {

		String s = "Expedientes - Comisión de Desarrollo Urbano, Ambiental y Economía";
		String url = "/concejal/comision/desarrolloUrbanoyEconomia/{id}";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/desarrolloUrbanoyEconomia/{id}")
	public String desarrolloUrbanoYEconomia(@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	        		CircuitoExpediente.DESPACHOS_DE_COMISION,
		            CircuitoExpediente.NOTAS_DE_COMISION
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/comision/desarrolloUrbanoyEconomia/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/comision/desarrolloUrbanoyEconomia/{id}")
	public String desarrolloUrbanoYEconomiaPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/comision/desarrolloUrbanoyEconomia/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/desarrolloUrbanoyEconomia/expedienteConfirmacion")
	public String confirmacionUrbanoYEconomia(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	/*
	 * concejal ambas comisiones
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/ambasComisiones")
	public String ambasComisiones(Model modelo) {

		String s = "Expedientes - Ambas Comisiones";
		String url = "/concejal/comision/ambasComisiones/{id}";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.AMBAS_COMISIONES));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}
	
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/ambasComisiones/{id}")
	public String ambasComisiones(@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	        		CircuitoExpediente.DESPACHOS_DE_COMISION,
		            CircuitoExpediente.NOTAS_DE_COMISION
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/comision/ambasComisiones/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/comision/ambasComisiones/{id}")
	public String ambasComisionesPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/comision/ambasComisiones/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/ambasComisiones/expedienteConfirmacion")
	public String confirmacionAmbasComisiones(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	/*
	 * concejal archivo
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/archivo")
	public String archivo(Model modelo) {

		String s = "Expedientes - Archivo";
		String url = "/concejal/comision/ambasComisiones/{id}";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.ARCHIVO));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/archivo/{id}")
	public String archivo (@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	            CircuitoExpediente.DESPACHOS_DE_COMISION,
	            CircuitoExpediente.NOTAS_DE_COMISION
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/comision/archivo/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/comision/archivo/{id}")
	public String archivoPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/comision/archivo/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/comision/archivo/expedienteConfirmacion")
	public String confirmacionArchivo(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	
	/*
	 * Despacho Comisión
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/despachoDeComision")
	public String despachoComision(Model modelo) {

		String s = "Expedientes - Despacho de Comisíon";
		String url = "/concejal/despachoDeComision/{id}";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.DESPACHOS_DE_COMISION));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/despachoDeComision/{id}")
	public String despachoComision (@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	            CircuitoExpediente.LEGISLACION
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/despachoDeComision/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/despachoDeComision/{id}")
	public String despachoComisionPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/despachoDeComision/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/despachoDeComision/expedienteConfirmacion")
	public String confirmacionDespachoComision(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	
	/*
	 * notas de comision
	 */
	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/notaDeComision")
	public String notaComision(Model modelo) {

		String s = "Expedientes - Despacho de Comisíon";
		String url = "/concejal/notaDeComision/{id}";
		
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.NOTAS_DE_COMISION));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/notaDeComision/{id}")
	public String notaComision (@PathVariable String id, Model modelo) {

	    Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

	    if (expedienteop.isPresent()) {
	        Expediente expediente = expedienteServi.buscarExpediente(id);
	        FrmExpedienteMover expedientemover = new FrmExpedienteMover();
	        List<Nota> notas = expediente.getMovimientos().getLast().getNotas();

	        List<CircuitoExpediente> circuitos = Arrays.asList(
	            CircuitoExpediente.REPUESTA_A_DESTINATARIO,
	            CircuitoExpediente.REPUESTA_MUNICIPIO
	        );

	        expedientemover.setId(expediente.getId());
	        expedientemover.setFecha(expediente.getFecha());
	        expedientemover.setCaratula(expediente.getCaratula());
	        expedientemover.setNombre(expediente.getPersona().getNombre());
	        String urlForm = "/concejal/notaDeComision/{id}";
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
	        modelo.addAttribute("urlForm", urlForm);
	        modelo.addAttribute("caratula", expedientemover.getCaratula());
	        modelo.addAttribute("fecha", expedientemover.getFecha());
	        modelo.addAttribute("nombre", expedientemover.getNombre());
	        modelo.addAttribute("estadoActual", expediente.getEstado());
	        modelo.addAttribute("notas", notas);
	        modelo.addAttribute("circuitoDisponible", circuitos);

	        return "moverExpediente";
	    } else {
	        return "redirect:/error"; // Redirige a una página de error o de lista
	    }
	}
	

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@PostMapping("/concejal/notaDeComision/{id}")
	public String notaComisionPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {
				
				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir= expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (expMover.getNota() != null) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.getNotas().add(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			
            return "redirect:/concejal/notaDeComision/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	
	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/concejal/notaDeComision/expedienteConfirmacion")
	public String confirmacionNotaComision(Model modelo) {

		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	
}
