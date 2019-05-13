package com.nsk.cms.rabbitmq.impl;

import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerEndpoint;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

public class NskRabbitListenerEndpoint extends SimpleRabbitListenerEndpoint {
	
	@Override
	protected MessageListener createMessageListener(MessageListenerContainer container) {
		MessageListener m = getMessageListener();
		if(m instanceof MessageListenerAdapter)
			((MessageListenerAdapter)m).setMessageConverter(container.getMessageConverter());
		return m;
	}
	
}
