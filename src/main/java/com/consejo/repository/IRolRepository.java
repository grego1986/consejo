package com.consejo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Rol;

@Repository
public interface IRolRepository extends JpaRepository <Rol,Integer>  {

	Rol findByRol(String rol);
}
