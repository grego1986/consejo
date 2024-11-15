package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.TipoCiudadano;

@Repository
public interface ITipoCiudadanoRepository extends JpaRepository <TipoCiudadano, Integer> {

}
