package com.nsk.cms.rabbitmq.impl;

import javax.annotation.Resource;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.rabbitmq.handler.RabbitMqHandler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Component
public class NskRabbitListenerBuilder {
	
	@Resource
	 private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;
	
	@Autowired
	private SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory;
	
	@Autowired
	private RabbitAdmin rabbitAdmin;
	
	private String ipToQueueName(String ip) {
		String QueueName = "NRDC.C2S." + ip;
		return QueueName;
	}
	
	public void registerListenerContainer(String ip) {
		String QueueName = ipToQueueName(ip);
		try {
			this.rabbitAdmin.declareQueue(new Queue(QueueName));
		}catch (Exception e) {
			// TODO: handle exception
			log.info(e.toString());
		}
		if(rabbitListenerEndpointRegistry.getListenerContainer(QueueName) != null) {
			
			startListenerContainers(ip);
			
		}else {
			NskRabbitListenerEndpoint rabbitListenerEndpoint = new NskRabbitListenerEndpoint();
			
			rabbitListenerEndpoint.setId(QueueName);
			rabbitListenerEndpoint.setQueueNames(QueueName);
			rabbitListenerEndpoint.setMessageListener(new NskMessageListenerAdapter(new RabbitMqHandler(ip)));
			this.rabbitListenerEndpointRegistry.registerListenerContainer(rabbitListenerEndpoint, rabbitListenerContainerFactory,true);
		}
	}

	
	public boolean isRunning(String ip) {
		String QueueName = ipToQueueName(ip);
		return rabbitListenerEndpointRegistry.getListenerContainer(QueueName).isRunning();
	}
	
	public void stopListenerContainer(String ip) {
		String QueueName = ipToQueueName(ip);
		MessageListenerContainer listenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(QueueName);
//		rabbitAdmin.deleteQueue(QueueName);
		if(listenerContainer != null) {
			listenerContainer.stop();
//			listenerContainer = null;
			log.info("Container " + QueueName + " stop!");
			return;
		}
	//	RetartListenerContainers();
	}
	
	public void startListenerContainers(String ip) {
		String QueueName = ipToQueueName(ip);
		MessageListenerContainer messageListenerContainer = rabbitListenerEndpointRegistry.getListenerContainer(QueueName);
		messageListenerContainer.start();
	}

}
