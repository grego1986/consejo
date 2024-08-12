package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.NotaModificacion;

@Repository
public interface INotaModificacionRepository extends JpaRepository <NotaModificacion,Long> {

}
