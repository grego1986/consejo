package com.consejo.restController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.core.LinkBuilderSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.consejo.daos.ExpedienteDaos;
import com.consejo.daos.MovimientoDaos;
import com.consejo.daos.NotaDaos;
import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.exceptions.ArchivoNoProcesadoException;
import com.consejo.exceptions.ResourceNotFoundException;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Nota;
import com.consejo.restController.assembler.ExpedienteModelAssemblerExport;
import com.consejo.restController.dto.ExpedienteDto;
import com.consejo.restController.dto.ExpedienteExportDto;
import com.consejo.restController.dto.MovimientoDto;
import com.consejo.restController.dto.NotaDto;
import com.consejo.restController.request.MovimientoRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/expediente")
@Tag(name = "Expediente", description = "Operaciones relacionadas con los expedientes")
public class ExpedienteRestController {

	@Autowired
	private ExpedienteDaos expedienteServi;
	@Autowired
	private ExpedienteModelAssemblerExport assembler;
	@Autowired
	private NotaDaos notaServi;
	

	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	@Operation(summary = "Listar todos los Expedientes", description = "Devuelve todos los Expedientes registrados")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listar() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes().stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listar()).withSelfRel()));
	}

	@GetMapping("/{id}")
	@Operation(summary = "buscar un expediente por id", description = "busca y obtiene un expediente ppor id")
	public ResponseEntity<EntityModel<ExpedienteDto>> obtener(@PathVariable("id") String id) {

		Expediente expediente = expedienteServi.buscarExpediente(id);
		if (expediente == null) {
			throw new ResourceNotFoundException("Expediente con ID " + id + "no encontrado");
		}

		return ResponseEntity.ok(assembler.toModel(expediente));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Eliminar un expediente", description = "Elimina un Expediente de la bases de datos.")
	public ResponseEntity<Void> eliminar(@PathVariable("id") String id) {

		Expediente expediente = expedienteServi.buscarExpediente(id);
		if (expediente == null) {
			throw new ResourceNotFoundException("no se encontro el expediente con el ID " + id);
		}
		expedienteServi.eliminaExpediente(expediente);

		return ResponseEntity.noContent().build();
	}

	@PutMapping("/{id}")
	@Operation(summary = "Modifica un expediente", description = "Modifica algun dato del expediente.")
	public ResponseEntity<EntityModel<ExpedienteDto>> modificar(@PathVariable("id") String id, @RequestBody Expediente expediente) {

		expediente.setId(id);
		expedienteServi.modificarExpediente(expediente, id);

		return ResponseEntity.ok(assembler.toModel(expediente));
	}

	@PostMapping("/nuevoExpediente")
	@Operation(summary = "Crea un nuevo expediente", description = "crea y guarda un nuevo expediente.")
	public ResponseEntity<EntityModel<ExpedienteDto>> agregar(@RequestBody Expediente e) {

		expedienteServi.guardarExpediente(e);

		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(e));
	}

	@GetMapping("/ingreso")
	@Operation(summary = "Listar solo ingresos", description = "Lista los expediente con Estado: INGRESO")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarIngreso() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.INGRESO).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarIngreso()).withSelfRel()));

	}

	@GetMapping("/oficinaParlamentaria")
	@Operation(summary = "Listar solo oficina Parlamentaria", description = "Lista los expediente con Estado: OFICINA_PARLAMENTARIA")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarOfParlamentario() {
		
		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.OFICINA_PARLAMENTARIA).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarOfParlamentario()).withSelfRel()));
	}

	@GetMapping("/comisionDesarrolloUrbanoAmbientalYEconomia")
	@Operation(summary = "Listar solo Comision de desarrollo Urbano, Ambiental y Economia", description = "Lista los expediente con Estado: COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarComisionUrbanoYAmbiental() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarComisionUrbanoYAmbiental()).withSelfRel()));
	}

	@GetMapping("/comisionGobiernoYDesarrolloSocial")
	@Operation(summary = "Listar solo comision de gobierno y desarrollo social", description = "Lista los expediente con Estado: COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarComisionGobYSocial() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarComisionGobYSocial()).withSelfRel()));
	}

	@GetMapping("/ambasComisiones")
	@Operation(summary = "Listar solo Ambas comisiones", description = "Lista los expediente con Estado: AMBAS_COMISIONES")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarAmbasComisiones() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.AMBAS_COMISIONES).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarAmbasComisiones()).withSelfRel()));
	}

	@GetMapping("/archivo")
	@Operation(summary="Listar solo Archivados", description="Lista de los expedientes con estado: ARCHIVO")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarArchivo() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.ARCHIVO).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarArchivo()).withSelfRel()));
	}

	@GetMapping("/presidencia")
	@Operation(summary="Listar solo Presidencia", description="Lista de los expedientes con estado: PRESIDENCIA")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarPresidencia() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.PRESIDENCIA).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarPresidencia()).withSelfRel()));
	}

	@GetMapping("/bloqueA")
	@Operation(summary="Listar solo Bloque A", description="Lista de los expedientes con estado: BLOQUE_A")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarBloqueA() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.BLOQUE_A).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarBloqueA()).withSelfRel()));
	}

	@GetMapping("/bloqueB")
	@Operation(summary="Listar solo Bloque B", description="Lista de los expedientes con estado: BLOQUE_B")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarBloqueB() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.BLOQUE_B).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarBloqueB()).withSelfRel()));
	}

	@GetMapping("/bloqueC")
	@Operation(summary="Listar solo Bloque C", description="Lista de los expedientes con estado: BLOQUE_C")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarBloqueC() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.BLOQUE_C).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarBloqueC()).withSelfRel()));
	}

	@GetMapping("/todosLosBloques")
	@Operation(summary="Listar solo Todos los Bloques", description="Lista de los expedientes con estado: TODOS_LOS_BLOQUES")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarTodosLosBloques() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.TODOS_LOS_BLOQUES).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarTodosLosBloques()).withSelfRel()));
	}

	@GetMapping("/despachoDeComision")
	@Operation(summary="Listar solo Despacho de Comisión", description="Lista de los expedientes con estado: DESPACHOS_DE_COMISION")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarDespachoComision() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.DESPACHOS_DE_COMISION).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarDespachoComision()).withSelfRel()));
	}

	@GetMapping("/legislacion")
	@Operation(summary="Listar solo Legislación", description="Lista de los expedientes con estado: LEGISLACION")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarLegislacion() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.LEGISLACION).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarLegislacion()).withSelfRel()));
	}

	@GetMapping("/notasDeComision")
	@Operation(summary="Listar solo Notas de Comisión", description="Lista de los expedientes con estado: NOTAS_DE_COMISION")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarNotasComision() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.NOTAS_DE_COMISION).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarNotasComision()).withSelfRel()));
	}

	@GetMapping("/repuestaACiudadano")
	@Operation(summary="Listar solo Repuesta a Ciudadano", description="Lista de los expedientes con estado: REPUESTA_AL_CIUDADANO")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarRepuestaACiudadano() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.REPUESTA_AL_CIUDADANO).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarRepuestaACiudadano()).withSelfRel()));
	}

	@GetMapping("/notaAlMunicipio")
	@Operation(summary="Listar solo Notas Al Municipio", description="Lista de los expedientes con estado: NOTA_AL_MUNICIPIO")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarNotaAlMunicipio() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.NOTA_AL_MUNICIPIO).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarNotaAlMunicipio()).withSelfRel()));
	}

	@GetMapping("/repuestaDelMunicipio")
	@Operation(summary="Listar solo Repuesta del Municipio", description="Lista de los expedientes con estado: REPUESTA_DEL_MUNICIPIO")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarRepuestaDelMunicipio() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarRepuestaDelMunicipio()).withSelfRel()));
	}

	@GetMapping("/finalizados")
	@Operation(summary="Listar solo Finalizados", description="Lista de los expedientes con estado: FIN")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> listarFin() {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.listarExpedientes(CircuitoExpediente.FIN).stream()
				.map(assembler::toModel).collect(Collectors.toList());

		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).listarFin()).withSelfRel()));
	}

	@GetMapping("/buscar")
	@Operation(summary="Buscar expedientes", description="buscar expedientes detalle, id, caratula o nonbre de quien comenzo el tramite")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> buscar(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(required = false) String detalle,
            @RequestParam(required = false) String id,
            @RequestParam(required = false) String caratula,
            @RequestParam(required = false) String nombrePersona) {
		
		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.buscarExpediente(fecha, detalle, id, caratula, nombrePersona)
				.stream().map(assembler::toModel).collect(Collectors.toList());
		
		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).buscar(fecha, detalle, id, caratula, nombrePersona)).withSelfRel()));
	}

	@GetMapping("/buscarPorFecha")
	@Operation(summary="Buscar expedientes por fecha", description="trae expediente desde una fecha hasta hoy")
	public ResponseEntity<CollectionModel<EntityModel<ExpedienteDto>>> buscarPorRangoFecha(LocalDate fecha) {

		List<EntityModel<ExpedienteDto>> expedientes = expedienteServi.buscarPorRangoDeFechasYCircuitos(fecha)
				.stream().map(assembler::toModel).collect(Collectors.toList());
		
		return ResponseEntity.ok(CollectionModel.of(expedientes,
				linkTo(methodOn(ExpedienteRestController.class).buscarPorRangoFecha(fecha)).withSelfRel()));
	}
	
	@PostMapping(value="/{id}/agregarMovimiento", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@Operation(summary="Agregar movimiento", description="Agrega un movimiento al expediente")
	public ResponseEntity<EntityModel<ExpedienteDto>> agregarMovimiento(@PathVariable("id")String id,
			@RequestParam MovimientoRequest movimiento) {

		
		Movimiento m = new Movimiento ();
		m.setDetalle(movimiento.getDetalle());
		m.setExpediente(expedienteServi.buscarExpediente(id));
		m.setFecha(movimiento.getFecha());
		//mover al service
		try {
		if (movimiento.getNota()!= null) {
			Nota nota = new Nota();
			nota.setTitulo(movimiento.getNota().getTitulo());
			byte[] n = null;
			n = movimiento.getNota().getNota().getBytes();
			nota.setNota(n);
		}
		}catch(IOException e) {
			throw new ArchivoNoProcesadoException("No se pudo procesar el archivo adjunto", e);
	    }
			
		CircuitoExpediente c = movimiento.getEstado();
		
		expedienteServi.agregarMovimiento(id, m, c);
		
		Expediente exp = expedienteServi.buscarExpediente(id);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(assembler.toModel(exp));
	}

	@GetMapping("/movimiento/ver-Nota/{id}")
	public ResponseEntity<byte[]> verNota(@PathVariable("id")Long id) {
		Nota n = notaServi.BuscarNota(id);
	    if (n == null || n.getNota() == null) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok()
	            .contentType(MediaType.APPLICATION_PDF) // o el tipo real si no es PDF
	            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"nota_" + id + ".pdf\"")
	            .body(toPrimitives(n.getNota()));
	}


	@GetMapping("/movimiento/descargar-Nota/{id}")
	public ResponseEntity<byte[]> descargarNota(@PathVariable("id")Long id) {
		
		Nota n = notaServi.BuscarNota(id);
	    if (n == null || n.getNota() == null) {
	        return ResponseEntity.notFound().build();
	    }
		
		return ResponseEntity.ok()
	            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=nota_" + id + ".pdf")
	            .contentType(MediaType.APPLICATION_PDF)
	            .body(n.getNota());
	}
	
	private byte[] toPrimitives(byte[] bs) {
	    byte[] result = new byte[bs.length];
	    for (int i = 0; i < bs.length; i++) {
	        result[i] = bs[i];
	    }
	    return result;
	}

	
}
