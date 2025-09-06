package com.consejo.restController.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.consejo.pojos.Expediente;
import com.consejo.pojos.Persona;
import com.consejo.restController.ExpedienteRestController;
import com.consejo.restController.PersonaRestController;
import com.consejo.restController.dto.ExpedienteExportDto;
import com.consejo.restController.dto.PersonaDto;

@Component
public class PersonaModelAssembler implements RepresentationModelAssembler <Persona, EntityModel<PersonaDto>> {

	@Override
	public EntityModel<PersonaDto> toModel(Persona entity) {

        PersonaDto personaDto = convertirADto(entity);
		
		return EntityModel.of(personaDto,
	            linkTo(methodOn(PersonaRestController.class).obtener(personaDto.getDni_Cuit())).withRel("obtener"),
	            linkTo(methodOn(PersonaRestController.class).listar(personaDto.getNombre())).withRel("Listar"),
	            linkTo(methodOn(PersonaRestController.class).agregar(null)).withRel("agregar"),
	            linkTo(methodOn(PersonaRestController.class).modificar(personaDto.getDni_Cuit(), null)).withRel("actualizar"),
	            linkTo(methodOn(PersonaRestController.class).eliminar(personaDto.getDni_Cuit())).withRel("eliminar")
	            );
	}
	
	public PersonaDto convertirADto (Persona p) {
		
		PersonaDto dto = new PersonaDto();
		
		dto.setDni_Cuit(p.getDni_Cuit());
		dto.setNombre(p.getNombre());
		dto.setDireccion(p.getDireccion());
		dto.setMail(p.getMail());
		dto.setTelefono(p.getTelefono());
		
		return dto;
	}

	public ExpedienteExportDto expedienteReducido(Expediente e) {
		
		ExpedienteExportDto ex = new ExpedienteExportDto();
		ex.setId(e.getId());
		ex.setCaratula(e.getCaratula());
		
		return ex;
	}
	
	
}
