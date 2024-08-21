package com.consejo.daos;

import java.io.IOException;
import java.util.List;

import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;


public interface IMovimientoDaos {

	public List<Movimiento> listarMovimientoExpediente (Expediente e);
	public void agregarMovimiento (Movimiento m, Expediente e) throws IOException;
	
}
