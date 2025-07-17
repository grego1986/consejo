package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.AsuntosEntrados;

public interface IAsuntosEntradosDaos {

	public List<AsuntosEntrados> listarNota();
	public List<AsuntosEntrados> listarTatadosFalse();
	public AsuntosEntrados BuscarNota(Long id);
	public void eliminaNota(AsuntosEntrados nota);
	public void guardarNota(AsuntosEntrados nota) throws IOException;
	public void modificarAsunto (AsuntosEntrados asunto) throws IOException;
}
