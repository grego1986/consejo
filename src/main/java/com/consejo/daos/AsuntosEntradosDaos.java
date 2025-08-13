package com.consejo.daos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.consejo.exceptions.BadRequestException;
import com.consejo.exceptions.InternalServerErrorException;
import com.consejo.exceptions.ResourceNotFoundException;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.repository.IAsuntosEntradosRepository;

@Service
public class AsuntosEntradosDaos implements IAsuntosEntradosDaos {

	@Autowired
	private IAsuntosEntradosRepository entradosRepo;
	
	@Override
	public List<AsuntosEntrados> listarNota() {
		
		try {
            List<AsuntosEntrados> asuntos = entradosRepo.findAll();
            return asuntos;
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al acceder a la base de datos.");
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al listar asuntos.");
        }
	}

	@Override
	public AsuntosEntrados BuscarNota(Long id) {
		
		try {
            return entradosRepo.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Asunto con ID " + id + " no encontrado."));
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al acceder a la base de datos.");
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al buscar el asunto.");
        }
	}

	@Override
	public void eliminaNota(AsuntosEntrados nota) {
		try {
            if (!entradosRepo.existsById(nota.getId())) {
                throw new ResourceNotFoundException("No se puede eliminar: el asunto no existe.");
            }
            entradosRepo.delete(nota);
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al acceder a la base de datos.");
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al eliminar el asunto.");
        }
	}

	@Override
	public void guardarNota(AsuntosEntrados nota){

		try {
            if (nota == null) {
                throw new BadRequestException("El asunto a guardar no puede ser nulo.");
            }
            nota.setTratado(false);
            entradosRepo.save(nota);
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al guardar en la base de datos.");
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("Datos inválidos: " + iae.getMessage());
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al guardar el asunto.");
        }
		
	}

	@Override
	public void modificarAsunto(AsuntosEntrados asunto) {

		try {
            if (asunto == null || asunto.getId() == null) {
                throw new BadRequestException("El asunto o su ID no pueden ser nulos.");
            }
            if (!entradosRepo.existsById(asunto.getId())) {
                throw new ResourceNotFoundException("No se puede modificar: el asunto no existe.");
            }
            entradosRepo.save(asunto);
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al acceder a la base de datos.");
        } catch (IllegalArgumentException iae) {
            throw new BadRequestException("Datos inválidos: " + iae.getMessage());
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al modificar el asunto.");
        }
		
	}

	@Override
	public List<AsuntosEntrados> listarTatadosFalse() {
		
		try {
            return entradosRepo.findAllByTratadoFalse(); // Devuelve lista vacía si no hay registros
        } catch (DataAccessException dae) {
            throw new InternalServerErrorException("Error al acceder a la base de datos.");
        } catch (Exception ex) {
            throw new InternalServerErrorException("Error inesperado al listar asuntos no tratados.");
        }
	}

}
