package com.consejo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.OrdenDia;

@Repository
public interface IOrdenDiaRepository extends JpaRepository <OrdenDia,Long> {
	
	List<OrdenDia> findAllByTratadoFalse();

}
