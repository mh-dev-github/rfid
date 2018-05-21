package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.Pedido;

/**
 * Repositorio Pedidos
 * 
 * @author arosorio@gmail.com
 *
 */
public interface PedidoRepository extends JpaRepository<Pedido, String>{

}
