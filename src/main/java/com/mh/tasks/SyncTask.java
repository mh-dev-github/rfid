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
public class SyncTask {
	@Autowired
	private RabbitTemplate rabbitTemplate;

	@Scheduled(cron =  "${sync.cron}")
	public void cron() {
		log.debug("sync.cron");
		rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_KEY, new RequestDTO(RequestType.SYNC_ALL));
	}
}
