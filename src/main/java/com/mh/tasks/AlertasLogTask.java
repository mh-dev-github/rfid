package com.mh.tasks;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.mh.amqp.RabbitMqConfig;
import com.mh.amqp.dto.RequestDTO;
import com.mh.amqp.dto.RequestType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AlertasLogTask {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Scheduled(cron = "${alertas-logs.cron}")
	public void cronReporteAlertas() {
		log.debug("alertas-logs.cron");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.ALERTAS_LOGS));
	}

	@Scheduled(cron = "${consolidados-logs.cron}")
	public void cronReporteConsolidado() {
		log.debug("consolidados-logs.cron");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY,
				new RequestDTO(RequestType.CONSOLIDADOS_LOGS));
	}
}