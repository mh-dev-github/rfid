package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.OrdenProduccion;


/**
 * Repositorio Ordenes de Producción
 * 
 * @author arosorio@gmail.com
 *
 */
public interface OrdenProduccionRepository extends JpaRepository<OrdenProduccion, String> {

}
