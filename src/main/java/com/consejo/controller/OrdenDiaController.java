package com.consejo.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.consejo.daos.ExpedienteDaos;
import com.consejo.daos.OrdenDiaDaos;
import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.form.OrdenForm;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Nota;
import com.consejo.pojos.OrdenDia;
import com.consejo.repository.IExpedienteRepository;

@Controller
public class OrdenDiaController {

	@Autowired
	private OrdenDiaDaos ordenServi;
	@Autowired
	private ExpedienteDaos expedienteServi;
	@Autowired
	private IExpedienteRepository expedienteRepo;

	@PreAuthorize("hasRole('ROLE_SEC_ADMINISTRATIVO')")
	@GetMapping("/secretarioAdministrativo/subirOrdenDelDia")
	public String subirOrden(Model modelo) {

		OrdenForm frmOrden = new OrdenForm();
		List<Expediente> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.DESPACHOS_DE_COMISION);
		modelo.addAttribute("frmOrden", frmOrden);
		modelo.addAttribute("expedientes", expedientes);
		modelo.addAttribute("ruta", "/secretarioAdministrativo/subirOrdenDelDia");
		return "ordenDelDia";
	}

	@PreAuthorize("hasRole('ROLE_SEC_ADMINISTRATIVO')")
	@PostMapping("/secretarioAdministrativo/subirOrdenDelDia")
	public String subirNotaPost(@ModelAttribute("frmOrden") OrdenForm frmOrden, Model modelo) throws IOException {

		try {
			OrdenDia orden = new OrdenDia();
			byte[] ordenDia = frmOrden.getNota().getBytes();
			orden.setNota(ordenDia);
			orden.setTitulo(frmOrden.getTitulo());
			orden.setFecha(frmOrden.getFecha());
			List<Expediente> expedientesSeleccionados = expedienteRepo.findAllById(frmOrden.getExpedienteIds());
			orden.setExpedientes(expedientesSeleccionados);
			ordenServi.guardarNota(orden);
			return "redirect:/home";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/notas/error";
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/ordenDelDia/ver")
	public String verOrdenDia(Model modelo) {

		List<OrdenDia> ordenesDelDia = ordenServi.listarNota();
		modelo.addAttribute("titulo", "Órdenes del Día - Desde hoy en adelante");
		modelo.addAttribute("ordenesDelDia", ordenesDelDia);
		modelo.addAttribute("pdf", "/notas/ordenDia/{id}");
		modelo.addAttribute("ruta", "/expediente/ordenDia/{id}");

		return "verOrdenDia";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/notas/ordenDia/{id}")
	public ResponseEntity<byte[]> viewNota(@PathVariable Long id) {
		OrdenDia orden = ordenServi.BuscarNota(id);
		// Verificar si la orden es null
		if (orden == null || orden.getNota() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND); 
		}

		byte[] pdfContent = orden.getNota(); 

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);

		return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/expediente/ordenDia/{id}")
	public String ordenExpedientes (@PathVariable Long id, Model modelo) {
		
		OrdenDia orden = ordenServi.BuscarNota(id);
		List<Expediente> expedientes = ordenServi.BuscarNota(id).getExpedientes();
		modelo.addAttribute("expedientes", expedientes);
		modelo.addAttribute("orden", orden);
		modelo.addAttribute("ruta", "/ordenDelDia/tratado/{id}");
		
		return "expedienteOrdendia";
		
	}
	
	@PreAuthorize("hasRole('SEC_PARLAMENTARIO')")
	@PostMapping("/ordenDelDia/tratado/{id}")
	public String tratado (@PathVariable Long id) throws IOException {
		
		OrdenDia orden = ordenServi.BuscarNota(id);
		orden.setTratado(true);
		ordenServi.modificarOrden(orden);
		
		return "redirect:/home";
	}
}
