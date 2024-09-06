package com.consejo.daos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.repository.IMovimientoRepository;

@Service
public class MovimientoDaos implements IMovimientoDaos {
	
	@Autowired
	private IMovimientoRepository movimientoRepo;
	
	@Override
	public List<Movimiento> listarMovimientoExpediente(Expediente e) {
		try {

			return movimientoRepo.findByExpediente(e);

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
	public void agregarMovimiento(Movimiento m) throws IOException{
			try {

		        movimientoRepo.save(m);
				
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
		
	}

