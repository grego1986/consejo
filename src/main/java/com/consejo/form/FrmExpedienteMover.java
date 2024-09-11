package com.consejo.form;

import java.time.LocalDate;

import org.springframework.web.multipart.MultipartFile;

import com.consejo.enumeraciones.CircuitoExpediente;
import com.consejo.pojos.Nota;

public class FrmExpedienteMover {

	private String id;
	private Long dni;
	private String nombre;
	private String detalleMovimiento;
    private String caratula;
    private LocalDate fecha;
    private String estado;
    private String tituloNota;
    private MultipartFile nota;
    private CircuitoExpediente circuito;
    
	public FrmExpedienteMover() {
		super();
	}

	public Long getDni() {
		return dni;
	}

	public void setDni(Long dni) {
		this.dni = dni;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getCaratula() {
		return caratula;
	}

	public void setCaratula(String caratula) {
		this.caratula = caratula;
	}

	public LocalDate getFecha() {
		return fecha;
	}

	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getTituloNota() {
		return tituloNota;
	}

	public void setTituloNota(String tituloNota) {
		this.tituloNota = tituloNota;
	}

	
	public MultipartFile getNota() {
		return nota;
	}

	public void setNota(MultipartFile nota) {
		this.nota = nota;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDetalleMovimiento() {
		return detalleMovimiento;
	}

	public void setDetalleMovimiento(String detalleMovimiento) {
		this.detalleMovimiento = detalleMovimiento;
	}

	public CircuitoExpediente getCircuito() {
		return circuito;
	}

	public void setCircuito(CircuitoExpediente circuito) {
		this.circuito = circuito;
	}
    
    
	
}
