package com.mh.api.alertas.servicios;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.model.esb.domain.esb.IntegracionType;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificacionService {
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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

	public void notificar(LocalDateTime fechaDesde, LocalDateTime fechaHasta, Map<IntegracionType, List<LogDTO>> map) {
		try {
			String data = generarContenido(fechaDesde,fechaHasta,map);

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
		} catch (IOException e) {
			log.error("Ocurrio una excepción al realizar la notificación de las alertas", e);
			throw new RuntimeException("Ocurrio una excepción al realizar la notificación de las alertas", e);
		} catch (RuntimeException e) {
			log.error("Ocurrio una excepción al realizar la notificación de las alertas", e);
			throw e;
		} finally {

		}
	}

	private String generarContenido(LocalDateTime fechaDesde, LocalDateTime fechaHasta, Map<IntegracionType, List<LogDTO>> map) throws IOException {
		File file = new ClassPathResource("templates\\alerta.html").getFile();
		String data = new String(Files.readAllBytes(file.toPath()));

		data = data
				.replaceAll("\\$\\{fechaDesde\\}", fechaDesde.format(formatterDateTime))
				.replaceAll("\\$\\{fechaHasta\\}", fechaHasta.format(formatterDateTime))
				.replaceAll("\\$\\{pedidos\\}", log(IntegracionType.PEDIDOS, map))
				.replaceAll("\\$\\{salidas\\}", log(IntegracionType.SALIDAS_TIENDA, map))
				.replaceAll("\\$\\{ordenes\\}", log(IntegracionType.ORDENES_DE_PRODUCCION, map))
				.replaceAll("\\$\\{entradas\\}", log(IntegracionType.ENTRADAS_PT, map))
				.replaceAll("\\$\\{productos\\}", log(IntegracionType.PRODUCTOS, map))
				.replaceAll("\\$\\{locaciones\\}", log(IntegracionType.LOCACIONES, map));
		return data;
	}

	private String log(IntegracionType tipoIntegracion, Map<IntegracionType, List<LogDTO>> map) {
		List<LogDTO> list = map.get(tipoIntegracion);
		// @formatter:off
		val sorted = list
				.stream()
				.sorted((a,b) -> a.getStatus().compareTo(b.getStatus()))
				.collect(Collectors.toList());
		// @formatter:on
		StringBuilder sb = new StringBuilder();
		sb.append("			<div class=\"table-title\">\n");
		sb.append("				<h3>" + tipoIntegracion.getNombrePlural() + "</h3>\n");
		sb.append("			</div>\n");

		if (list.isEmpty()) {
			sb.append("			<div class=\"table-title\">\n");
			sb.append("			<p><b>SIN NOVEDADES</b></p>\n");
			sb.append("			</div>\n");
		} else {
			sb.append("			<table class=\"table-fill\">\n");
			sb.append("			<thead>\n");
			sb.append("				<tr>\n");
			sb.append("					<th class\"text-left\">Fecha</th>\n");
			sb.append("					<th class=\"text-left\">External ID</th>\n");
			sb.append("					<th class=\"text-left\">ID</th>\n");
			sb.append("					<th class=\"text-left\">MID</th>\n");
			sb.append("					<th class=\"text-left\">Tipo</th>\n");
			sb.append("					<th class=\"text-left\">Estado</th>\n");
			sb.append("					<th class=\"text-left\">Código</th>\n");
			sb.append("					<th class=\"text-left\">Mensaje</th>\n");
			sb.append("					<th class=\"text-left\">Excepción</th>\n");
			sb.append("					<th class=\"text-left\">Pull</th>\n");
			sb.append("				</tr>\n");
			sb.append("			</thead>\n");
			sb.append("			<tbody class=\"table-hover\">\n");
			for (LogDTO e : sorted) {
				sb.append("			<tr>\n");
				sb.append("				<td class=\"text-left\">" + e.getFecha().format(formatterDate) + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getExternalId() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getId() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getMid() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getType().toString() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getStatus().toString() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getCode() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getMessage() + "</td>\n");
				sb.append("				<td class=\"text-left\">" + e.getException() + "</td>\n");
				sb.append("				<td class=\"text-left\" nowrap>" + e.getFechaUltimoPull().format(formatterDateTime) + "</td>\n");
				sb.append("			</tr>\n");
			}
			sb.append("			</tbody>\n");
			sb.append("			</table>\n");

		}

		return sb.toString();
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
