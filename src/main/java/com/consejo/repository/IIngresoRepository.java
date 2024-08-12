package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Ingreso;


@Repository
public interface IIngresoRepository extends JpaRepository <Ingreso,Long> {

}
