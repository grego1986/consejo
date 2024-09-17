package com.consejo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.consejo.daos.PasswordDaos;
import com.consejo.daos.RolDaos;
import com.consejo.daos.UsuarioDaos;
import com.consejo.form.BuscarUsuarioForm;
import com.consejo.form.FrmRegistroUsuario;
import com.consejo.form.UsuarioForm;
import com.consejo.pojos.Password;
import com.consejo.pojos.Rol;
import com.consejo.pojos.Usuario;

@Controller
public class UsuarioController {

	@Autowired
	private UsuarioDaos usuarioServi;
	private BuscarUsuarioForm buscarUser = new BuscarUsuarioForm();
	private List<Usuario> usuarios = new ArrayList<>();
	private UsuarioForm usuarioModificar = new UsuarioForm();
	@Autowired
	private RolDaos rolServi;
	@Autowired
	private PasswordDaos passServi;
	@Autowired
	private PasswordEncoder passwordEncoder;

	// El formulario se inicializa aquí
	private FrmRegistroUsuario form = new FrmRegistroUsuario();
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/registroUsuario")
	public String registroUsuario(Model modelo) {
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/administrador/buscarUsuario")
    public String busacarUsuario(Model modelo) {
        modelo.addAttribute("formBuscarCiudadano", buscarUser);
    	  
        return "buscarUsuario";
    }
	
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/buscarUsuario/resultado")
	public String buscarUsuarios(Model modelo, @RequestParam String action, @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "apellido", required = false) String apellido) {
		
		if (action.equals("buscar")) {
	    usuarios = usuarioServi.buscarUsuarios(nombre , apellido);
	    
	    modelo.addAttribute("usuarios", usuarios);
	    modelo.addAttribute("formBuscarCiudadano", buscarUser);
		}
	    return "buscarUsuario";  // Volvemos a la misma vista con los resultados en la tabla
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/administrador/modificarUsuario/{id}")
	public String modificarUsuario (@PathVariable Long id, Model modelo) {
		Usuario usuario = usuarioServi.buscarUsuario(id);
		
		List<Rol> roles = rolServi.listarRoles();
		usuarioModificar.setDni(usuario.getDni());
		usuarioModificar.setApellido(usuario.getApellido());
		usuarioModificar.setNombre(usuario.getNombre());
		usuarioModificar.setMail(usuario.getMail());
		
		
		modelo.addAttribute("usuarioform", usuarioModificar);
		modelo.addAttribute("roles", roles);
		return "modificarUsuario";
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/administrador/modificarUsuario/{id}")
	public String modificarUsuariopost (@ModelAttribute("usuarioform") UsuarioForm usuarioForm, Model modelo) {
		
		Usuario usuario = usuarioServi.buscarUsuario(usuarioForm.getDni());
		Rol rol = rolServi.buscarRol(usuarioForm.getRol());
		
		usuario.setMail(usuarioForm.getMail());
		usuario.setRol(rol);
		
		if (usuarioServi.modificarUsuario(usuario)) {
			return "home";
		}
		return "redirect:/administrador/error";
	}
}
