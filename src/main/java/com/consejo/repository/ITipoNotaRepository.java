package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.TipoNota;

@Repository
public interface ITipoNotaRepository extends JpaRepository <TipoNota,Integer> {

}
