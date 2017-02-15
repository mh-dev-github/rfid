package com.mh.model.esb.repo.esb;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mh.model.esb.domain.esb.Serial;

public interface SerialRepository extends JpaRepository<Serial, Long>{
	Serial findOneBySerial(String serial);
}
