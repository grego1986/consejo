package com.consejo.daos;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.consejo.pojos.Evento;
import com.consejo.repository.IEventoRepository;

@Service
public class EventoDaos implements IEventoDaos{

	@Autowired
	private IEventoRepository eventoRepo;
	
	@Override
	public List<Evento> listarEvento(LocalDate fecha) {
		
		return eventoRepo.findByFechaGreaterThanEqualOrderByFechaAsc(fecha);
	}

	@Override
	public Evento buscarEvento(Long id) {
		
		return eventoRepo.findById(id).orElse(null);
	}

	@Transactional
	@Override
	public void eliminaEvento(LocalDate fecha) {

        eventoRepo.deleteByFechaBefore(fecha);;	
	}

	@Override
	public void agregarEvento(Evento e) {
		
		eventoRepo.save(e);
	}

	@Override
	public void modificarEvento(Evento e) {
		
		if (eventoRepo.existsById(e.getId())) {
            eventoRepo.save(e);
        }
		
	}

}
