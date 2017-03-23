package com.mh.util.mail;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificacionEmailService {
	@Value("${mail.from}")
	private String mailFrom;

	@Value("${mail.from.personal}")
	private String mailFromPersonal;

	@Value("#{'${mail.to}'.split(',')}")
	private String[] mailTo;

	@Value("#{'${mail.cc}'.split(',')}")
	private String[] mailCc;

	@Value("${mail.subject}")
	private String subject;

	@Autowired
	private JavaMailSender mailSender;

	public void notificar(String data) {
		try {
			MimeMessage message = this.mailSender.createMimeMessage();

			// @formatter:off
			message = crearMimeMessage(
					message, 
					this.mailFrom, 
					this.mailFromPersonal, 
					this.mailTo, 
					this.mailCc, 
					this.subject, 
					data, 
					true, 
					new File[0]);
			// @formatter:on

			this.mailSender.send(message);
		} catch (RuntimeException e) {
			log.error("Ocurrio una excepción al realizar la notificación de las alertas", e);
			throw e;
		} finally {

		}
	}


	static private MimeMessage crearMimeMessage(MimeMessage message, String from, String fromPersonal, String to[],
			String cc[], String subject, String data, boolean html, File attachments[]) {

		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);

			helper.setFrom(from, fromPersonal);
			helper.setTo(Arrays.stream(to).filter(s -> !s.isEmpty()).toArray(String[]::new));
			helper.setCc(Arrays.stream(cc).filter(s -> !s.isEmpty()).toArray(String[]::new));
			helper.setSubject(subject);
			helper.setText(data, html);

			for (File attachment : attachments) {
				helper.addAttachment(attachment.getName(), attachment);
			}

			return message;
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new RuntimeException("Ocurrio una excepción al crear el mensaje de notificación", e);
		}
	}
}
