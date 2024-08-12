package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Expediente;

@Repository
public interface IExpedienteRepository extends JpaRepository <Expediente,String> {

}
