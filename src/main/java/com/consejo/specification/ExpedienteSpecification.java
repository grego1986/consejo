package com.consejo.specification;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.consejo.pojos.Expediente;

import jakarta.persistence.criteria.Predicate;

public class ExpedienteSpecification {

	public static Specification<Expediente> buscarExpedientes(
            LocalDate fecha,
            String detalle,
            String id,
            String caratula,
            String nombrePersona) {

		return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por fecha de ingreso
            if (fecha != null) {
                predicates.add(criteriaBuilder.equal(root.get("fecha"), fecha));
            }

            // Filtro por detalle (coincidencia parcial)
            if (detalle != null && !detalle.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("detalle"), "%" + detalle + "%"));
            }

            // Filtro por ID (coincidencia parcial)
            if (id != null && !id.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("id"), "%" + id + "%"));
            }

            // Filtro por carátula (coincidencia parcial)
            if (caratula != null && !caratula.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("caratula"), "%" + caratula + "%"));
            }

            // Filtro por nombre de la persona (coincidencia parcial)
            if (nombrePersona != null && !nombrePersona.isEmpty()) {
                predicates.add(criteriaBuilder.like(root.join("persona").get("nombre"), "%" + nombrePersona + "%"));
            }

            // Ordenar por fecha de ingreso
            query.orderBy(criteriaBuilder.desc(root.get("fecha")));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
