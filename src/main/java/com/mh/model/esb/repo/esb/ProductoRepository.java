package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.Producto;

/**
 * Repositorio Producto
 * 
 * @author arosorio@gmail.com
 *
 */
public interface ProductoRepository extends JpaRepository<Producto, String>{

}
