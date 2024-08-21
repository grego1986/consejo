package com.consejo.daos;

import java.util.List;

import com.consejo.pojos.TipoCiudadano;

public interface ITipoCiudadanoDaos {

	public List<TipoCiudadano> listarTipoCiudadano ();
	public TipoCiudadano buscarTipoCiudadano (Integer id);
	public void agregarTipoCiudadano (TipoCiudadano tipo);
	public void eliminarTipoCiudadano (Integer id);
}
