package com.consejo.daos;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.Nota;
import com.consejo.repository.INotaRepository;

import convertir.PdfToByteArray;


@Service
public class NotaDaos implements INotaDaos {

	@Autowired
	private INotaRepository notaRepo;
	
	
	@Override
	public List<Nota> listarNota() {
		
		return notaRepo.findAll();
	}

	@Override
	public Nota BuscarNota(Long id) {
	    
		return notaRepo.findById(id).orElse(null);
	}

	@Override
	public void agregarNota(Nota nota) {
	
		notaRepo.save(nota);
	}

	@Override
	public void eliminaNota(Nota nota) {
		
			notaRepo.delete(nota);		
	}

	@Override
	public void guardarNota(String titulo, byte[] bs) throws IOException {
		
		Nota nota = new Nota();
        nota.setTitulo(titulo);
        nota.setNota(bs);
        notaRepo.save(nota);
	}

	@Override
	public void guardarNota(String titulo, File pdfFile) throws IOException {
		Nota nota = new Nota();
        nota.setTitulo(titulo);
        nota.setNota(PdfToByteArray.convertPdfToByteArray(pdfFile));
        notaRepo.save(nota);		
	}

	
}
