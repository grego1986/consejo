package com.consejo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import com.consejo.daos.UsuarioDaos;
import com.consejo.form.BuscarUsuarioForm;
import com.consejo.pojos.Usuario;

@Controller
public class BuscarUsuarioController {

	@Autowired
	private UsuarioDaos usuarioServi;
	private BuscarUsuarioForm buscarUser = new BuscarUsuarioForm();
	private List<Usuario> usuarios = new ArrayList<>();
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/administrador/buscarUsuario")
    public String abrirIndex(Model modelo) {
        modelo.addAttribute("formBean", buscarUser);
    	  
        return "buscarUsuario";
    }
	
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/buscarUsuario/resultado")
	public String buscarUsuarios(Model modelo, @RequestParam String action, @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido) {
		
		if (action.equals("buscar")) {
	    usuarios = usuarioServi.buscarUsuarios(nombre , apellido);
	    
	    modelo.addAttribute("usuarios", usuarios);
	    modelo.addAttribute("formBean", buscarUser);
		}
	    return "buscarUsuario";  // Volvemos a la misma vista con los resultados en la tabla
	}
}
