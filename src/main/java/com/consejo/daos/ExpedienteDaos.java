package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.exceptions.BadRequestException;
import com.consejo.exceptions.InternalServerErrorException;
import com.consejo.exceptions.ResourceNotFoundException;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoNota;
import com.consejo.repository.IExpedienteRepository;
import com.consejo.specification.ExpedienteSpecification;

@Service
public class ExpedienteDaos implements IExpedienteDaos {

	@Autowired
	private IExpedienteRepository expedienteRepo;
	private final Map<CircuitoExpediente, Consumer<Expediente>> acciones = new HashMap<>();

	public ExpedienteDaos() {
		inicializarAcciones();
	}

	private void inicializarAcciones() {
		acciones.put(CircuitoExpediente.OFICINA_PARLAMENTARIA, this::moverAOficinaParlamentaria);
		acciones.put(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL, this::moverAGobiernoYSocial);
		acciones.put(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
				this::moverAComisionAmbientalYEconomia);
		acciones.put(CircuitoExpediente.AMBAS_COMISIONES, this::moverAAmbasComisiones);
		acciones.put(CircuitoExpediente.ARCHIVO, this::moverAArchivo);
		acciones.put(CircuitoExpediente.PRESIDENCIA, this::moverAPresidencia);
		acciones.put(CircuitoExpediente.BLOQUE_A, this::moverABloque_A);
		acciones.put(CircuitoExpediente.BLOQUE_B, this::moverABloque_B);
		acciones.put(CircuitoExpediente.BLOQUE_C, this::moverABloque_C);
		acciones.put(CircuitoExpediente.TODOS_LOS_BLOQUES, this::moverATodosBloques);
		acciones.put(CircuitoExpediente.LEGISLACION, this::moverALegislacion);
		acciones.put(CircuitoExpediente.DESPACHOS_DE_COMISION, this::moverADespachoComision);
		acciones.put(CircuitoExpediente.NOTAS_DE_COMISION, this::moverANotasComision);
		acciones.put(CircuitoExpediente.REPUESTA_AL_CIUDADANO, this::moverARepuestaDestinatario);
		acciones.put(CircuitoExpediente.NOTA_AL_MUNICIPIO, this::moverANotaMunicipio);
		acciones.put(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO, this::moverARepuestaMunicipio);
		acciones.put(CircuitoExpediente.FIN, this::moverAFin);
	}

	@Override
	public List<Expediente> listarExpedientes() {
		try {

			return expedienteRepo.findAll();

		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al listar los Expedientes.");
		}

	}

	@Override
	public Expediente buscarExpediente(String id) {
		try {
			return expedienteRepo.findById(id)
					.orElseThrow(() -> new ResourceNotFoundException("Expediente con ID " + id + " no encontrado."));
		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al buscar el Expediente.");
		}

	}

	@Override
	public void eliminaExpediente(Expediente expediente) {

		// borrar expediente

	}

	@Override
	public void guardarExpediente(Expediente expediente) {

		try {
			expedienteRepo.save(expediente);

		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (IllegalArgumentException iae) {
			throw new BadRequestException("Datos inválidos: " + iae.getMessage());
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al modificar el expediente.");
		}

	}

	@Override
	public void agregarMovimiento(String id, Movimiento movimiento, CircuitoExpediente circuito){

		try {
			Expediente e = expedienteRepo.findById(id)
					.orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));

			movimiento.setExpediente(e);
			movimiento.setId(generateMovimientoId(e));
			e.getMovimientos().add(movimiento);

			if (circuito != e.getEstado()) {
				acciones.getOrDefault(circuito, exp -> {
					throw new IllegalArgumentException("Estado no reconocido");
				}).accept(e);
			} else {
				expedienteRepo.save(e);
			}

		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (IllegalArgumentException iae) {
			throw new BadRequestException("Datos inválidos: " + iae.getMessage());
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al modificar el expediente.");
		}

	}

	@Override
	public void modificarExpediente(Expediente e, String id) {

		try {

			Expediente exp = expedienteRepo.findById(id)
					.orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));
			if (exp == null || exp.getId() == null) {
				throw new BadRequestException("El expediente o su ID no pueden ser nulos.");
			}
			if (!expedienteRepo.existsById(id)) {
				throw new ResourceNotFoundException("No se puede modificar: el expediente no existe.");
			}
			exp.setCaratula(e.getCaratula());
			exp.setDetalle(e.getDetalle());
			exp.setTipo(e.getTipo());
			expedienteRepo.save(exp);

		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (IllegalArgumentException iae) {
			throw new BadRequestException("Datos inválidos: " + iae.getMessage());
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al modificar el expediente.");
		}

	}

	private String generateMovimientoId(Expediente expediente) {
		// Lógica para generar el ID del movimiento basado en el expediente y el índice
		// de la lista
		int numeroMovimiento = expediente.getMovimientos().size() - 1;
		return expediente.getId() + "/" + String.format("%02d", numeroMovimiento);
	}

	// Este método podría ser llamado cuando se crea un nuevo expediente
	public String createExpedienteId(Integer codigoAsunto, String tipoNota, LocalDate fecha, int nroOrdenDia) {
		String tipo = tipoNota; // Asumiendo que 'inicial' es un código corto para el tipo
		String fechaStr = fecha.format(DateTimeFormatter.BASIC_ISO_DATE);
		String nroOrdenStr = String.format("%02d", nroOrdenDia); // Para asegurarse de que tenga dos dígitos

		return String.format("%s-%s-%s-%s", codigoAsunto, tipo, fechaStr, nroOrdenStr);
	}

	@Override
	public void moverAOficinaParlamentaria(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.REPUESTA_DEL_MUNICIPIO)) {
			expediente.setEstado(CircuitoExpediente.OFICINA_PARLAMENTARIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAComisionAmbientalYEconomia(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAGobiernoYSocial(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAAmbasComisiones(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.AMBAS_COMISIONES);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAArchivo(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.ARCHIVO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAPresidencia(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.PRESIDENCIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_A(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_A);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_B(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_B);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_C(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_C);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverATodosBloques(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.TODOS_LOS_BLOQUES);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverADespachoComision(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)
				|| (expediente.getEstado() == CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA)
				|| (expediente.getEstado() == CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL)
				|| (expediente.getEstado() == CircuitoExpediente.AMBAS_COMISIONES)
				|| (expediente.getEstado() == CircuitoExpediente.ARCHIVO)
				|| (expediente.getEstado() == CircuitoExpediente.PRESIDENCIA)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_A)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_B)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_C)
				|| (expediente.getEstado() == CircuitoExpediente.TODOS_LOS_BLOQUES)) {

			expediente.setEstado(CircuitoExpediente.DESPACHOS_DE_COMISION);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverALegislacion(Expediente expediente) {

		if (expediente.getEstado() == CircuitoExpediente.DESPACHOS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.LEGISLACION);
			expediente.setFincircuito(true);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverANotasComision(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)
				|| (expediente.getEstado() == CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA)
				|| (expediente.getEstado() == CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL)
				|| (expediente.getEstado() == CircuitoExpediente.AMBAS_COMISIONES)
				|| (expediente.getEstado() == CircuitoExpediente.ARCHIVO)
				|| (expediente.getEstado() == CircuitoExpediente.PRESIDENCIA)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_A)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_B)
				|| (expediente.getEstado() == CircuitoExpediente.BLOQUE_C)
				|| (expediente.getEstado() == CircuitoExpediente.TODOS_LOS_BLOQUES)) {

			expediente.setEstado(CircuitoExpediente.NOTAS_DE_COMISION);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverARepuestaDestinatario(Expediente expediente) {

		if (expediente.getEstado() == CircuitoExpediente.NOTAS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.REPUESTA_AL_CIUDADANO);
			expediente.setFincircuito(true);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}
	}

	@Override
	public void moverAFin(Expediente expediente) {

		if ((expediente.getEstado() == CircuitoExpediente.REPUESTA_AL_CIUDADANO)
				|| (expediente.getEstado() == CircuitoExpediente.LEGISLACION)
				|| (expediente.getEstado() == CircuitoExpediente.REPUESTA_DEL_MUNICIPIO)
				|| (expediente.getEstado() == CircuitoExpediente.NOTA_AL_MUNICIPIO)) {
			expediente.setEstado(CircuitoExpediente.FIN);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverANotaMunicipio(Expediente expediente) {
		if (expediente.getEstado() == CircuitoExpediente.NOTAS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.NOTA_AL_MUNICIPIO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverARepuestaMunicipio(Expediente expediente) {
		if (expediente.getEstado() == CircuitoExpediente.NOTA_AL_MUNICIPIO) {
			expediente.setEstado(CircuitoExpediente.REPUESTA_DEL_MUNICIPIO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}
	}

	@Override
	public List<Expediente> listarExpedientes(CircuitoExpediente estado) {
		try {

			return expedienteRepo.findByEstado(estado);

		} catch (DataAccessException dae) {
			throw new InternalServerErrorException("Error al acceder a la base de datos.");
		} catch (Exception ex) {
			throw new InternalServerErrorException("Error inesperado al listar los Expedientes.");
		}
	}

	@Override
	public Optional<Expediente> buscarExpedienteOptional(String id) {
		try {
			return expedienteRepo.findById(id);
		} catch (DataAccessException dae) {
			// Manejar la excepción de acceso a la base de datos
			System.err.println("Error al acceder a la base de datos: " + dae.getMessage());
			return null; // O retornar null si prefieres, pero es mejor devolver una lista vacía.
		} catch (Exception ex) {
			// Manejar cualquier otra excepción
			System.err.println("Ocurrió un error: " + ex.getMessage());
			return null;
		}
	}

	@Override
	public List<Expediente> buscarExpediente(LocalDate fecha, String detalle, String id, String caratula,
			String nombrePersona) {

		try {
			Specification<Expediente> spec = ExpedienteSpecification.buscarExpedientes(fecha, detalle, id, caratula,
					nombrePersona);

			List<Expediente> resultados = expedienteRepo.findAll(spec);

			return resultados;

		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Error en los parámetros de búsqueda: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Error inesperado al buscar expedientes", e);
		}
	}

	@Override
	public List<Expediente> buscarPorRangoDeFechasYCircuitos(LocalDate startDate) {
		
		List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.INGRESO,
				CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
				CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
				CircuitoExpediente.AMBAS_COMISIONES, CircuitoExpediente.ARCHIVO,
				CircuitoExpediente.DESPACHOS_DE_COMISION, CircuitoExpediente.LEGISLACION);

		try {

			return expedienteRepo.findByUltimoMovimientoFechaBetweenAndCircuitoIn(startDate, LocalDate.now(), circuitos);

		} catch (IllegalArgumentException e) {
			throw new RuntimeException("Error en los parámetros de búsqueda: " + e.getMessage());
		} catch (Exception e) {
			throw new RuntimeException("Error inesperado al buscar expedientes", e);
		}
	}

}
