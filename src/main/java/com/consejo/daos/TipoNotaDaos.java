package com.consejo.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.TipoNota;
import com.consejo.repository.ITipoNotaRepository;

@Service
public class TipoNotaDaos implements ITipoNotaDaos {

	@Autowired
	private ITipoNotaRepository tipoNotaRepo;

	@Override
	public List<TipoNota> listarTipoNota() {
		
		return tipoNotaRepo.findAll();
	}

	@Override
	public TipoNota buscarTipoNota(Integer id) {
		
		return tipoNotaRepo.findById(id).orElse(null);
	}

	@Override
	public void agregarTipoNota(TipoNota tipo) {
		
		tipoNotaRepo.save(tipo);
	}

	@Override
	public void eliminarTipoNota(Integer id) {
		// TODO Auto-generated method stub
		
	}
	
	
}
