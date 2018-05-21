package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.OrdenProduccionMessage;

/**
 * Repositorio Mensajes Ordenes de Producción
 * 
 * @author arosorio@gmail.com
 *
 */
public interface OrdenProduccionMessageRepository extends JpaRepository<OrdenProduccionMessage, Long>{

}
