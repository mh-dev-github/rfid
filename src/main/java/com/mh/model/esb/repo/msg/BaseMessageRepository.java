package com.mh.model.esb.repo.msg;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.msg.PedidoMessage;

public interface BaseMessageRepository extends JpaRepository<PedidoMessage, Long>{

}
