package com.consejo.daos;

import java.io.IOException;
import java.util.List;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoNota;

public interface IExpedienteDaos {

	public List<Expediente> listarExpedientes();
	public Expediente buscarExpediente(String id);
	public void guardarExpediente(Expediente expediente) throws IOException;
	public void eliminaExpediente(Expediente expediente);
	public void agregarMovimiento (String id, Movimiento movimiento) throws IOException;
	public void modificarExpediente (String id, String detalle, String caratula, TipoNota tipo) throws IOException;

}
