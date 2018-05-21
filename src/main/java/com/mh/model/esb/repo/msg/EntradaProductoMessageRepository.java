package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.EntradaProductoMessage;

/**
 * Repositorio Mensajes Entradas de Productos Terminado
 * 
 * @author arosorio@gmail.com
 *
 */
public interface EntradaProductoMessageRepository extends JpaRepository<EntradaProductoMessage, Long>{

}
