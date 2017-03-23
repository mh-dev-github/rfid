package com.mh.api.alertas.servicios;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mh.api.mensajes.dto.LogDTO;
import com.mh.model.esb.domain.esb.Integracion;
import com.mh.model.esb.domain.esb.IntegracionType;
import com.mh.model.esb.repo.esb.IntegracionRepository;

@Service
public class AlertasLogService {
	@Autowired
	PedidosAlertasService pedidosService;

	@Autowired
	SalidasTiendaAlertasService salidasTiendasService;

	@Autowired
	OrdenesProduccionAlertasService ordenesProduccionService;

	@Autowired
	EntradasProductoAlertasService entradasProductosService;

	@Autowired
	ProductosAlertasService productosService;

	@Autowired
	LocacionesAlertasService locacionesService;

	@Autowired
	private IntegracionRepository integracionRepository;

	@Autowired
	private NotificacionService notificacionService;

	protected Integracion getIntegracion(IntegracionType tipoIntegracion) {
		return integracionRepository.findOneByCodigo(tipoIntegracion.toString());
	}

	@Transactional(readOnly = false)
	public void generarReporteAlertas() {
		Integracion integracion = getIntegracion(IntegracionType.ALERTAS_LOGS);
		LocalDateTime fechaDesde = integracion.getFechaUltimoPull();
		LocalDateTime fechaHasta = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
		boolean novedades = false;

		List<LogDTO> pedidos = pedidosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> salidas = salidasTiendasService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> ordenes = ordenesProduccionService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> entradas = entradasProductosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> productos = productosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> locaciones = locacionesService.alertar(fechaDesde, fechaHasta);

		novedades |= !pedidos.isEmpty();
		novedades |= !salidas.isEmpty();
		novedades |= !ordenes.isEmpty();
		novedades |= !entradas.isEmpty();
		novedades |= !productos.isEmpty();
		novedades |= !locaciones.isEmpty();

		if (novedades) {

			Map<IntegracionType, List<LogDTO>> map = new TreeMap<IntegracionType, List<LogDTO>>();
			map.put(IntegracionType.PEDIDOS, pedidos);
			map.put(IntegracionType.SALIDAS_TIENDA, salidas);
			map.put(IntegracionType.ORDENES_DE_PRODUCCION, ordenes);
			map.put(IntegracionType.ENTRADAS_PT, entradas);
			map.put(IntegracionType.PRODUCTOS, productos);
			map.put(IntegracionType.LOCACIONES, locaciones);

			notificacionService.notificar(fechaDesde, fechaHasta, map);
		}

		integracion.setFechaUltimoPull(fechaHasta);

		integracionRepository.save(integracion);
	}

	@Transactional(readOnly = false)
	public void generarReporteConsolidados() {
		Integracion integracion = getIntegracion(IntegracionType.CONSOLIDADOS_LOGS);
		LocalDateTime fechaDesde = integracion.getFechaUltimoPull();
		LocalDateTime fechaHasta = LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);

		List<LogDTO> pedidos = pedidosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> salidas = salidasTiendasService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> ordenes = ordenesProduccionService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> entradas = entradasProductosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> productos = productosService.alertar(fechaDesde, fechaHasta);
		List<LogDTO> locaciones = locacionesService.alertar(fechaDesde, fechaHasta);

		Map<IntegracionType, List<LogDTO>> map = new TreeMap<IntegracionType, List<LogDTO>>();
		map.put(IntegracionType.PEDIDOS, pedidos);
		map.put(IntegracionType.SALIDAS_TIENDA, salidas);
		map.put(IntegracionType.ORDENES_DE_PRODUCCION, ordenes);
		map.put(IntegracionType.ENTRADAS_PT, entradas);
		map.put(IntegracionType.PRODUCTOS, productos);
		map.put(IntegracionType.LOCACIONES, locaciones);

		//notificacionService.notificar(fechaDesde, fechaHasta, map);

		integracion.setFechaUltimoPull(fechaHasta);

		integracionRepository.save(integracion);
		
	}
}