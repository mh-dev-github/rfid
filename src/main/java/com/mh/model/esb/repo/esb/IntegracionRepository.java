package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.Integracion;

/**
 * Repositorio Integraciones
 * 
 * @author arosorio@gmail.com
 *
 */

public interface IntegracionRepository extends JpaRepository<Integracion, String>{

	Integracion findOneByCodigo(String string);

}
