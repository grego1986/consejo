package com.consejo.daos;

import java.util.List;

import com.consejo.pojos.TipoNota;



public interface ITipoNotaDaos {

	public List<TipoNota> listarTipoNota ();
	public TipoNota buscarTipoNota (Integer id);
	public void agregarTipoNota (TipoNota tipo);
	public void eliminarTipoNota (Integer id);
}
