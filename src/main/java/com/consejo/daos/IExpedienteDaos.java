package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.Expediente;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.Persona;
import com.consejo.pojos.TipoNota;

public interface IExpedienteDaos {
	
	public List<Expediente> listarExpedientes();
	public List<Expediente> listarExpedientes (CircuitoExpediente estado);
	public Expediente buscarExpediente(String id);
	public Optional <Expediente> buscarExpedienteOptional(String id);
	public void guardarExpediente(Expediente expediente) throws IOException;
	public void eliminaExpediente(Expediente expediente);
	public void agregarMovimiento (String id, Movimiento movimiento,CircuitoExpediente circuito) throws IOException;
	public void modificarExpediente (Expediente e, String id) throws IOException;
	public void moverAOficinaParlamentaria(Expediente expediente) throws IOException ;
	public void moverAComisionAmbientalYEconomia (Expediente expediente) throws IOException;
	public void moverAGobiernoYSocial (Expediente expediente) throws IOException;
	public void moverAAmbasComisiones (Expediente expediente) throws IOException;
	public void moverAArchivo (Expediente expediente) throws IOException;
	public void moverAPresidencia (Expediente expediente) throws IOException;
	public void moverABloque_A (Expediente expediente) throws IOException;
	public void moverABloque_B (Expediente expediente) throws IOException;
	public void moverABloque_C(Expediente expediente) throws IOException;
	public void moverATodosBloques (Expediente expediente) throws IOException;
	public void moverADespachoComision (Expediente expediente) throws IOException;
	public void moverALegislacion (Expediente expediente) throws IOException;
	public void moverANotasComision (Expediente expediente) throws IOException;
	public void moverARepuestaDestinatario (Expediente expediente) throws IOException;
	public void moverANotaMunicipio (Expediente expediente) throws IOException;
	public void moverARepuestaMunicipio (Expediente expediente) throws IOException;
	public void moverAFin(Expediente expediente) throws IOException;
	public List<Expediente> buscarExpediente (LocalDate fecha, String detalle, String id, String caratula, String nombrePersona);
	public List<Expediente> buscarPorRangoDeFechasYCircuitos(LocalDate startDate, LocalDate endDate);

}
