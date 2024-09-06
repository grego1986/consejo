package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.IngresoDiario;

@Repository
public interface IIngresoDiarioRepository extends JpaRepository <IngresoDiario, Integer>  {

}
