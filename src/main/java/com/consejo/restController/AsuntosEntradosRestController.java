package com.consejo.restController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.consejo.restController.assembler.AsuntosEntradosModelAssembler;
import com.consejo.restController.dto.AsuntosEntradosDto;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import com.consejo.daos.AsuntosEntradosDaos;
import com.consejo.exceptions.ResourceNotFoundException;
import com.consejo.pojos.AsuntosEntrados;
import java.io.IOException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/asuntos-entrados")
@Tag(name = "Asuntos Entrados", description = "Operaciones relacionadas con los asuntos entrados")
public class AsuntosEntradosRestController {

	@Autowired
	private AsuntosEntradosDaos asuntosServi;
	@Autowired
    private AsuntosEntradosModelAssembler assembler;
	
	@GetMapping( produces = { MediaType.APPLICATION_JSON_VALUE})
	@Operation(summary = "Listar todos los asuntos entrados", description = "Devuelve todos los asuntos entrados registrados")
	public ResponseEntity<CollectionModel<EntityModel<AsuntosEntradosDto>>> listar() {
		
	    List<EntityModel<AsuntosEntradosDto>> asuntos = asuntosServi.listarNota().stream()
	            .map(assembler::toModel)
	            .collect(Collectors.toList());

	    return ResponseEntity.ok(
	            CollectionModel.of(asuntos,
	                    linkTo(methodOn(AsuntosEntradosRestController.class).listar()).withSelfRel())
	    );
	}
	

	@PostMapping("/nuevo")
	@Operation(summary="Crear un nuevo Asuntos Entrados", description ="Crea y persiste un nuevo asunto entrados")
	public ResponseEntity<EntityModel<AsuntosEntradosDto>> crear(@RequestBody AsuntosEntrados nuevo){
		
		asuntosServi.guardarNota(nuevo);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(nuevo));
	}

	@GetMapping("/{id}")
	@Operation(summary="Obtiene un asunto entrado", description="Obtiene de la bases de datos un asunto entrado")
	public ResponseEntity<EntityModel<AsuntosEntradosDto>> obtener(@PathVariable("id") Long id) {
        
		AsuntosEntrados asunto = asuntosServi.BuscarNota(id);
		if (asunto == null) {
			throw new ResourceNotFoundException("Asunto con ID " + id + " no encontrado");
		}
		
		return ResponseEntity.ok(assembler.toModel(asunto));
	}

	@GetMapping("/no-tratados")
	@Operation(summary = "Listar todos los asuntos entrados NO tratados", description = "Devuelve todos los asuntos entrados NO tratados")
	public ResponseEntity<CollectionModel<EntityModel<AsuntosEntradosDto>>> noTratados() {
		
		List<EntityModel<AsuntosEntradosDto>> asuntos = asuntosServi.listarTatadosFalse().stream().map(assembler::toModel).collect(Collectors.toList());
		
		 return ResponseEntity.ok(
		            CollectionModel.of(asuntos,
		                    linkTo(methodOn(AsuntosEntradosRestController.class).noTratados()).withSelfRel())
		    );
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Elimina Asunto entrado", description = "Elimina Asunto Entrado")
	public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
		
		AsuntosEntrados asunto = asuntosServi.BuscarNota(id);
		if (asunto == null) {
			throw new ResourceNotFoundException("No se encontró el asunto con ID " + id);
		}
		asuntosServi.eliminaNota(asunto);
		
		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	@Operation(summary = "Actualiza Asuntos Entrados", description = "Actualiza el asunto entrado")
	public ResponseEntity<EntityModel<AsuntosEntradosDto>> actualizar(@PathVariable("id") Long id,@RequestBody AsuntosEntrados asunto) {

		    asunto.setId(id);
	        asuntosServi.modificarAsunto(asunto);

	        return ResponseEntity
	                .ok(assembler.toModel(asunto));
	}

	@GetMapping("/descargar-nota/{id}")
	public ResponseEntity<byte[]> descargarNota(@PathVariable("id") Long id) {
		 AsuntosEntrados asunto = asuntosServi.BuscarNota(id);
		    if (asunto == null || asunto.getNota() == null) {
		        return ResponseEntity.notFound().build();
		    }

		    return ResponseEntity.ok()
		            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nota_" + id + ".pdf")
		            .contentType(MediaType.APPLICATION_PDF)
		            .body(asunto.getNota());
	}

	@GetMapping("/ver-nota/{id}")
	public ResponseEntity<byte[]> verNota(@PathVariable Long id) {
	    AsuntosEntrados asunto = asuntosServi.BuscarNota(id);
	    if (asunto == null || asunto.getNota() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF) // o el tipo real si no es PDF
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"nota_" + id + ".pdf\"")
	            .body(toPrimitives(asunto.getNota())); // convertimos Byte[] → byte[]
	}


	 private byte[] toPrimitives(byte[] bs) {
	    byte[] result = new byte[bs.length];
	    for (int i = 0; i < bs.length; i++) {
	        result[i] = bs[i];
	    }
	    return result;
	}

	
}
