package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoNota;
import com.consejo.repository.IExpedienteRepository;

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
				moverAOficinaParlamentaria(e);
				break;
				
			case COMISION_DE_GOBIERNO_Y_DESARROLLO_SOCIAL:
				moverAGobiernoYSocial(e);
				break;
				
			case COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA:
				moverAComisionAmbientalYEconomia(e);
				break;
				
			case AMBAS_COMISIONES:
				moverAAmbasComisiones(e);
				break;
				
			case ARCHIVO:
				moverAArchivo(e);
				break;
				
			case PRESIDENCIA:
				moverAPresidencia(e);
				break;
				
			case BLOQUE_A:
				moverABloque_A(e);
				break;
				
			case BLOQUE_B:
				moverABloque_B(e);
				break;

			case BLOQUE_C:
				moverABloque_B(e);
				break;
				
			case TODOS_LOS_BLOQUES:
				moverATodosBloques(e);
				break;
				
			case LEGISLACION:
				moverALegislacion(e);
				break;
				
			case DESPACHOS_DE_COMISION:
				moverADespachoComision(e);
				break;
				
			case NOTAS_DE_COMISION:
				moverANotasComision(e);
				break;
				
			case REPUESTA_A_DESTINATARIO:
				moverARepuestaDestinatario(e);
				break;
				
			case NOTA_MUNICIPIO:
				moverANotaMunicipio(e);
				break;
				
			case REPUESTA_MUNICIPIO:
				moverARepuestaMunicipio(e);
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

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.OFICINA_PARLAMENTARIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAComisionAmbientalYEconomia(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAGobiernoYSocial(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.COMISION_DE_DESARROLLO_URBANO_AMBIENTAL_Y_ECONOMIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAAmbasComisiones(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.AMBAS_COMISIONES);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAArchivo(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.ARCHIVO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverAPresidencia(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.PRESIDENCIA);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_A(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_A);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_B(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_B);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverABloque_C(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
			expediente.setEstado(CircuitoExpediente.BLOQUE_C);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverATodosBloques(Expediente expediente) throws IOException {

		if (expediente.getEstado() == CircuitoExpediente.INGRESO) {
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
			expediente.setEstado(CircuitoExpediente.REPUESTA_A_DESTINATARIO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}
	}

	@Override
	public void moverANotaMunicipio(Expediente expediente) throws IOException {
		if (expediente.getEstado() == CircuitoExpediente.NOTAS_DE_COMISION) {
			expediente.setEstado(CircuitoExpediente.NOTA_MUNICIPIO);
			expedienteRepo.save(expediente);
		} else {
			throw new IllegalStateException("El expediente no se encuentra en la etapa correcta");
		}

	}

	@Override
	public void moverARepuestaMunicipio(Expediente expediente) throws IOException {
		if (expediente.getEstado() == CircuitoExpediente.NOTA_MUNICIPIO) {
			expediente.setEstado(CircuitoExpediente.REPUESTA_MUNICIPIO);
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

}