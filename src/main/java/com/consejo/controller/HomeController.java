package com.consejo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.consejo.daos.AsuntosEntradosDaos;
import com.consejo.daos.OrdenDiaDaos;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.OrdenDia;

@Controller
public class HomeController {

	@Autowired
	private OrdenDiaDaos ordenServi;
	@Autowired
	private AsuntosEntradosDaos asuntosServi;

	@GetMapping("/home")
	public String index(Model modelo) {

		List<OrdenDia> ordenesDelDia = ordenServi.listarTratadoFalse();
		List<AsuntosEntrados> asuntos = asuntosServi.listarTatadosFalse();

		modelo.addAttribute("ordenesDelDia", ordenesDelDia);
		modelo.addAttribute("asuntosEntrados", asuntos);
		
		return "home"; // Retorna la vista `index.html`
	}

}
