package com.nsk.cms.rabbitmq.sender;

import org.springframework.amqp.core.Message;

public interface MessageSender {
	
	DetailRes send(Object object);
}
