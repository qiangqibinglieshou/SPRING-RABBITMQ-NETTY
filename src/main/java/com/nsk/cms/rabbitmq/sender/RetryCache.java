package com.nsk.cms.rabbitmq.sender;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.amqp.core.Message;
import org.springframework.beans.factory.annotation.Autowired;

import com.nsk.cms.common.Constants;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RetryCache {
	
   private MessageSender messageSender;
	
   private boolean stop = false;
    
   private Map<String, MessageWithTime> map = new ConcurrentSkipListMap<>();
    
   private static AtomicLong id = new AtomicLong();
   	
   public void setMessageSender(MessageSender messageSender){
	   this.messageSender = messageSender;
	   startRetry();
   }
   
   public String generateId() {
	   return "" + id.incrementAndGet();
   }
   
   public void add(String id,Object object) {
	   map.put(id, new MessageWithTime(System.currentTimeMillis(), object));
   }
   
   public void del(String id) {
	   map.remove(id);
   }
   
   public Object get(String id) {
	   return map.get(id);
   }
    
   public int count() {
	   return map.size();
   }
   
   private void startRetry() {
       new Thread(() ->{
           while (!stop) {
               try {
                   Thread.sleep(Constants.RETRY_TIME_INTERVAL);
               } catch (InterruptedException e) {
                   e.printStackTrace();
               		}

               long now = System.currentTimeMillis();

               for (Map.Entry<String, MessageWithTime> entry : map.entrySet()) {
            	   MessageWithTime message = entry.getValue();

                   if (null != message) {
                       if (message.getTime() + 3 * Constants.VALID_TIME < now) {
                           log.info("send message failed after 3 min " + message.getMessage());
                           del(entry.getKey());
                       } else if (message.getTime() + Constants.VALID_TIME < now) {
                           DetailRes detailRes = messageSender.send(message.getMessage());

                           if (detailRes.isSuccess()) {
                        	   del(entry.getKey());
                           }
                       }
                    }   
                }
           	}
       }, "c.n.c.Rabbitmq.s.RetryCache.startRetry").start();
   }
   
   @NoArgsConstructor
   @AllArgsConstructor
   @Data
   private static class MessageWithTime {
       long time;
       Object message;
   }
}
