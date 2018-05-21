package com.mh.model.esb.repo.esb;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.Locacion;

/**
 * Repositorio Locaciones
 * 
 * @author arosorio@gmail.com
 *
 */
public interface LocacionRepository extends JpaRepository<Locacion, String> {
	List<Locacion> findByName(String name);
}
