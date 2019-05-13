package com.nsk.cms.rabbitmq.sender;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.common.Constants;
import com.nsk.cms.ptotobuf.ProtobufMessageConverter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NskMessageSender implements MessageSender {

	private RabbitTemplate rabbitTemplate;

	private RetryCache retryCache;
	
	private RabbitAdmin rabbitAdmin;
	
	@Autowired
	public NskMessageSender(RabbitTemplate rabbitTemplate, RetryCache retryCache, RabbitAdmin rabbitAdmin) {
		this.retryCache = retryCache;
		this.rabbitTemplate = rabbitTemplate;
		this.rabbitAdmin = rabbitAdmin;
		retryCache.setMessageSender(this);
		
		this.rabbitTemplate.setRoutingKey("NSK.S2W");
		this.rabbitTemplate.setMessageConverter(new ProtobufMessageConverter());
		this.rabbitTemplate.setMandatory(true);  
		this.rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                log.info("send message failed: " + cause + correlationData.toString());
            } else {
//            	 log.info(String.valueOf(retryCache.count()));
            	 try {
            		 retryCache.del(correlationData.getId());
            	 	}
                catch(Exception e){
                	}
                }
//            log.info(String.valueOf(retryCache.count()));
            });
		this.rabbitTemplate.setReturnCallback((message, replyCode, replyText, tmpExchange, tmpRoutingKey) -> {
    	  new Thread(()->{
    		  try {
                  Thread.sleep(Constants.ONE_SECOND);
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
        log.info("send message failed: " + replyCode + " " + replyText);
        if(replyCode == 312) {
      	  this.rabbitAdmin.declareQueue(new Queue(tmpRoutingKey));
        }
        send(message);
          }, "c.n.c.r.s.NskMessageSender.ReturnCallback").start();
    	});
	}
	
	@Override
	public DetailRes send(Object object) {
		// TODO Auto-generated method stub
		try {
			String id = retryCache.generateId();
	        retryCache.add(id, object);
	        rabbitTemplate.correlationConvertAndSend(object, new CorrelationData(id));
		}catch (Exception e) {
			return(new DetailRes(false,""));
		}	
		return new DetailRes(true,"");
	}
}
