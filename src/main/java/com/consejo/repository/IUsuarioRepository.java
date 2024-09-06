package com.consejo.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Usuario;

@Repository
public interface IUsuarioRepository extends JpaRepository <Usuario,Long>{

	public Usuario findByMail(String mail);
	
	@Query("SELECT u FROM Usuario u WHERE " +
		       "(:nombre IS NULL OR LOWER(u.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))) " +
		       "AND (:apellido IS NULL OR LOWER(u.apellido) LIKE LOWER(CONCAT('%', :apellido, '%')))")
		List<Usuario> findByNombreOrApellido(@Param("nombre") String nombre, @Param("apellido") String apellido);
}
