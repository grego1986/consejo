package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Persona;

@Repository
public interface IPersonaRepository extends JpaRepository <Persona,Long> {

}
