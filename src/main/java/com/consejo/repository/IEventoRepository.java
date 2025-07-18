package com.consejo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Evento;

@Repository
public interface IEventoRepository extends JpaRepository <Evento, Long> {

	List<Evento> findByFechaGreaterThanEqualOrderByFechaAsc(LocalDate fecha);
	void deleteByFechaBefore(LocalDate fecha);
}
