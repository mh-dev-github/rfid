package com.mh.api.seriales.servicios;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mh.api.seriales.dto.SerialDTO;
import com.mh.model.esb.domain.esb.Serial;
import com.mh.model.esb.repo.esb.SerialRepository;

@Service
public class SerialesExportService {
	@Autowired
	SerialRepository serialRepository;

	@Transactional
	public void export(List<SerialDTO> list) {
		LocalDateTime now = LocalDateTime.now();
		List<Serial> seriales = new ArrayList<>();
		for (SerialDTO e : list) {
			// @formatter:off
			seriales.addAll(
				e.getEpcs()
					.stream()
					.map(a -> new Serial(a, e.getEan(),now))
					.collect(Collectors.toList()));
			// @formatter:on
		}

		checkDuplicadosEnLaPeticion(seriales);
		checkDuplicadosEnLaBaseDeDatos(seriales);
		serialRepository.save(seriales);
	}

	private void checkDuplicadosEnLaPeticion(List<Serial> seriales) {
		// @formatter:off
		List<Serial> duplicados = seriales
				.stream()
				.filter(e -> Collections.frequency(seriales, e) > 1)
				.collect(Collectors.toList());
		// @formatter:on

		if (!duplicados.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append("Los siguientes elementos estan duplicados en la petición:");
			for (Serial serial : duplicados) {
				// @formatter:off
				sb
				.append("{")
				.append("serial:")
				.append("\"")
				.append(serial.getSerial())
				.append("\"")
				.append(",")
				.append("sku:")
				.append("\"")
				.append(serial.getSku())
				.append("\"")
				.append("}")
				.append(",");
				// @formatter:on
			}
			throw new RuntimeException(sb.toString());
		}
	}

	private void checkDuplicadosEnLaBaseDeDatos(List<Serial> seriales) {
		boolean duplicados = false;
		StringBuilder sb = new StringBuilder();
		sb.append("Los siguientes elementos estan duplicados en la base de datos de seriales:");
		for (Serial serial : seriales) {
			if (serialRepository.findOneBySerial(serial.getSerial()) != null) {
				// @formatter:off
				sb
				.append("{")
				.append("serial:")
				.append("\"")
				.append(serial.getSerial())
				.append("\"")
				.append(",")
				.append("sku:")
				.append("\"")
				.append(serial.getSku())
				.append("\"")
				.append("}")
				.append(",");
				// @formatter:on
				duplicados = true;
			}
		}
		if (duplicados) {
			throw new RuntimeException(sb.toString());
		}
	}
}
