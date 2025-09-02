package com.consejo.restController.dto;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.AsuntosEntrados;
import com.consejo.pojos.Movimiento;
import com.consejo.pojos.OrdenDia;
import com.consejo.pojos.TipoNota;
import com.consejo.pojos.Usuario;
import com.consejo.restController.ExpedienteRestController;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(implementation = ExpedienteDto.class)
public class ExpedienteDto extends RepresentationModel<ExpedienteDto>{

	private String id; 
	private String caratula;
	private String detalle;
	private LocalDate fecha;
	private CircuitoExpediente estado;
	private List<CircuitoExpediente> circuitos_Disponibles = new ArrayList<>();
	private boolean fincircuito;
	private EntityModel<PersonaReducidoDto> persona;
	private List<EntityModel<MovimientoDto>> movimientos = new ArrayList<>();
	//private TipoNota tipo;
	//private List<Usuario> usuariosAsignados = new ArrayList<>();
	//private List<OrdenDia> ordenDia = new ArrayList<>();
	//private List<AsuntosEntrados> asuntos = new ArrayList<>();

	public ExpedienteDto() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
	}

	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public CircuitoExpediente getEstado() {
		return estado;
	}

	public void setEstado(CircuitoExpediente estado) {
		this.estado = estado;
	}

	
	public List<CircuitoExpediente> getCircuitos_Disponibles() {
		return circuitos_Disponibles;
	}

	public void setCircuitos_Disponibles(List<CircuitoExpediente> circuitos_Disponibles) {
		this.circuitos_Disponibles = circuitos_Disponibles;
	}

	public boolean isFincircuito() {
		return fincircuito;
	}

	public void setFincircuito(boolean fincircuito) {
		this.fincircuito = fincircuito;
	}

	public void setPersona(EntityModel<PersonaReducidoDto> persona) {
		this.persona = persona;
	}

	public EntityModel<PersonaReducidoDto> getPersona() {
		return persona;
	}


	public List<EntityModel<MovimientoDto>> getMovimientos() {
		return movimientos;
	}

	public void setMovimientos(List<EntityModel<MovimientoDto>> movimientos) {
		this.movimientos = movimientos;
	}
	
/*	public TipoNota getTipo() {
		return tipo;
	}

	public void setTipo(TipoNota tipo) {
		this.tipo = tipo;
	}

	public List<Usuario> getUsuariosAsignados() {
		return usuariosAsignados;
	}

	public void setUsuariosAsignados(List<Usuario> usuariosAsignados) {
		this.usuariosAsignados = usuariosAsignados;
	}

	public List<OrdenDia> getOrdenDia() {
		return ordenDia;
	}

	public void setOrdenDia(List<OrdenDia> ordenDia) {
		this.ordenDia = ordenDia;
	}

	public List<AsuntosEntrados> getAsuntos() {
		return asuntos;
	}

	public void setAsuntos(List<AsuntosEntrados> asuntos) {
		this.asuntos = asuntos;
	}
*/
}
