package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import com.consejo.pojos.Evento;

public interface IEventoDaos {

	public List<Evento> listarEvento(LocalDate fecha);
	public Evento buscarEvento(Long id);
	public void eliminaEvento(LocalDate fecha);
	public void agregarEvento(Evento e);
	public void modificarEvento (Evento e);
}
