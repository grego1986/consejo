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

import com.consejo.daos.AsuntosEntradosDaos;
import com.consejo.daos.ExpedienteDaos;
import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.form.OrdenForm;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Expediente;
import com.consejo.repository.IExpedienteRepository;

@Controller
public class AsuntosEntradosController {

	@Autowired
	private ExpedienteDaos expedienteServi;
	@Autowired
	private IExpedienteRepository expedienteRepo;
	@Autowired
	private AsuntosEntradosDaos asuntosServi;
	
	@PreAuthorize("hasRole('ROLE_SEC_ADMINISTRATIVO')")
	@GetMapping("/secretarioAdministrativo/subirAsuntosEntrados")
	public String subirOrden(Model modelo) {

		OrdenForm frmOrden = new OrdenForm();
		List<Expediente> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.INGRESO);
		modelo.addAttribute("titulo", "Asuntos Entrados");
		modelo.addAttribute("frmOrden", frmOrden);
		modelo.addAttribute("expedientes", expedientes);
		return "ordenDelDia";
	}

	@PreAuthorize("hasRole('ROLE_SEC_ADMINISTRATIVO')")
	@PostMapping("/secretarioAdministrativo/subirAsuntosEntrados")
	public String subirNotaPost(@ModelAttribute("frmOrden") OrdenForm frmOrden, Model modelo) throws IOException {

		try {
			AsuntosEntrados orden = new AsuntosEntrados();
			byte[] ordenDia = frmOrden.getNota().getBytes();
			orden.setNota(ordenDia);
			orden.setTitulo(frmOrden.getTitulo());
			orden.setFecha(frmOrden.getFecha());
			List<Expediente> expedientesSeleccionados = expedienteRepo.findAllById(frmOrden.getExpedienteIds());
			orden.setExpedientes(expedientesSeleccionados);
			asuntosServi.guardarNota(orden);
			return "redirect:/home";
		} catch (IOException e) {
			e.printStackTrace();
			return "redirect:/notas/error";
		}
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/asuntosEntrados/ver")
	public String verOrdenDia(Model modelo) {

		List<AsuntosEntrados> ordenesDelDia = asuntosServi.listarNota();
		modelo.addAttribute("ordenesDelDia", ordenesDelDia);

		return "verOrdenDia";
	}

	@PreAuthorize("hasAnyRole('CONCEJAL_COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA', 'PRESIDENTE', 'SEC_PARLAMENTARIO' , 'CONCEJAL_COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL')")
	@GetMapping("/notas/asuntosEntrados/{id}")
	public ResponseEntity<byte[]> viewNota(@PathVariable Long id) {
		AsuntosEntrados orden = asuntosServi.BuscarNota(id);
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
	@GetMapping("/expediente/asuntosEntrados/{id}")
	public String ordenExpedientes (@PathVariable Long id, Model modelo) {
		
		List<Expediente> expedientes = asuntosServi.BuscarNota(id).getExpedientes();
		modelo.addAttribute("expedientes", expedientes);
		
		return "expedienteOrdendia";
		
	}
}
