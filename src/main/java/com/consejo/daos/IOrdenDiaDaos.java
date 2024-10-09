package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.Nota;
import com.consejo.pojos.OrdenDia;

public interface IOrdenDiaDaos {

	public List<OrdenDia> listarNota();
	public OrdenDia BuscarNota(Long id);
	public void eliminaNota(OrdenDia nota);
	public void guardarNota(OrdenDia nota) throws IOException;
	public void modificarNota (OrdenDia nuevaNota, Nota nota) throws IOException;
}
