package br.com.gfctech.project_manager.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${spring.mail.username}")
	private String sender;
	
	public String enviarEmailTexto(String recipient, String subject, String message) {
		try {
			SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
			simpleMailMessage.setFrom(sender);
			simpleMailMessage.setTo(recipient);
			simpleMailMessage.setSubject(subject);
			simpleMailMessage.setText(message);
			
			javaMailSender.send(simpleMailMessage);
	
			// Verifique se o e-mail foi enviado corretamente.
			return "Email enviado para " + recipient;
	
		} catch (Exception e) {
			// Registre o erro completo para mais informações sobre o erro.
			e.printStackTrace();
			return "Erro ao tentar enviar email: " + e.getMessage();
		}
	}
	

}
