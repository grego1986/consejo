package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Movimiento;

@Repository
public interface IMovimientoRepository extends JpaRepository <Movimiento,String>{

}
