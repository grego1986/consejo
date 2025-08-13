package com.consejo.restController.assembler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.stream.Collectors;

import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Expediente;
import com.consejo.restController.AsuntosEntradosRestController;
import com.consejo.restController.ExpedienteRestController;
import com.consejo.restController.dto.AsuntosEntradosDto;
import com.consejo.restController.dto.ExpedienteExportDto;

@Component
public class AsuntosEntradosModelAssembler implements RepresentationModelAssembler <AsuntosEntrados, EntityModel<AsuntosEntradosDto>> {

	@Autowired
	ExpedienteModelAssemblerExport expedienteModelAssembler;
	
	@Override
	public EntityModel<AsuntosEntradosDto> toModel(AsuntosEntrados entity) {
		//aca pongo las rutas para los link de hateoas
		AsuntosEntradosDto dto = convertirPojoADto(entity);
		
		List<EntityModel<ExpedienteExportDto>> expedientesReducidos =
			    entity.getExpedientes().stream()
			        .map(e -> (Expediente) e) // ⬅️ cast explícito
			        .map(exp -> {
			            ExpedienteExportDto reducido = convertirAExpedienteReducido(exp);
			            return EntityModel.of(
			                reducido,
			                linkTo(methodOn(ExpedienteRestController.class)
			                       .obtener(exp.getId()))
			                    .withSelfRel()
			            );
			        })
			        .collect(Collectors.toList());

	        dto.setExpedientes(expedientesReducidos);
		
			return EntityModel.of(dto,  
					linkTo(methodOn(AsuntosEntradosRestController.class).obtener(dto.getId())).withSelfRel(),
			        linkTo(methodOn(AsuntosEntradosRestController.class).listar()).withRel("todos"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).noTratados()).withRel("noTratados"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).actualizar(dto.getId(), entity)).withRel("editar"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).eliminar(dto.getId())).withRel("eliminar"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).descargarNota(dto.getId())).withRel("descargar"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).verNota(dto.getId())).withRel("ver"),
			        linkTo(methodOn(AsuntosEntradosRestController.class).crear(null)).withRel("crear")
			);
		
	}

	private AsuntosEntradosDto convertirPojoADto (AsuntosEntrados asuntos) {
		
		AsuntosEntradosDto asuntosDto = new AsuntosEntradosDto ();
		
		asuntosDto.setId(asuntos.getId());
		asuntosDto.setFecha(asuntos.getFecha());
		asuntosDto.setTitulo(asuntos.getTitulo());
		asuntosDto.setTratado(asuntos.isTratado());
		 if (asuntos.getNota() != null) {
	            asuntosDto.setNota(linkTo(methodOn(AsuntosEntradosRestController.class)
	                    .descargarNota(asuntos.getId())).toUri().toString());
	        }
		
		return asuntosDto;
	}
	
	private ExpedienteExportDto convertirAExpedienteReducido(Expediente expediente) {
        ExpedienteExportDto dto = new ExpedienteExportDto();
        dto.setId(expediente.getId());
        dto.setCaratula(expediente.getCaratula());
        return dto;
    }
}
