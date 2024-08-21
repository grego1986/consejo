package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.Nota;
import com.consejo.pojos.NotaModificacion;

public interface INotaModificacionDaos {

	public List<NotaModificacion> listarNota();
	public NotaModificacion BuscarNota(Long id);
	public void eliminaNota(NotaModificacion nota);
	public void guardarNota(NotaModificacion nota) throws IOException;
	public void modificarNota (NotaModificacion nuevaNota, Nota nota) throws IOException;
}
