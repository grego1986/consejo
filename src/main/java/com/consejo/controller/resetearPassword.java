package com.consejo.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.consejo.daos.EmailService;
import com.consejo.daos.PasswordResetTokenDaos;
import com.consejo.daos.UsuarioDaos;
import com.consejo.pojos.PasswordResetToken;
import com.consejo.pojos.Usuario;

@Controller
public class resetearPassword {

	@Autowired
    private UsuarioDaos usuarioServi;
    @Autowired
    private EmailService emailServi;  // Un servicio para enviar correos
    @Autowired
    private PasswordResetTokenDaos resetServi;
    
    @GetMapping("/forgot-password")
    public String processForgotPassword( Model model) {
    	return "forgot-password";
    }
    
    
    @PostMapping("/forgot-password")
    public String processForgotPassword(@RequestParam("email") String email, Model model) {
        // Buscar el usuario por email
        Usuario usuario = usuarioServi.buscarPorMail(email);
        if (usuario == null) {
            model.addAttribute("error", "No existe una cuenta asociada a ese email.");
            return "forgot-password"; // Redirigir a la página de olvidé mi contraseña
        }

        // Generar el token y guardarlo
        String token = UUID.randomUUID().toString();
        resetServi.createPasswordResetToken(usuario, token);

        // Crear el enlace de restablecimiento de contraseña
        String resetPasswordLink = "http://192.168.0.4:8080/resetearPassword/reset-password?token=" + token;

        // Enviar el email con el enlace
        emailServi.sendPasswordResetEmail(email, resetPasswordLink);

        model.addAttribute("message", "Se ha enviado un enlace para restablecer la contraseña a su email.");
        return "forgot-password";
    }
    
    @GetMapping("/resetearPassword/reset-password")
    public String showResetPasswordForm(@RequestParam("token") String token, Model model) {
        // Verificar si el token es válido
        PasswordResetToken passwordResetToken = resetServi.getTokenByToken(token);

        if (passwordResetToken == null || passwordResetToken.isExpired()) {
            model.addAttribute("error", "El enlace de restablecimiento es inválido o ha expirado.");
            return "reset-password";
        }

        // Si el token es válido, mostrar el formulario para cambiar la contraseña
        model.addAttribute("token", token); // Para enviarlo al formulario
        return "reset-password";
    }

    @PostMapping("/resetearPassword/reset-password")
    public String processResetPassword(@RequestParam("token") String token,
                                       @RequestParam("newPassword") String newPassword,
                                       Model model) {
        PasswordResetToken passwordResetToken = resetServi.getTokenByToken(token);

        if (passwordResetToken == null || passwordResetToken.isExpired()) {
            model.addAttribute("error", "El enlace de restablecimiento es inválido o ha expirado.");
            return "reset-password";
        }

        // Cambiar la contraseña del usuario
        Usuario usuario = passwordResetToken.getUsuario();
        usuarioServi.recuperarAcceso(usuario, newPassword);

        // Eliminar el token después de usarlo
        resetServi.eliminarToken(usuario);

        model.addAttribute("message", "Su contraseña ha sido actualizada exitosamente.");
        return "login";  // Redirigir al formulario de login
    }
}
