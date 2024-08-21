package com.consejo.daos;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import com.consejo.pojos.Ingreso;
import com.consejo.pojos.Usuario;


public interface IIngresoDaos {

	public List<Ingreso> listarIngresos();
	public Ingreso buscarIngreso(Long id);
	public void nuevoIngreso(Ingreso ingreso) throws IOException;
	public void eliminaIngreso(Ingreso ingreso);
	public List<Ingreso> listarIngresoUsuario (Usuario usuario);
	public List<Ingreso> listarIngresoDia(LocalDate fecha);

}
