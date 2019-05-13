package com.nsk.cms.rabbitmq.impl;

import java.io.IOException;

import org.springframework.amqp.AmqpIllegalStateException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.listener.exception.ListenerExecutionFailedException;

import com.nsk.cms.rabbitmq.handler.RabbitMqHandler;
import com.rabbitmq.client.Channel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NskMessageListenerAdapter extends MessageListenerAdapter {
	
	public NskMessageListenerAdapter(RabbitMqHandler ackHandler) {
		// TODO Auto-generated constructor stub
		super(ackHandler);
	}

	@Override
	public void onMessage(Message message, Channel channel) throws Exception {
		super.onMessage(message, channel);
		try {
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.info("{}",e.getMessage());
			try {
				channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
