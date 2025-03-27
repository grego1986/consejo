package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Nota;

public interface IAsuntosEntradosDaos {

	public List<AsuntosEntrados> listarNota();
	public AsuntosEntrados BuscarNota(Long id);
	public void eliminaNota(AsuntosEntrados nota);
	public void guardarNota(AsuntosEntrados nota) throws IOException;
	public void modificarNota (AsuntosEntrados nuevaNota, Nota nota) throws IOException;
}
