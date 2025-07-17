package com.consejo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.AsuntosEntrados;

@Repository
public interface IAsuntosEntradosRepository extends JpaRepository <AsuntosEntrados,Long> {

	List<AsuntosEntrados> findAllByTratadoFalse();
}
