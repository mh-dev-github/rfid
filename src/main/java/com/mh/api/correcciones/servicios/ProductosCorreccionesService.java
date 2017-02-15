package com.mh.api.correcciones.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.mh.model.esb.domain.esb.Producto;
import com.mh.model.esb.domain.msg.ProductoMessage;
import com.mh.model.esb.repo.esb.ProductoRepository;
import com.mh.model.esb.repo.msg.ProductoMessageRepository;

@Service
public class ProductosCorreccionesService extends BaseCorreccionesService<Producto, ProductoMessage> {
	@Autowired
	private ProductoMessageRepository messageRepository;

	@Autowired
	private ProductoRepository entityRepository;

	@Override
	protected JpaRepository<ProductoMessage, Long> getMessageRepository() {
		return messageRepository;
	}

	@Override
	protected JpaRepository<Producto, String> getEntityRepository() {
		return entityRepository;
	}

	@Override
	protected ProductoMessage crearMensaje(Producto entidad) {
		ProductoMessage mensaje = new ProductoMessage();

		poblarMessageEntity(mensaje, entidad);

		mensaje.setCompanyPrefix(entidad.getCompanyPrefix());
		mensaje.setName(entidad.getName());
		mensaje.setReference(entidad.getReference());
		mensaje.setEan(entidad.getEan());
		mensaje.setColor(entidad.getColor());
		mensaje.setCodigoColor(entidad.getCodigoColor());
		mensaje.setTalla(entidad.getTalla());
		mensaje.setTipoProducto(entidad.getTipoProducto());
		mensaje.setColeccion(entidad.getColeccion());
		mensaje.setGrupoProducto(entidad.getGrupoProducto());
		mensaje.setSubGrupoProducto(entidad.getSubGrupoProducto());
		mensaje.setFabricante(entidad.getFabricante());
		mensaje.setTemporada(entidad.getTemporada());
		mensaje.setReferencia(entidad.getReferencia());
		mensaje.setModelo(entidad.getModelo());
		mensaje.setGenero(entidad.getGenero());

		return mensaje;
	}

	@Override
	protected ProductoMessage clonarMensaje(ProductoMessage a) {
		return new ProductoMessage(a);
	}
}