package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.consejo.pojos.Permiso;

@Repository
public interface IPermisoRepository extends JpaRepository <Permiso,Integer>  {

}
