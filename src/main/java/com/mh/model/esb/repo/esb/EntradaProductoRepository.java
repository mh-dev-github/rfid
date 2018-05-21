package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.EntradaProducto;

/**
 * Repositorio Entradas de Productos Terminado
 * 
 * @author arosorio@gmail.com
 *
 */
public interface EntradaProductoRepository extends JpaRepository<EntradaProducto, String> {

}
