package com.consejo.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.consejo.daos.ExpedienteDaos;
import com.consejo.daos.IngresoDiarioDaos;
import com.consejo.daos.MovimientoDaos;
import com.consejo.daos.NotaDaos;
import com.consejo.daos.PersonaDaos;
import com.consejo.daos.TipoCiudadanoDaos;
import com.consejo.daos.TipoNotaDaos;
import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.form.BuscarExpedienteForm;
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
	private TipoNotaDaos tipoNotaServi;
	@Autowired
	private IngresoDiarioDaos ingDiarioServi;

	private ExpedienteForm expedienteform;
	private NroExpedienteForm nExpedeinteForm = new NroExpedienteForm();
	private String movimiento;

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
		mov.setNota(nota);
		mov.setExpediente(exp);

		nota.setHistorial(mov);

		exp.getMovimientos().add(mov);
		exp.setTipo(tNota);
		exp.setEstado(CircuitoExpediente.INGRESO);
		exp.setFincircuito(false);

		expedienteServi.guardarExpediente(exp);

		// Agregar el número de expediente al modelo
		nExpedeinteForm.setnExpediente(exp.getId());

		// Redirigir a la vista de confirmación
		return "redirect:/mesa-entrada/expedienteConfirmacion";

	}

	@PreAuthorize("hasRole('ROLE_ENTRADA')")
	@GetMapping("/mesa-entrada/expedienteConfirmacion")
	public String mostrarConfirmacion(Model modelo) {

		modelo.addAttribute("titulo", "Numero de expediente asignado");
		modelo.addAttribute("nExpediente", nExpedeinteForm.getnExpediente());
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * Oficina parlamentaria Repuesta desde el municipio
	 * 
	 */
	
	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/repuestaMunicipio")
	public String ofParlamentario(Model modelo) {

		String url = "/secParlamentario/repuestaMunicipio/mover/{id}";
		String s = "Expedientes - Ingresos";

		modelo.addAttribute("titulo", s);
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.OFICINA_PARLAMENTARIA));
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/repuestaMunicipio/mover/{id}")
	public String ofParlamentario(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(
					CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
					CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
					CircuitoExpediente.AMBAS_COMISIONES, CircuitoExpediente.ARCHIVO
			// CircuitoExpediente.BLOQUE_A,
			// CircuitoExpediente.BLOQUE_B,
			// CircuitoExpediente.BLOQUE_C,
			// CircuitoExpediente.TODOS_LOS_BLOQUES
			);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/secParlamentario/repuestaMunicipio/mover/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@PostMapping("/secParlamentario/repuestaMunicipio/mover/{id}")
	public String ofParlamentarioPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			
            //nExpedeinteForm.setnExpediente(expediente.getId());
            movimiento = mov.getId();
			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());
			

			return "redirect:/secParlamentario/repuestaMunicipio/mover/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/repuestaMunicipio/mover/expedienteConfirmacion")
	public String mostrarConfirmacionMovimientoofParlamentario(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}
	
	/*
	 * administracion movimiento de expediente
	 */

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/expediente/ingresos")
	public String expedienteIngresos(Model modelo) {

		String url = "/secParlamentario/expediente/mover/{id}";
		String s = "Expedientes - Ingresos";

		modelo.addAttribute("titulo", s);
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.INGRESO));
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/expediente/mover/{id}")
	public String moverExpediente(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(
					CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
					CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
					CircuitoExpediente.AMBAS_COMISIONES, CircuitoExpediente.ARCHIVO
			// CircuitoExpediente.BLOQUE_A,
			// CircuitoExpediente.BLOQUE_B,
			// CircuitoExpediente.BLOQUE_C,
			// CircuitoExpediente.TODOS_LOS_BLOQUES
			);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/secParlamentario/expediente/mover/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@PostMapping("/secParlamentario/expediente/mover/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/secParlamentario/expediente/mover/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/secParlamentario/expediente/mover/expedienteConfirmacion")
	public String mostrarConfirmacionMovimientoInicial(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * concejal comision de gobierno y desarrollo social
	 */

	@PreAuthorize("hasAnyRole('ROLE_PRESIDENTE', 'ROLE_SEC_PARLAMENTARIO', 'ROLE_CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/gobiernoYDesarrolloSocial")
	public String gobiernoYDesarrolloSocial(Model modelo) {

		String url = "/comision/gobiernoYDesarrolloSocial/{id}";
		String s = "Expedientes - Comisión de Gobierno y Desarrollo Social";

		modelo.addAttribute("expedientes",
				expedienteServi.listarExpedientes(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL));
		modelo.addAttribute("titulo", s);
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/gobiernoYDesarrolloSocial/{id}")
	public String gobiernoYDesarrolloSocialMovimiento(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
					CircuitoExpediente.NOTAS_DE_COMISION);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/comision/gobiernoYDesarrolloSocial/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@PostMapping("/comision/gobiernoYDesarrolloSocial/{id}")
	public String gobiernoYDesarrolloSocialMovimientoPost(
			@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover, @PathVariable String id, Model modelo)
			throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			Nota nota = new Nota();
			mov.setFecha(LocalDate.now());

			if (expMover.getDetalleMovimiento() != null) {

				mov.setDetalle(expMover.getDetalleMovimiento());
			} else {
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/comision/gobiernoYDesarrolloSocial/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasRole('ROLE_CONCEJAL')")
	@GetMapping("/comision/gobiernoYDesarrolloSocial/expedienteConfirmacion")
	public String confirmacionDesarrolloSocial(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * consejal comision de desarrollo urbano y economia
	 */

	@PreAuthorize("hasAnyRole('ROLE_CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'ROLE_PRESIDENTE', 'ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/comision/desarrolloUrbanoAmbientalYEconomia")
	public String desarrolloUrbanoyEconomia(Model modelo) {

		String s = "Expedientes - Comisión de Desarrollo Urbano, Ambiental y Economía";
		String url = "/comision/desarrolloUrbanoAmbientalYEconomia/{id}";

		modelo.addAttribute("expedientes", expedienteServi
				.listarExpedientes(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO')")
	@GetMapping("/comision/desarrolloUrbanoAmbientalYEconomia/{id}")
	public String desarrolloUrbanoYEconomia(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
					CircuitoExpediente.NOTAS_DE_COMISION);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/comision/desarrolloUrbanoAmbientalYEconomia/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO')")
	@PostMapping("/comision/desarrolloUrbanoAmbientalYEconomia/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/comision/desarrolloUrbanoAmbientalYEconomia/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO')")
	@GetMapping("/comision/desarrolloUrbanoAmbientalYEconomia/expedienteConfirmacion")
	public String confirmacionUrbanoYEconomia(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * concejal ambas comisiones
	 */

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/ambasComisiones")
	public String ambasComisiones(Model modelo) {

		String s = "Expedientes - Ambas Comisiones";
		String url = "/comision/ambasComisiones/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.AMBAS_COMISIONES));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/ambasComisiones/{id}")
	public String ambasComisiones(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
					CircuitoExpediente.NOTAS_DE_COMISION);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/comision/ambasComisiones/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@PostMapping("/comision/ambasComisiones/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/comision/ambasComisiones/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO', 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/ambasComisiones/expedienteConfirmacion")
	public String confirmacionAmbasComisiones(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * concejal archivo
	 */

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/expediente/archivo")
	public String archivo(Model modelo) {

		String s = "Expedientes - Archivo";
		String url = "/expediente/archivo/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.ARCHIVO));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/expediente/archivo/{id}")
	public String archivo(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
					CircuitoExpediente.NOTAS_DE_COMISION);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/expediente/archivo/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@PostMapping("/expediente/archivo/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/expediente/archivo/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/expediente/archivo/expedienteConfirmacion")
	public String confirmacionArchivo(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * Despacho Comisión
	 */

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/despachoDeComision")
	public String despachoComision(Model modelo) {

		String s = "Expedientes - Despacho de Comisíon";
		String url = "/comision/despachoDeComision/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.DESPACHOS_DE_COMISION));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/despachoDeComision/{id}")
	public String despachoComision(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.LEGISLACION);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/comision/despachoDeComision/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@PostMapping("/comision/despachoDeComision/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/comision/despachoDeComision/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/despachoDeComision/expedienteConfirmacion")
	public String confirmacionDespachoComision(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * notas de comision
	 */

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/notaDeComision")
	public String notaComision(Model modelo) {

		String s = "Expedientes - Despacho de Comisíon";
		String url = "/comision/notaDeComision/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.NOTAS_DE_COMISION));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/notaDeComision/{id}")
	public String notaComision(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.REPUESTA_AL_CIUDADANO,
					CircuitoExpediente.NOTA_AL_MUNICIPIO);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/comision/notaDeComision/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@PostMapping("/comision/notaDeComision/{id}")
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
				CircuitoExpediente cir = expMover.getCircuito();
				String estado = cir.toString();
				mov.setDetalle("Ingresó a " + estado.replace("_", " "));
			}

			if (!expMover.getNota().isEmpty()) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);
			movimiento = mov.getId();

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/comision/notaDeComision/expedienteConfirmacion";
		}

		return "redirect:/error";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/comision/notaDeComision/expedienteConfirmacion")
	public String confirmacionNotaComision(Model modelo) {

		String titulo = "Numero de movimiento asignado";
		modelo.addAttribute("titulo", titulo );
		
		modelo.addAttribute("nExpediente", movimiento);
		return "expedienteConfirmacion"; // Nombre del archivo Thymeleaf
	}

	/*
	 * buscar expedientes
	 */
	@GetMapping("/expediente/buscarExpediente")
	public String buscarExpedientes(Model modelo) {

		modelo.addAttribute("formBuscarExpediente", new BuscarExpedienteForm());

		return "buscarExpediente";

	}

	/*
	 * nota al municipio
	 */

	@PreAuthorize("hasAnyRole('ROLE_SEC_PARLAMENTARIO', 'ENTRADA')")
	@GetMapping("/mesa-entrada/notaAMunicipio")
	public String notaAMunicipio(Model modelo) {

		String url = "/mesa-entrada/notaAMunicipio/mover/{id}";
		String s = "Expedientes - Nota al Municipio";

		modelo.addAttribute("titulo", s);
		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.NOTA_AL_MUNICIPIO));
		modelo.addAttribute("urlMover", url);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasRole('ROLE_SEC_PARLAMENTARIO')")
	@GetMapping("/mesa-entrada/notaAMunicipio/mover/{id}")
	public String notaMunicipio(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO,
			CircuitoExpediente.FIN
			// CircuitoExpediente.BLOQUE_A,
			// CircuitoExpediente.BLOQUE_B,
			// CircuitoExpediente.BLOQUE_C,
			// CircuitoExpediente.TODOS_LOS_BLOQUES
			);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/mesa-entrada/notaAMunicipio/mover/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "comunicacionMunicipio";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('ROLE_SEC_PARLAMENTARIO', 'ENTRADA')")
	@PostMapping("/mesa-entrada/notaAMunicipio/mover/{id}")
	public String notaMunicipioPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
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
					
					mov.setDetalle("Espera de repuesta desde municipalidad.");
				
			}

			if ((expMover.getNota() != null) &&(!expMover.getNota().isEmpty()) ) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo(expMover.getTituloNota());
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/mesa-entrada/notaAMunicipio";
		}

		return "redirect:/error";
	}

	/*
	 * REPUESTA A CIUDADANO
	 */

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL', 'ENTRADA')")
	@GetMapping("/mesa-entrada/repuestaACiudadano")
	public String repuestaCiudadano(Model modelo) {

		String s = "Expedientes - Repuesta al ciudadano";
		String url = "/mesa-entrada/repuestaACiudadano/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.REPUESTA_AL_CIUDADANO));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL', 'ENTRADA')")
	@GetMapping("/mesa-entrada/repuestaACiudadano/{id}")
	public String repuestaCiudadano(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.FIN);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/mesa-entrada/repuestaACiudadano/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "repuestaCiudadano";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL', 'ENTRADA')")
	@PostMapping("/mesa-entrada/repuestaACiudadano/{id}")
	public String repuestaCiudadanoPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
			@PathVariable String id, Model modelo) throws IOException {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			Movimiento mov = new Movimiento();
			mov.setFecha(LocalDate.now());

			expMover.setCircuito(CircuitoExpediente.FIN);

			CircuitoExpediente cir = expMover.getCircuito();
			String estado = cir.toString();
			mov.setDetalle("el expediente llegó a su " + estado.replace("_", " "));

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/home";
		}

		return "redirect:/error";
	}
	
	/*
	 * Repuesta municipio
	 */
	
	@PreAuthorize("hasAnyRole('ROLE_SEC_PARLAMENTARIO', 'ENTRADA')")
	@GetMapping("/mesa-entrada/repuestaMunicipio")
	public String repuestaMunicipio(Model modelo) {

		String s = "Expedientes - Repuesta del Municipio";
		String url = "/mesa-entrada/repuestaMunicipio/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL', 'ENTRADA')")
	@GetMapping("/mesa-entrada/repuestaMunicipio/{id}")
	public String repuestaMunicipio(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.OFICINA_PARLAMENTARIA);

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());
			String urlForm = "/mesa-entrada/repuestaMunicipio/{id}";

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return "comunicacionMunicipio";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL', 'ENTRADA')")
	@PostMapping("/mesa-entrada/repuestaMunicipio/{id}")
	public String repuestaMunicipioPost(@ModelAttribute("frmExpedienteMover") FrmExpedienteMover expMover,
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
					
					mov.setDetalle("Espera de repuesta desde municipalidad.");
				
			}

			if ((expMover.getNota() != null && !expMover.getNota().isEmpty())) {

				byte[] contenidoArchivo = expMover.getNota().getBytes();
				nota.setNota(contenidoArchivo);
				nota.setTitulo("repuesta municipio");
				nota.setHistorial(mov);
				nota.setEsActiva(true);
				mov.setNota(nota);
			}

			mov.generateId(expediente.getId(), expediente.getMovimientos().size());
			expediente.getMovimientos().add(mov);

			expedienteServi.agregarMovimiento(id, mov, expMover.getCircuito());

			return "redirect:/mesa-entrada/repuestaMunicipio";
		}

		return "redirect:/error";
	}

	/*
	 * Buscar expediente
	 */

	@GetMapping("/expediente/buscarExpediente/resultado")
	public String buscarExpedientes(@RequestParam(required = false) LocalDate fecha,
			@RequestParam(required = false) String detalle, @RequestParam(required = false) String id,
			@RequestParam(required = false) String caratula, @RequestParam(required = false) String nombrePersona,
			Model model) {

		List<Expediente> expedientes = expedienteServi.buscarExpediente(fecha, detalle, id, caratula, nombrePersona);
		model.addAttribute("expedientes", expedientes);
		model.addAttribute("fecha", null);
		model.addAttribute("detalle", "");
		model.addAttribute("id", "");
		model.addAttribute("caratula", "");
		model.addAttribute("nombrePersona", "");

		return "buscarExpediente";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/expediente/ver/{id}")
	public String verExpediente(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			CircuitoExpediente circuito = expediente.getEstado();

			List<CircuitoExpediente> circuitos = new ArrayList();
			String urlForm = "";

			switch (expediente.getEstado()) {

			case INGRESO:
				circuitos = Arrays.asList(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
						CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
						CircuitoExpediente.AMBAS_COMISIONES, CircuitoExpediente.ARCHIVO);
				urlForm = "redirect:/secParlamentario/expediente/mover/{id}";
				break;

			case COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL:
				circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
						CircuitoExpediente.NOTAS_DE_COMISION);

				urlForm = "redirect:/comision/gobiernoYDesarrolloSocial/{id}";
				break;

			case COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA:
				circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
						CircuitoExpediente.NOTAS_DE_COMISION);
				urlForm = "redirect:/comision/desarrolloUrbanoAmbientalYEconomia/{id}";
				break;

			case AMBAS_COMISIONES:
				circuitos = Arrays.asList(CircuitoExpediente.DESPACHOS_DE_COMISION,
						CircuitoExpediente.NOTAS_DE_COMISION);
				urlForm = "redirect:/comision/ambasComisiones/{id}";
				break;

			case ARCHIVO:

				circuitos = Arrays.asList(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
						CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
						CircuitoExpediente.AMBAS_COMISIONES);
				urlForm = "redirect:/expediente/archivo/{id}";
				break;

			case PRESIDENCIA:

				break;

			case BLOQUE_A:

				break;

			case BLOQUE_B:

				break;

			case BLOQUE_C:

				break;

			case TODOS_LOS_BLOQUES:

				break;
				
			case FIN:
				urlForm = "moverExpediente";
				break;

			case LEGISLACION:

				urlForm = "moverExpediente";
				break;

			case DESPACHOS_DE_COMISION:
				circuitos = Arrays.asList(CircuitoExpediente.LEGISLACION);
				urlForm = "redirect:redirect:/comision/despachoDeComision/{id}";
				break;

			case NOTAS_DE_COMISION:
				circuitos = Arrays.asList(CircuitoExpediente.NOTA_AL_MUNICIPIO,
						CircuitoExpediente.REPUESTA_AL_CIUDADANO);
				urlForm = "redirect:/comision/notaDeComision/{id}";
				break;

			case REPUESTA_AL_CIUDADANO:
				circuitos = Arrays.asList(CircuitoExpediente.FIN);
				urlForm = "moverExpediente";
				break;

			case NOTA_AL_MUNICIPIO:
				circuitos = Arrays.asList(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO);
				urlForm = "moverExpediente";
				break;

			case REPUESTA_DEL_MUNICIPIO:
				circuitos = Arrays.asList(CircuitoExpediente.NOTA_AL_MUNICIPIO,
						CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
						CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
						CircuitoExpediente.AMBAS_COMISIONES,
						CircuitoExpediente.ARCHIVO,
						CircuitoExpediente.DESPACHOS_DE_COMISION);
				urlForm = "moverExpediente";
				break;

			default:
				System.out.println("Estado no reconocido.");
				break;

			}

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("urlForm", urlForm);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);
			modelo.addAttribute("circuitoDisponible", circuitos);

			return urlForm;
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}

	/*
	 * Expedientes visto por prensa
	 */
	

	@PreAuthorize("hasRole('PRENSA')")
	@GetMapping("/prensa/ingresosAComision")
    public String buscarExpedientesPorMovimiento(
    		@RequestParam(required = false) LocalDate startDate,
        Model model) {

        List<Expediente> expedientes = expedienteServi.buscarPorRangoDeFechasYCircuitos(startDate, LocalDate.now());
        model.addAttribute("expedientes", expedientes);
        return "prensaIngresosComision";
    }
	
	@PreAuthorize("hasRole('PRENSA')")
	@GetMapping("/prensa/expediente/ver/{id}")
	public String prensaVerExpediente(@PathVariable String id, Model modelo) {

		Optional<Expediente> expedienteop = expedienteServi.buscarExpedienteOptional(id);

		if (expedienteop.isPresent()) {
			Expediente expediente = expedienteServi.buscarExpediente(id);
			FrmExpedienteMover expedientemover = new FrmExpedienteMover();
			List<Movimiento> movimientos = expediente.getMovimientos();

			expedientemover.setId(expediente.getId());
			expedientemover.setFecha(expediente.getFecha());
			expedientemover.setCaratula(expediente.getCaratula());
			expedientemover.setNombre(expediente.getPersona().getNombre());

			modelo.addAttribute("frmExpedienteMover", expedientemover);
			modelo.addAttribute("expediente", expedientemover);
			modelo.addAttribute("caratula", expedientemover.getCaratula());
			modelo.addAttribute("fecha", expedientemover.getFecha());
			modelo.addAttribute("nombre", expedientemover.getNombre());
			modelo.addAttribute("estadoActual", expediente.getEstado());
			modelo.addAttribute("movimientos", movimientos);

			return "moverExpediente";
		} else {
			return "redirect:/error"; // Redirige a una página de error o de lista
		}
	}
	
	
	@PreAuthorize("hasRole('ROLE_SEC_ADMINISTRATIVO')")
	@GetMapping("/secretarioAdministrativo/despachoDeComision")
	public String despachos (Model modelo) {
		
		String s = "Expedientes - Despachos de Comision";
		String url = "/secretarioAdministrativo/despachoDeComision/{id}";

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.DESPACHOS_DE_COMISION));
		modelo.addAttribute("urlMover", url);
		modelo.addAttribute("titulo", s);

		return "expedienteConsulta";
		
	}
	

}
