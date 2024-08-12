package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository <Usuario,Long>{

}
