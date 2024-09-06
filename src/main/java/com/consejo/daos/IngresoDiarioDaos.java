package com.consejo.daos;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.consejo.pojos.IngresoDiario;
import com.consejo.repository.IIngresoDiarioRepository;

@Service
public class IngresoDiarioDaos implements IIngresoDiarioDaos {

	
	@Autowired
	private IIngresoDiarioRepository ingresoDiarioRepository;

	@Override
	public int getNumeroDeIngreso() {
		// Obtén el registro único de la base de datos
        Optional<IngresoDiario> ingresoDiarioOpt = ingresoDiarioRepository.findById(1);

        IngresoDiario ingresoDiario;
        if (ingresoDiarioOpt.isEmpty()) {
            // Si no existe, crea uno nuevo
            ingresoDiario = new IngresoDiario();
            ingresoDiario.setFecha(LocalDate.now());
            ingresoDiario.setContador(0);
            ingresoDiario.setId(1);
        } else {
            ingresoDiario = ingresoDiarioOpt.get();
            // Si es un nuevo día, reinicia el contador
            if (!ingresoDiario.getFecha().equals(LocalDate.now())) {
                ingresoDiario.setFecha(LocalDate.now());
                ingresoDiario.setContador(0);
            }
        }

        // Incrementa el contador y guarda el registro
        int nuevoContador = ingresoDiario.getContador() + 1;
        ingresoDiario.setContador(nuevoContador);
        ingresoDiarioRepository.save(ingresoDiario);

        return nuevoContador;
	}

}
