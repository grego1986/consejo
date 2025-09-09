package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.consejo.enumeraciones.CircuitoExpediente;
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

	@Override
	public List<Expediente> listarExpedientes() {
		try {

			return expedienteRepo.findAll();

		} catch (DataAccessException dae) {
			// Manejar la excepción de acceso a la base de datos
			System.err.println("Error al acceder a la base de datos: " + dae.getMessage());
			return new ArrayList<>(); // O retornar null si prefieres, pero es mejor devolver una lista vacía.
		} catch (Exception ex) {
			// Manejar cualquier otra excepción
			System.err.println("Ocurrió un error: " + ex.getMessage());
			return new ArrayList<>();
		}

	}

	@Override
	public Expediente buscarExpediente(String id) {
		try {
			return expedienteRepo.findById(id).orElse(null);
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
	public void eliminaExpediente(Expediente expediente) {

		// borrar expediente

	}

	@Override
	public void guardarExpediente(Expediente expediente) throws IOException {

		try {
			expedienteRepo.save(expediente);

		} catch (DataAccessException dae) {
			// Manejo de errores específicos de la base de datos
			throw new IOException("Error al acceder a la base de datos: " + dae.getMessage(), dae);
		} catch (IllegalArgumentException iae) {
			// Manejo de argumentos inválidos pasados a la entidad o al repositorio
			throw new IOException("Argumento inválido: " + iae.getMessage(), iae);
		} catch (Exception ex) {
			// Manejo general de excepciones
			throw new IOException("Error al guardar el expediente: " + ex.getMessage(), ex);
		}

	}

	@Override
	public void agregarMovimiento(String id, Movimiento movimiento, CircuitoExpediente circuito) throws IOException {

		try {

			Expediente e = expedienteRepo.findById(id)
					.orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));
			movimiento.setExpediente(e);
			movimiento.setId(generateMovimientoId(e));
			e.getMovimientos().add(movimiento);
			switch (circuito) {

			case OFICINA_PARLAMENTARIA:
				if (circuito != e.getEstado()) {
					moverAOficinaParlamentaria(e);
				} else {
					expedienteRepo.save(e);
				}
				break;

			case COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL:
				if (circuito != e.getEstado()) {
					moverAGobiernoYSocial(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA:
				if (circuito != e.getEstado()) {
					moverAComisionAmbientalYEconomia(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case AMBAS_COMISIONES:
				if (circuito != e.getEstado()) {
					moverAAmbasComisiones(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case ARCHIVO:
				moverAArchivo(e);
				break;

			case PRESIDENCIA:
				if (circuito != e.getEstado()) {
					moverAPresidencia(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case BLOQUE_A:
				if (circuito != e.getEstado()) {
					moverABloque_A(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case BLOQUE_B:
				if (circuito != e.getEstado()) {
					moverABloque_B(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case BLOQUE_C:
				if (circuito != e.getEstado()) {
					moverABloque_B(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case TODOS_LOS_BLOQUES:
				if (circuito != e.getEstado()) {
					moverATodosBloques(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case LEGISLACION:
				if (circuito != e.getEstado()) {
					moverALegislacion(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case DESPACHOS_DE_COMISION:
				if (circuito != e.getEstado()) {
					moverADespachoComision(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case NOTAS_DE_COMISION:
				if (circuito != e.getEstado()) {
					moverANotasComision(e);
				} else {
					expedienteRepo.save(e);
				}

				break;

			case REPUESTA_AL_CIUDADANO:
				moverARepuestaDestinatario(e);
				break;

			case NOTA_AL_MUNICIPIO:
				moverANotaMunicipio(e);
				break;

			case REPUESTA_DEL_MUNICIPIO:
				moverARepuestaMunicipio(e);
				break;
			case FIN:
				moverAFin(e);
				break;

			default:
				System.out.println("Estado no reconocido.");
				break;

			}

		} catch (DataAccessException dae) {
			// Manejo de errores específicos de la base de datos
			throw new IOException("Error al acceder a la base de datos: " + dae.getMessage(), dae);
		} catch (IllegalArgumentException iae) {
			// Manejo de argumentos inválidos pasados a la entidad o al repositorio
			throw new IOException("Argumento inválido: " + iae.getMessage(), iae);
		} catch (Exception ex) {
			// Manejo general de excepciones
			throw new IOException("Error al guardar el expediente: " + ex.getMessage(), ex);
		}

	}

	@Override
	public void modificarExpediente(Expediente e, String id) throws IOException {

		try {

			Expediente exp = expedienteRepo.findById(id)
					.orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));
			exp.setCaratula(e.getCaratula());
			exp.setDetalle(e.getDetalle());
			exp.setTipo(e.getTipo());
			expedienteRepo.save(e);

		} catch (DataAccessException dae) {
			// Manejo de errores específicos de la base de datos
			throw new IOException("Error al acceder a la base de datos: " + dae.getMessage(), dae);
		} catch (IllegalArgumentException iae) {
			// Manejo de argumentos inválidos pasados a la entidad o al repositorio
			throw new IOException("Argumento inválido: " + iae.getMessage(), iae);
		} catch (Exception ex) {
			// Manejo general de excepciones
			throw new IOException("Error al guardar el expediente: " + ex.getMessage(), ex);
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
	public void moverAOficinaParlamentaria(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.REPUESTA_DEL_MUNICIPIO)) {
			expediente.setEstado(CircuitoExpediente.OFICINA_PARLAMENTARIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAComisionAmbientalYEconomia(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAGobiernoYSocial(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAAmbasComisiones(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.AMBAS_COMISIONES);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAArchivo(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.ARCHIVO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAPresidencia(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.PRESIDENCIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_A(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_A);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_B(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_B);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_C(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_C);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverATodosBloques(Expediente expediente) throws IOException {

		if ((expediente.getEstado() == CircuitoExpediente.INGRESO)
				|| (expediente.getEstado() == CircuitoExpediente.OFICINA_PARLAMENTARIA)) {
			expediente.setEstado(CircuitoExpediente.TODOS_LOS_BLOQUES);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverADespachoComision(Expediente expediente) throws IOException {

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
	public void moverALegislacion(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.DESPACHOS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.LEGISLACION);
			expediente.setFincircuito(true);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverANotasComision(Expediente expediente) throws IOException {

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
	public void moverARepuestaDestinatario(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.NOTAS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.REPUESTA_AL_CIUDADANO);
			expediente.setFincircuito(true);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}
	}

	@Override
	public void moverAFin(Expediente expediente) throws IOException {

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
	public void moverANotaMunicipio(Expediente expediente) throws IOException {
		if (expediente.getEstado() == CircuitoExpediente.NOTAS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.NOTA_AL_MUNICIPIO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverARepuestaMunicipio(Expediente expediente) throws IOException {
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
			// Manejar la excepción de acceso a la base de datos
			System.err.println("Error al acceder a la base de datos: " + dae.getMessage());
			return new ArrayList<>(); // O retornar null si prefieres, pero es mejor devolver una lista vacía.
		} catch (Exception ex) {
			// Manejar cualquier otra excepción
			System.err.println("Ocurrió un error: " + ex.getMessage());
			return new ArrayList<>();
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

		Specification<Expediente> spec = ExpedienteSpecification.buscarExpedientes(fecha, detalle, id, caratula,
				nombrePersona);

		return expedienteRepo.findAll(spec);
	}

	@Override
	public List<Expediente> buscarPorRangoDeFechasYCircuitos(LocalDate startDate, LocalDate endDate) {
		List<CircuitoExpediente> circuitos = Arrays.asList(CircuitoExpediente.INGRESO,
				CircuitoExpediente.COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL,
				CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA,
				CircuitoExpediente.AMBAS_COMISIONES, CircuitoExpediente.ARCHIVO,
				CircuitoExpediente.DESPACHOS_DE_COMISION, CircuitoExpediente.LEGISLACION);
		return expedienteRepo.findByUltimoMovimientoFechaBetweenAndCircuitoIn(startDate, endDate, circuitos);
	}

	@Override
	public List<Expediente> porVencer() {

		LocalDate hoy = LocalDate.now();
		LocalDate desde = hoy.minusMonths(12);
		LocalDate hasta = hoy.minusMonths(11);
		List<CircuitoExpediente> excluidos = List.of(CircuitoExpediente.ARCHIVO, CircuitoExpediente.LEGISLACION,
				CircuitoExpediente.FIN);

		return expedienteRepo.findByUltimoMovimientoFechaBetweenAndCircuitoIn(desde, hasta, excluidos);
	}

	@Override
	public void sinestadoParlamentario() {

		LocalDate haceUnAnio = LocalDate.now().minusYears(1);

		List<CircuitoExpediente> excluidos = List.of(CircuitoExpediente.ARCHIVO, CircuitoExpediente.LEGISLACION,
				CircuitoExpediente.FIN);

		List<Expediente> vencidos = expedienteRepo.findByFechaBeforeAndEstadoNotIn(haceUnAnio, excluidos);
		// ver
		Movimiento m = new Movimiento();
		m.setDetalle("SIN ESTADO PARLAMENTARIO.");
		m.setFecha(LocalDate.now());

		vencidos.forEach(exp -> {

			exp.setEstado(CircuitoExpediente.ARCHIVO);
			m.setId(generateMovimientoId(exp));
			exp.getMovimientos().add(m);
		});

		expedienteRepo.saveAll(vencidos);
	}

}
