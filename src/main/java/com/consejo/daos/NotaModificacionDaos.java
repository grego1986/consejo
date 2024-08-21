package com.consejo.daos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Nota;
import com.consejo.pojos.NotaModificacion;
import com.consejo.repository.INotaModificacionRepository;

@Service
public class NotaModificacionDaos implements INotaModificacionDaos {

	@Autowired
	private INotaModificacionRepository notaModiRepo;
	
	@Override
	public List<NotaModificacion> listarNota() {

		try {

			return notaModiRepo.findAll();

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
	public NotaModificacion BuscarNota(Long id) {

		try {
			return notaModiRepo.findById(id).orElse(null);
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
	public void eliminaNota(NotaModificacion nota) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void guardarNota(NotaModificacion nota) throws IOException {
		try {

			notaModiRepo.save(nota);

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
	public void modificarNota(NotaModificacion nuevaNota, Nota nota) throws IOException {

		/*
		try {
			if (notaModiRepo.existsById(nota.getId())) {
	            nota.setEsActiva(false);
	            nota.getModificaciones().add(nuevaNota);
	            notaRepo.save(nota);    
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
		*/
	}

}
