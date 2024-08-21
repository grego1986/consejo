package com.consejo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;

@Repository
public interface IMovimientoRepository extends JpaRepository <Movimiento, String>{

	List<Movimiento> findByExpediente(Expediente expediente);;
}
