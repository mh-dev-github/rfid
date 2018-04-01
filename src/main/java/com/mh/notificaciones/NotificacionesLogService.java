package com.mh.notificaciones;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.model.esb.domain.esb.Integracion;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.repo.esb.IntegracionRepository;
import com.mh.servicios.entradasProducto.EntradasProductoLogsService;
import com.mh.servicios.locaciones.LocacionesLogsService;
import com.mh.servicios.ordenesProduccion.OrdenesProduccionLogsService;
import com.mh.servicios.pedidos.PedidosLogsService;
import com.mh.servicios.productos.ProductosLogsService;
import com.mh.servicios.salidasTienda.SalidasTiendaLogsService;
import com.mh.util.mail.NotificacionEmailService;

import lombok.val;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class NotificacionesLogService {
	private static final DateTimeFormatter formatterDate = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

	@Autowired
	PedidosLogsService pedidosService;

	@Autowired
	SalidasTiendaLogsService salidasTiendasService;

	@Autowired
	OrdenesProduccionLogsService ordenesProduccionService;

	@Autowired
	EntradasProductoLogsService entradasProductosService;

	@Autowired
	ProductosLogsService productosService;

	@Autowired
	LocacionesLogsService locacionesService;

	@Autowired
	private IntegracionRepository integracionRepository;

	@Autowired
	private NotificacionEmailService notificacionService;

	protected Integracion getIntegracion(IntegracionType tipoIntegracion) {
		return integracionRepository.findOneByCodigo(tipoIntegracion.toString());
	}

	@Transactional(readOnly = false)
	public void generarReporteAlertas() {
		Integracion integracion = getIntegracion(IntegracionType.ALERTAS_LOGS);
		LocalDateTime fechaDesde = integracion.getFechaUltimoPull();
		LocalDateTime fechaHasta = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

		Map<IntegracionType, List<LogDTO>> map = getData(fechaDesde, fechaHasta);

		boolean novedades = false;
		novedades |= !(map.get(IntegracionType.PEDIDOS)).isEmpty();
		novedades |= !(map.get(IntegracionType.SALIDAS_TIENDA)).isEmpty();
		novedades |= !(map.get(IntegracionType.ORDENES_DE_PRODUCCION)).isEmpty();
		novedades |= !(map.get(IntegracionType.ENTRADAS_PT)).isEmpty();
		novedades |= !(map.get(IntegracionType.PRODUCTOS)).isEmpty();
		novedades |= !(map.get(IntegracionType.LOCACIONES)).isEmpty();

		if (novedades) {
			try {
				String data = this.generarContenidoAlertas(fechaDesde, fechaHasta, map);
				notificacionService.notificar(data);
				integracion.setFechaUltimoPull(fechaHasta);
				integracionRepository.save(integracion);
			} catch (IOException e) {
				log.debug("Ocurrio el siguiente error durate la generación del reporte:", e);
			}
		}
	}

	@Transactional(readOnly = false)
	public void generarReporteConsolidados() {
		Integracion integracion = getIntegracion(IntegracionType.CONSOLIDADOS_LOGS);
		LocalDateTime fechaDesde = integracion.getFechaUltimoPull();
		LocalDateTime fechaHasta = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

		Map<IntegracionType, List<LogDTO>> map = getData(fechaDesde, fechaHasta);

		try {
			String data = this.generarContenidoConsolidado(fechaDesde, fechaHasta, map);
			notificacionService.notificar(data);
			integracion.setFechaUltimoPull(fechaHasta);
			integracionRepository.save(integracion);
		} catch (IOException e) {
			log.debug("Ocurrio el siguiente error durate la generación del reporte:", e);
		}
	}

	private Map<IntegracionType, List<LogDTO>> getData(LocalDateTime fechaDesde, LocalDateTime fechaHasta) {
		Map<IntegracionType, List<LogDTO>> map;
		List<LogDTO> pedidos = pedidosService.getLogs(fechaDesde, fechaHasta);
		List<LogDTO> salidas = salidasTiendasService.getLogs(fechaDesde, fechaHasta);
		List<LogDTO> ordenes = ordenesProduccionService.getLogs(fechaDesde, fechaHasta);
		List<LogDTO> entradas = entradasProductosService.getLogs(fechaDesde, fechaHasta);
		List<LogDTO> productos = productosService.getLogs(fechaDesde, fechaHasta);
		List<LogDTO> locaciones = locacionesService.getLogs(fechaDesde, fechaHasta);

		map = new TreeMap<IntegracionType, List<LogDTO>>();
		map.put(IntegracionType.PEDIDOS, pedidos);
		map.put(IntegracionType.SALIDAS_TIENDA, salidas);
		map.put(IntegracionType.ORDENES_DE_PRODUCCION, ordenes);
		map.put(IntegracionType.ENTRADAS_PT, entradas);
		map.put(IntegracionType.PRODUCTOS, productos);
		map.put(IntegracionType.LOCACIONES, locaciones);
		return map;
	}

	private String generarContenidoAlertas(LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Map<IntegracionType, List<LogDTO>> map) throws IOException {
		File file = new ClassPathResource("templates\\alerta.html").getFile();
		String data = new String(Files.readAllBytes(file.toPath()));

		data = data.replaceAll("\\$\\{fechaDesde\\}", fechaDesde.format(formatterDateTime))
				.replaceAll("\\$\\{fechaHasta\\}", fechaHasta.format(formatterDateTime))
				.replaceAll("\\$\\{pedidos\\}", log(IntegracionType.PEDIDOS, map))
				.replaceAll("\\$\\{salidas\\}", log(IntegracionType.SALIDAS_TIENDA, map))
				.replaceAll("\\$\\{ordenes\\}", log(IntegracionType.ORDENES_DE_PRODUCCION, map))
				.replaceAll("\\$\\{entradas\\}", log(IntegracionType.ENTRADAS_PT, map))
				.replaceAll("\\$\\{productos\\}", log(IntegracionType.PRODUCTOS, map))
				.replaceAll("\\$\\{locaciones\\}", log(IntegracionType.LOCACIONES, map));
		return data;
	}

	private String generarContenidoConsolidado(LocalDateTime fechaDesde, LocalDateTime fechaHasta,
			Map<IntegracionType, List<LogDTO>> map) throws IOException {
		File file = new ClassPathResource("templates\\consolidados.html").getFile();
		String data = new String(Files.readAllBytes(file.toPath()));

		data = data.replaceAll("\\$\\{fechaDesde\\}", fechaDesde.format(formatterDateTime))
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
				sb.append("				<td class=\"text-left\" nowrap>"
						+ e.getFechaUltimoPull().format(formatterDateTime) + "</td>\n");
				sb.append("			</tr>\n");
			}
			sb.append("			</tbody>\n");
			sb.append("			</table>\n");
		}

		return sb.toString();
	}

}