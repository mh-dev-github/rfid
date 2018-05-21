package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.ProductoMessage;

/**
 * Repositorio Mensajes Productos
 * 
 * @author arosorio@gmail.com
 *
 */
public interface ProductoMessageRepository extends JpaRepository<ProductoMessage, Long> {

}
