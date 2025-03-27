package com.consejo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.consejo.pojos.AsuntosEntrados;

@Repository
public interface IAsuntosEntradosRepository extends JpaRepository <AsuntosEntrados,Long> {

}
