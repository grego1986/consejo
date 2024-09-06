package com.consejo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.consejo.daos.PasswordDaos;
import com.consejo.daos.RolDaos;
import com.consejo.daos.UsuarioDaos;
import com.consejo.form.FrmRegistroUsuario;
import com.consejo.pojos.Password;
import com.consejo.pojos.Rol;
import com.consejo.pojos.Usuario;

@Controller
public class RegistroUsuarioController {
	@Autowired
	private RolDaos rolServi;
	@Autowired
	private UsuarioDaos usuarioServi;
	@Autowired
	private PasswordDaos passServi;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// El formulario se inicializa aquí
	private FrmRegistroUsuario form = new FrmRegistroUsuario();

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/registroUsuario")
	public String abrirIndex(Model modelo) {
		modelo.addAttribute("formBean", form);
		modelo.addAttribute("dni", form);
		modelo.addAttribute("nombre", form);
		modelo.addAttribute("apellido", form);
		modelo.addAttribute("mail", form);
		modelo.addAttribute("rol", form);
		modelo.addAttribute("pass", form);
		modelo.addAttribute("esActivo", form);

		List<Rol> roles = rolServi.listarRoles();
		modelo.addAttribute("rolUsuario", roles);

		return "registroUsuario";
	}

	@PostMapping("/administrador/registroUsuario")
	public String registrarUsuario(@ModelAttribute("formBean") FrmRegistroUsuario registroUsuario, Model modelo,
			@RequestParam String action) {
		// Lógica para registrar al usuario
		Usuario usuario = new Usuario();
		Password pass = new Password();
		Rol rol = new Rol();
		

		if (action.equals("guardarUsuario")) {
			if (usuarioServi.existe(registroUsuario.getDni()) == false) {
				rol = rolServi.buscarRol(registroUsuario.getRol());
				pass.setPass(passwordEncoder.encode(registroUsuario.getPass()));
				pass.setUser(usuario);
				usuario.setDni(registroUsuario.getDni());
				usuario.setNombre(registroUsuario.getNombre());
				usuario.setApellido(registroUsuario.getApellido());
				usuario.setMail(registroUsuario.getMail());
				usuario.setContra(pass);
				usuario.setRol(rol);

				boolean exito = usuarioServi.agregarUsuario(usuario);
				passServi.ingresarPass(usuario, pass.getPass());
				
				if (exito) {
		            modelo.addAttribute("mensaje", "Registro exitoso.");
		            modelo.addAttribute("tipo", "exito");
		            
		            
		        } else {
		            modelo.addAttribute("mensaje", "El registro falló. Intente nuevamente.");
		            modelo.addAttribute("tipo", "error");
		           
		        }

				
			} else {
				modelo.addAttribute("mensaje", "El usuario ya existe");
	            modelo.addAttribute("tipo", "error");
	            
			}

		}
		
		return "redirect:/administrador/registroUsuario";
	}

}
