package com.consejo.restController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.consejo.daos.PersonaDaos;
import com.consejo.exceptions.ResourceNotFoundException;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Persona;
import com.consejo.restController.assembler.PersonaModelAssembler;
import com.consejo.restController.dto.PersonaDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/persona")
@Tag(name = "Persona", description = "Operaciones relacionadas con las Personas")
public class PersonaRestController {
	
	@Autowired
	private PersonaDaos personaServi;
	@Autowired
    private PersonaModelAssembler assembler;

	@GetMapping("/{id}")
	@Operation(summary = "buscar una persona por DNI o CUIT", description = "busca y obtiene una persona por DNI o CUIT")
	public ResponseEntity<EntityModel<PersonaDto>> obtener(@PathVariable("id")Long dni_Cuit) {
		 
		Persona persona = personaServi.buscarPersona(dni_Cuit);
		if (persona == null) {
			throw new ResourceNotFoundException("persona con DNI o Cuit " + dni_Cuit + "no encontrado");
		}

		return ResponseEntity.ok(assembler.toModel(persona));
	}

	@GetMapping("/")
	@Operation(summary = "Listar personas", description = "Trae una lista de personas por nombre")
	public ResponseEntity<CollectionModel<EntityModel<PersonaDto>>> listar(@PathVariable(required = false)String nombre) {
		
		List<EntityModel<PersonaDto>> personas = personaServi.listarPersonas(nombre).stream()
	            .map(assembler::toModel)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(
	            CollectionModel.of(personas,
	                    linkTo(methodOn(PersonaRestController.class).listar(nombre)).withSelfRel())
	    );
	}

	@PostMapping("/registrar")
	@Operation(summary = "Registra una persona.", description = "Registra una persona, juridica o fisica.")
	public ResponseEntity<EntityModel<PersonaDto>> agregar(@RequestParam Persona p) {
		
		personaServi.guardarPersona(p);
		
		return  ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(p));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modifica una persona.", description = "Modifica los datos de una persona, juridica o fisica.")
	public ResponseEntity<EntityModel<PersonaDto>> modificar(@PathVariable ("id") Long dni_Cuit, @RequestParam Persona p) {


		Persona persona = personaServi.buscarPersona(dni_Cuit);
		if (persona!=null) {
			p.setDni_Cuit(dni_Cuit);
			personaServi.modificarPersona(p);
		}
		
		persona = personaServi.buscarPersona(dni_Cuit);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(persona));
	}

	@DeleteMapping("/{id}")
	@Operation(summary="Elimina una persona", description="Elimina una persona de la bases de datos")
	public ResponseEntity<EntityModel<PersonaDto>> eliminar(@PathVariable ("id")Long dni_Cuit) {

		Persona p = personaServi.buscarPersona(dni_Cuit);
		personaServi.eliminaPersona(p);
		return ResponseEntity.noContent().build();
	}

	
}
