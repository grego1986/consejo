package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.TipoUsuario;

@Repository
public interface ITipoUsuarioRepository extends JpaRepository <TipoUsuario,Integer> {

}
