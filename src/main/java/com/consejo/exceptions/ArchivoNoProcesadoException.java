package com.consejo.exceptions;

public class ArchivoNoProcesadoException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArchivoNoProcesadoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
	
}
