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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/expediente/ingresos")
	public String expedienteIngresos(Model modelo) {

		modelo.addAttribute("expedientes", expedienteServi.listarExpedientes(CircuitoExpediente.INGRESO));

		return "expedienteIngresos";
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
	        
	        modelo.addAttribute("frmExpedienteMover", expedientemover);
	        modelo.addAttribute("expediente", expedientemover);
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
	
	
	
}
