package com.consejo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Persona;

@Repository
public interface IPersonaRepository extends JpaRepository <Persona,Long> {

	List<Persona> findByNombreContaining(String nombre);
}
