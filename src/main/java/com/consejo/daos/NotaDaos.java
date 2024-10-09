package com.consejo.daos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Nota;
import com.consejo.repository.INotaRepository;

@Service
public class NotaDaos implements INotaDaos {

	@Autowired
	private INotaRepository notaRepo;

	@Override
	public List<Nota> listarNota() {

		try {

			return notaRepo.findAll();

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
	public Nota BuscarNota(Long id) {

		try {
			return notaRepo.findById(id).orElse(null);
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

	/*
	 * @Override public void agregarNota(Nota nota) {
	 * 
	 * notaRepo.save(nota); }
	 */

	@Override
	public void eliminaNota(Nota nota) {

		// notaRepo.delete(nota);
	}

	@Override
	public void guardarNota(Nota nota) throws IOException {

		try {

			notaRepo.save(nota);

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
	public void modificarNota( Nota nota) throws IOException {

		try {
			if (notaRepo.existsById(nota.getId())) {
	            nota.setEsActiva(true);
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

	}

	/*
	 * @Override public void guardarNota(String titulo, File pdfFile) throws
	 * IOException { Nota nota = new Nota(); nota.setTitulo(titulo);
	 * nota.setNota(PdfToByteArray.convertPdfToByteArray(pdfFile));
	 * notaRepo.save(nota); }
	 */

}
