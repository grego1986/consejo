package com.consejo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;

@Repository
public interface IExpedienteRepository extends JpaRepository <Expediente,String>, JpaSpecificationExecutor<Expediente> {
	
	List<Expediente> findByEstado(CircuitoExpediente estado);

	@Query("SELECT e FROM Expediente e " +
		       "JOIN e.movimientos m " +
		       "WHERE m.fecha = (SELECT MAX(m2.fecha) FROM Movimiento m2 WHERE m2.expediente = e) " +
		       "AND m.fecha BETWEEN :startDate AND :endDate " +
		       "AND e.estado IN :estados")
		List<Expediente> findByUltimoMovimientoFechaBetweenAndCircuitoIn(
		    @Param("startDate") LocalDate startDate,
		    @Param("endDate") LocalDate endDate,
		    @Param("estados") List<CircuitoExpediente> circuitos);
	
	 List<Expediente> findByFechaBetweenAndEstadoNotIn(
	            LocalDate desde,
	            LocalDate hasta,
	            List<CircuitoExpediente> estadosExcluidos);
	 
	 
	 List<Expediente> findByFechaBeforeAndEstadoNotIn(
	            LocalDate fecha,
	            List<CircuitoExpediente> estadosExcluidos
	    );
	

}
