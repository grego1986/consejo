package com.consejo.daos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String recipientEmail, String link) {
        String subject = "Restablecer contraseña";
        String content = "<p>Hola,</p>"
                + "<p>Has solicitado restablecer tu contraseña.</p>"
                + "<p>Haz clic en el enlace a continuación para restablecerla:</p>"
                + "<p><a href=\"" + link + "\">Restablecer mi contraseña</a></p>"
                + "<p>Si no solicitaste este cambio, ignora este correo.</p>";

        sendEmail(recipientEmail, subject, content);
    }

    private void sendEmail(String recipientEmail, String subject, String content) {
        MimeMessage message = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom("joseluisgregoretti@gmail.com"); // Configura el email del remitente
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new MailSendException("Error al enviar email de restablecimiento de contraseña", e);
        }
    }
    
    public void sendTestEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("joseluisgregoretti@gmail.com");
        message.setSubject("Prueba de conexión");
        message.setText("Este es un correo de prueba.");

        try {
        	mailSender.send(message);
            System.out.println("Correo enviado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error al enviar correo: " + e.getMessage());
        }
    }
    
}
