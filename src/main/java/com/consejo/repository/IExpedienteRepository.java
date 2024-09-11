package com.consejo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.Expediente;

@Repository
public interface IExpedienteRepository extends JpaRepository <Expediente,String> {
	
	List<Expediente> findByEstado(CircuitoExpediente estado);

}
