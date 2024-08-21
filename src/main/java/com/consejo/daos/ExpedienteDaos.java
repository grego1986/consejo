package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

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
	public void guardarExpediente(Expediente expediente, Persona ciudadano, TipoNota tipoNota, int nroOrdenDia, Movimiento movimiento)	throws IOException {
		
		try {
			Expediente e = new Expediente();
			String nExpediente = createExpedienteId(ciudadano.getTipo().getId(), tipoNota.getTipo(), expediente.getFecha(), nroOrdenDia);
			e.setId(nExpediente);
			e.setCaratula(expediente.getCaratula());
			e.setDetalle(expediente.getDetalle());
			e.setFecha(expediente.getFecha());
			e.setPersona(ciudadano);
			e.setTipo(tipoNota);
			expedienteRepo.save(e);
			
			agregarMovimiento(e.getId(), movimiento);

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
	public void agregarMovimiento(String id, Movimiento movimiento) throws IOException {

		try {

			Expediente e = expedienteRepo.findById(id).orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));
			movimiento.setExpediente(e);
	        movimiento.setId(generateMovimientoId(e));
	        e.getMovimientos().add(movimiento);
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

	@Override
	public void modificarExpediente(String id, String detalle, String caratula, TipoNota tipo) throws IOException {

		try {
			
			Expediente e = expedienteRepo.findById(id).orElseThrow(() -> new Exception("Expediente no encontrado con ID: " + id));
			e.setCaratula(caratula);
			e.setDetalle(detalle);
			e.setTipo(tipo);
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
        // Lógica para generar el ID del movimiento basado en el expediente y el índice de la lista
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

}
