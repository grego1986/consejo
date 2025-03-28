package com.consejo.daos;


import java.io.IOException;
import java.util.List;
import com.consejo.pojos.Nota;

public interface INotaDaos {

	public List<Nota> listarNota();
	public Nota BuscarNota(Long id);
	//public void agregarNota(Nota nota);
	public void eliminaNota(Nota nota);
	//public void guardarNota(String titulo, File pdfFile) throws IOException;
	void guardarNota(Nota nota) throws IOException;
	public void modificarNota ( Nota nota) throws IOException;
}
