package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.SalidaTiendaMessage;

/**
 * Repositorio Mensajes Salidas de Tiendas
 * 
 * @author arosorio@gmail.com
 *
 */
public interface SalidaTiendaMessageRepository extends JpaRepository<SalidaTiendaMessage, Long> {

}
