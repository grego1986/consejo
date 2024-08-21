package com.consejo.daos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.TipoCiudadano;
import com.consejo.repository.ITipoCiudadanoRepository;

@Service
public class TipoCiudadanoDaos implements ITipoCiudadanoDaos {

	@Autowired
	private ITipoCiudadanoRepository tipoCiudadanoRepo;

	@Override
	public List<TipoCiudadano> listarTipoCiudadano() {
		
		return tipoCiudadanoRepo.findAll();
	}

	@Override
	public TipoCiudadano buscarTipoCiudadano(Integer id) {
		
		return tipoCiudadanoRepo.findById(id).orElse(null);
	}

	@Override
	public void agregarTipoCiudadano(TipoCiudadano tipo) {
		
		tipoCiudadanoRepo.save(tipo);
	}

	@Override
	public void eliminarTipoCiudadano(Integer id) {
		// TODO Auto-generated method stub
		
	}
	
	
}
