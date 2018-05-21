package com.mh.tasks;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.mh.amqp.RabbitMqConfig.EXCHANGE_NAME;
import static com.mh.amqp.RabbitMqConfig.ROUTING_KEY;
import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.amqp.RequestType;

import lombok.extern.slf4j.Slf4j;


/**
 * 
 * Componente encargado de ejecutar por demanda todos los flujos de integración. Requerido para resolver incidencias.
 * 
 * @author arosorio@gmail.com
 *
 */
@Component
@Slf4j
public class SyncTask {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Scheduled(cron =  "${sync.cron}")
	public void cron() {
		log.debug("sync.cron");
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(RequestType.SYNC_ALL));
	}
}
