package com.consejo.exceptions;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;;

@RestControllerAdvice(basePackages = "com.consejo.restController")
public class GlobalExceptionHandler {

	 @ExceptionHandler(ResourceNotFoundException.class)
	    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex.getMessage(), request.getRequestURI());
	    }

	    @ExceptionHandler(BadRequestException.class)
	    public ResponseEntity<ApiError> handleBadRequest(BadRequestException ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.BAD_REQUEST, "Solicitud inválida", ex.getMessage(), request.getRequestURI());
	    }

	    @ExceptionHandler(UnauthorizedException.class)
	    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.UNAUTHORIZED, "No autorizado", ex.getMessage(), request.getRequestURI());
	    }

	    @ExceptionHandler(ForbiddenException.class)
	    public ResponseEntity<ApiError> handleForbidden(ForbiddenException ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.FORBIDDEN, "Acceso prohibido", ex.getMessage(), request.getRequestURI());
	    }

	    @ExceptionHandler(Exception.class)
	    public ResponseEntity<ApiError> handleGeneral(Exception ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", ex.getMessage(), request.getRequestURI());
	    }

	    private ResponseEntity<ApiError> buildResponse(HttpStatus status, String error, String message, String path) {
	        ApiError apiError = new ApiError(LocalDateTime.now(), status.value(), error, message, path);
	        return ResponseEntity.status(status).body(apiError);
	    }
	    
	    @ExceptionHandler(ArchivoNoProcesadoException.class)
	    public ResponseEntity<ApiError> handleArchivo(ArchivoNoProcesadoException ex, HttpServletRequest request) {
	        return buildResponse(HttpStatus.BAD_REQUEST, "Error procesando archivo", ex.getMessage(), request.getRequestURI());
	    }
}
