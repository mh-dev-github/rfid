package com.mh.servicios.inventarios;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Locacion;
import com.mh.model.esb.repo.esb.LocacionRepository;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * Servicio encargado de exportar los datos de inventario en el directorio asociado a la locación del mismo
 * 
 * @author arosorio@gmail.com
 *
 */
@Service
@Slf4j
public class InventariosExportService {
	@Autowired
	LocacionRepository locacionRepository;

	@Value("${output.directorio.inventarios}")
	private String DIRECTORIO_SALIDAS;

	public void export(InventarioDTO input) {

		Path path = null;
		List<Locacion> list = locacionRepository.findByName(input.getCodigoBodega());
		Locacion locacion = (list.size() > 0 ? list.get(0) : null);

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
		String fileName = MessageFormat.format("{0}-{1}",
				input.getCodigoInventario(),
				input.getCodigoBodega());
		// @formatter:on

		Path fileTmp = path.resolve(fileName + ".temporal");
		Path fileTxt = path.resolve(fileName + ".txt");

		try {
			Files.deleteIfExists(fileTmp);
			Files.deleteIfExists(fileTxt);

			try (PrintWriter writer = new PrintWriter(new FileWriter(fileTmp.toFile()))) {
				for (LineaInventarioDTO e : input.getLineas()) {
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
	
	@Data
	public class InventariosDTO {
		private InventarioDTO inventario;
	}

	@Data
	public class InventarioDTO {
		private String codigoInventario;
		private String codigoBodega;
		private List<LineaInventarioDTO> lineas = new ArrayList<>();
	}

	@Data
	public class LineaInventarioDTO {
		private String codigo;
		private int cantidad;
	}
}
