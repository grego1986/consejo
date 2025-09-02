package com.consejo.restController.assembler;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Nota;
import com.consejo.pojos.Persona;
import com.consejo.restController.AsuntosEntradosRestController;
import com.consejo.restController.ExpedienteRestController;
import com.consejo.restController.PersonaRestController;
import com.consejo.restController.dto.ExpedienteDto;
import com.consejo.restController.dto.MovimientoDto;
import com.consejo.restController.dto.NotaDto;
import com.consejo.restController.dto.PersonaReducidoDto;

@Component
public class ExpedienteModelAssemblerExport implements RepresentationModelAssembler <Expediente, EntityModel<ExpedienteDto>> {

	@Autowired
	PersonaModelAssembler personaModelAssembler;
	
	@Override
	public EntityModel<ExpedienteDto> toModel(Expediente entity) {
		
		ExpedienteDto expedienteDto = convertirPojoADto (entity);
		
		expedienteDto.setPersona(personaModel (entity));
		expedienteDto.setCircuitos_Disponibles(entity.getEstado().getEstadosValidos());
		expedienteDto.setMovimientos(listaMovimientoDto(entity.getMovimientos()));
		
		 return EntityModel.of(expedienteDto,
		            linkTo(methodOn(ExpedienteRestController.class).obtener(expedienteDto.getId())).withRel("obtener"),
		            linkTo(methodOn(ExpedienteRestController.class).agregar(entity)).withRel("agregar"),
		            linkTo(methodOn(ExpedienteRestController.class).listar()).withRel("listar"),
		            linkTo(methodOn(ExpedienteRestController.class).eliminar(expedienteDto.getId())).withRel("eliminar"),
		            linkTo(methodOn(ExpedienteRestController.class).modificar(expedienteDto.getId(), entity)).withRel("modificar"),
		            linkTo(methodOn(ExpedienteRestController.class).listarIngreso()).withRel("expedientes con estado: Ingreso"),
		            linkTo(methodOn(ExpedienteRestController.class).listarOfParlamentario()).withRel("expedientes con estado: Oficina Parlamentaria"),
		            linkTo(methodOn(ExpedienteRestController.class).listarComisionUrbanoYAmbiental()).withRel("expedientes con estado: Comisión de Desarrollo Urbano, Ambiental y Economia"),
		            linkTo(methodOn(ExpedienteRestController.class).listarComisionGobYSocial()).withRel("expedientes con estado: Comisión de Gobierno y Desarrollo Social"),
		            linkTo(methodOn(ExpedienteRestController.class).listarAmbasComisiones()).withRel("expedientes con estado: Ambas Comisiones"),
		            linkTo(methodOn(ExpedienteRestController.class).listarArchivo()).withRel("expedientes con estado: Archivo"),
		            linkTo(methodOn(ExpedienteRestController.class).listarPresidencia()).withRel("expedientes con estado: Presidencia"),
		            linkTo(methodOn(ExpedienteRestController.class).listarBloqueA()).withRel("expedientes con estado: Bloque A"),
		            linkTo(methodOn(ExpedienteRestController.class).listarBloqueB()).withRel("expedientes con estado: Bloque B"),
		            linkTo(methodOn(ExpedienteRestController.class).listarBloqueC()).withRel("expedientes con estado: Bloque C"),
		            linkTo(methodOn(ExpedienteRestController.class).listarTodosLosBloques()).withRel("expedientes con estado: Todos los Bloques"),
		            linkTo(methodOn(ExpedienteRestController.class).listarDespachoComision()).withRel("expedientes con estado: Despacho de Comisión"),
		            linkTo(methodOn(ExpedienteRestController.class).listarLegislacion()).withRel("expedientes con estado: Legislación"),
		            linkTo(methodOn(ExpedienteRestController.class).listarNotasComision()).withRel("expedientes con estado: Notas de Comisión"),
		            linkTo(methodOn(ExpedienteRestController.class).listarRepuestaACiudadano()).withRel("expedientes con estado: Repuesta al Ciudadano"),
		            linkTo(methodOn(ExpedienteRestController.class).listarNotaAlMunicipio()).withRel("expedientes con estado: Nota al Municipio"),
		            linkTo(methodOn(ExpedienteRestController.class).listarRepuestaDelMunicipio()).withRel("expedientes con estado: Repuesta del Municipio"),
		            linkTo(methodOn(ExpedienteRestController.class).listarFin()).withRel("expedientes con estado: Fin"),
		            linkTo(methodOn(ExpedienteRestController.class).buscar(expedienteDto.getFecha(), expedienteDto.getCaratula(),expedienteDto.getDetalle(), expedienteDto.getId(), entity.getPersona().getNombre())).withRel("buscador de expedientes"),
		            linkTo(methodOn(ExpedienteRestController.class).buscarPorRangoFecha(expedienteDto.getFecha())).withRel("Buscar expediente desde un fecha hasta hoy"),
		            linkTo(methodOn(ExpedienteRestController.class).agregarMovimiento(expedienteDto.getId(), null)).withRel("Agregar movimiento al expediente")
		            
		        );
	}


	private ExpedienteDto convertirPojoADto (Expediente expediente) {
		
		ExpedienteDto expedienteDto = new ExpedienteDto ();
		
		expedienteDto.setId(expediente.getId());
		expedienteDto.setCaratula(expediente.getCaratula());
		expedienteDto.setDetalle(expediente.getDetalle());
		expedienteDto.setFecha(expediente.getFecha());
		expedienteDto.setEstado(expediente.getEstado());
		expedienteDto.setFincircuito(expediente.isFincircuito());
		//expedienteDto.setTipo(expediente.getTipo());
		//expedienteDto.setUsuariosAsignados(expediente.getUsuariosAsignados());
		//expedienteDto.setOrdenDia(expediente.getOrdenDia());
		//expedienteDto.setAsuntos(expediente.getAsuntos());
		
		return expedienteDto;
	}
	
	private PersonaReducidoDto convertirPersona (Persona p) {
		
		PersonaReducidoDto dto = new PersonaReducidoDto();
		dto.setDni_Cuit(p.getDni_Cuit());
		dto.setNombre(p.getNombre());
		
		return dto;
	}
	
	private MovimientoDto convertirMovimiento (Movimiento m) {
		
		MovimientoDto dto = new MovimientoDto ();
		 dto.setId(m.getId());
		 dto.setFecha(m.getFecha());
		 dto.setDetalle(m.getDetalle());
		 if (m.getNota()!= null) {
			 dto.setNota(convertirNota(m.getNota()));
		 }
		 
		 return dto;
	}
	
	private EntityModel<PersonaReducidoDto> personaModel (Expediente e) {
		
		if (e.getPersona()!= null) {
			
			PersonaReducidoDto dto = convertirPersona(e.getPersona());
			EntityModel<PersonaReducidoDto> personaModel = EntityModel.of(
	                dto,
	                linkTo(methodOn(PersonaRestController.class)
	                       .obtener(dto.getDni_Cuit()))
	                    .withRel("Ver Persona")
	            );

			
			return personaModel;
		} else {
			return null;
		}
	}
	
	private List<EntityModel<MovimientoDto>> listaMovimientoDto (List<Movimiento> movimientos){
		
		List<EntityModel<MovimientoDto>> movimientoReducidos =
			    movimientos.stream()
			        .map(m -> (Movimiento) m) // ⬅️ cast explícito
			        .map(mov -> {
			            MovimientoDto reducido = convertirMovimiento(mov);
			            return EntityModel.of(
			                reducido
			            );
			        })
			        .collect(Collectors.toList());
		return movimientoReducidos;
	}
	
	private NotaDto convertirNota (Nota n) {
		
		NotaDto dto = new NotaDto();
		
		dto.setId(n.getId());
		dto.setTitulo(n.getTitulo());
		dto.setVerNota(linkTo(methodOn(ExpedienteRestController.class)
	                    .verNota(n.getId())).toUri().toString());
		dto.setDescargarNota(linkTo(methodOn(ExpedienteRestController.class)
	                    .descargarNota(n.getId())).toUri().toString());
		return dto;
	}
	
	
}
