package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.PedidoMessage;

/**
 * Repositorio Mensajes Pedidos
 * 
 * @author arosorio@gmail.com
 *
 */
public interface PedidoMessageRepository extends JpaRepository<PedidoMessage, Long> {

}
