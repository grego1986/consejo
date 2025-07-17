package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.OrdenDia;

public interface IOrdenDiaDaos {

	public List<OrdenDia> listarNota();
	public List<OrdenDia> listarTratadoFalse();
	public OrdenDia BuscarNota(Long id);
	public void eliminaNota(OrdenDia nota);
	public void guardarNota(OrdenDia nota) throws IOException;
	public void modificarOrden (OrdenDia orden) throws IOException;
	
}
