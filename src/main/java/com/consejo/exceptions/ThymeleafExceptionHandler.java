package com.consejo.exceptions;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice (basePackages = "com.consejo.controller")
public class ThymeleafExceptionHandler {

	 @ExceptionHandler(ResourceNotFoundException.class)
	    public String handleNotFound(ResourceNotFoundException ex, Model model) {
	        model.addAttribute("errorTitle", "Recurso no encontrado");
	        model.addAttribute("errorMessage", ex.getMessage());
	        return "error/404"; // vista Thymeleaf (templates/error/404.html)
	    }

	    @ExceptionHandler(BadRequestException.class)
	    public String handleBadRequest(BadRequestException ex, Model model) {
	        model.addAttribute("errorTitle", "Solicitud inválida");
	        model.addAttribute("errorMessage", ex.getMessage());
	        return "error/400";
	    }

	    @ExceptionHandler(Exception.class)
	    public String handleGeneral(Exception ex, Model model) {
	        model.addAttribute("errorTitle", "Error interno");
	        model.addAttribute("errorMessage", ex.getMessage());
	        return "error/500";
	    }
	    
	    @ExceptionHandler(UnauthorizedException.class)
	    public String handleUnauthorized(UnauthorizedException ex, Model model) {
	        model.addAttribute("errorTitle", "No autorizado");
	        model.addAttribute("errorMessage", ex.getMessage());
	        return "error/401"; // vista Thymeleaf (templates/error/401.html)
	    }

	    @ExceptionHandler(ForbiddenException.class)
	    public String handleForbidden(ForbiddenException ex, Model model) {
	        model.addAttribute("errorTitle", "Acceso prohibido");
	        model.addAttribute("errorMessage", ex.getMessage());
	        return "error/403"; // vista Thymeleaf (templates/error/403.html)
	    }
	
}
