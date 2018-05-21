package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.LocacionMessage;

/**
 * Repositorio Mensajes Locaciones
 * 
 * @author arosorio@gmail.com
 *
 */
public interface LocacionMessageRepository extends JpaRepository<LocacionMessage, Long>{

}
