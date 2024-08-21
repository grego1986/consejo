package com.consejo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.Ingreso;
import com.consejo.pojos.Usuario;


@Repository
public interface IIngresoRepository extends JpaRepository <Ingreso,Long> {

	// Consulta para listar los ingresos de un usuario específico
    List<Ingreso> findByUsuario(Usuario usuario);

    // Consulta para listar los ingresos en una fecha específica
    List<Ingreso> findByFecha(LocalDate fecha);

}
