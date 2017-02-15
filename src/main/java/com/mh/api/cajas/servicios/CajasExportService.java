package com.mh.api.cajas.servicios;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mh.api.cajas.dto.CajaDTO;
import com.mh.api.cajas.dto.LineaCajaDTO;
import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.repo.esb.LocacionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CajasExportService {
	@Autowired
	LocacionRepository locacionRepository;

	@Value("${output.directorio.cajas}")
	private String DIRECTORIO_SALIDAS;

	public void export(CajaDTO input) {
		
		Path path = null;
		Locacion locacion = locacionRepository.findOne(input.getCodigoBodega());
		
		if (locacion != null) {
			if(locacion.getDirectorioSalidas()== null){
				locacion.setDirectorioSalidas("");
			}
			
			//TODO
			// @formatter:off
//			locacion.setDirectorioSalidas(
//					locacion.getDirectorioSalidas()
//					.replace("\\\\Cobogapp12\\", "\\\\Aff2ce4140yhm\\esb\\")
//					.replace("\\\\MHRD01\\", "\\\\Aff2ce4140yhm\\esb\\"));
			// @formatter:on

			if (!locacion.getDirectorioSalidas().isEmpty()) {
				path = Paths.get(locacion.getDirectorioSalidas());
				if (!Files.isDirectory(path)) {
					path = null;
				}
			}
		}

		if (path == null) {
			path = Paths.get(DIRECTORIO_SALIDAS);
		}

		log.debug("path:{}", path.toString());

		if (!Files.isDirectory(path)) {
			throw new RuntimeException("No existe el directorio de salida " + path.toString());
		}

		// @formatter:off
		String fileName = MessageFormat.format("{0}-{1}-{2}",
				input.getExternalId(),
				input.getCodigoBodega(),
				input.getCodigoCaja());
		// @formatter:on

		Path fileTmp = path.resolve(fileName + ".temporal");
		Path fileTxt = path.resolve(fileName + ".txt");

		try {
			Files.deleteIfExists(fileTmp);
			Files.deleteIfExists(fileTxt);

			try (PrintWriter writer = new PrintWriter(new FileWriter(fileTmp.toFile()))) {
				for (LineaCajaDTO e : input.getLineas()) {
					String linea = MessageFormat.format("{0},{1}", e.getCodigo(), e.getCantidad());
					writer.println(linea);
				}
			}

			fileTmp.toFile().renameTo(fileTxt.toFile());
			Files.deleteIfExists(fileTmp);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
