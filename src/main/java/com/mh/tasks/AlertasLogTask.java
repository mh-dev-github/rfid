package com.mh.tasks;

import static com.mh.amqp.RabbitMqConfig.EXCHANGE_NAME;
import static com.mh.amqp.RabbitMqConfig.ROUTING_KEY;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mh.dto.amqp.RequestDTO;
import com.mh.dto.amqp.RequestType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AlertasLogTask {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Scheduled(cron = "${alertas-logs.cron}")
	public void cronReporteAlertas() {
		log.debug("alertas-logs.cron");
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(RequestType.ALERTAS_LOGS));
	}

	@Scheduled(cron = "${consolidados-logs.cron}")
	public void cronReporteConsolidado() {
		log.debug("consolidados-logs.cron");
		rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, new RequestDTO(RequestType.CONSOLIDADOS_LOGS));
	}
}
