package com.nsk.cms.rabbitmq.conf;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.nsk.cms.ptotobuf.ProtobufMessageConverter;
import com.nsk.cms.rabbitmq.sender.MessageSender;
import com.nsk.cms.rabbitmq.sender.NskMessageSender;
import com.nsk.cms.rabbitmq.sender.RetryCache;
@Configuration
@EnableAutoConfiguration
public class RabbitConfig {
	/**
     * 设置Admin, 用于自动创建exchange, queue
     * @param connectionFactory
     * @return
     */
    @Bean
    RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
    /**
     * 这里主要是用于创建Queue， 如果选择手动创建的话，可以省略
     * @param rabbitAdmin
     * @return
    **/
//    @Bean
//    Queue C2S(RabbitAdmin rabbitAdmin) {
//        Queue queue = new Queue("NSK.C2S." + LocalIP.getLocalIP(), true);
//        rabbitAdmin.declareQueue(queue);
//        return queue;
//    }
    
//  @Bean
//  Queue test(RabbitAdmin rabbitAdmin) {
//      Queue queue = new Queue("test", true);
//      rabbitAdmin.declareQueue(queue);
//      return queue;
//  }
//
//    @Bean
//    Queue C2S(RabbitAdmin rabbitAdmin) {
//        Queue queue = new Queue("NSK.C2S");
//        rabbitAdmin.declareQueue(queue);
//        return queue;
//    }
//    
//    @Bean
//    Queue C2SIP(RabbitAdmin rabbitAdmin) {
//        Queue queue = new Queue("NSK.C2S.test");
//        rabbitAdmin.declareQueue(queue);
//        return queue;
//    }
//    /**
//     * 这里主要是用于创建exchange topic， 如果选择手动创建的话，可以省略
//     * @param rabbitAdmin
//     * @return
//    **/
//    @Bean
//    TopicExchange exchange(RabbitAdmin rabbitAdmin) {
//        TopicExchange topicExchange = new TopicExchange("dispatch-exchange");
//        rabbitAdmin.declareExchange(topicExchange);
//        return topicExchange;
//    }
//    /** 
//    * 将queue和具体的exchange关联起来
//    */
//    @Bean
//    Binding bindingExchangeDispatch(Queue queueDispatch, TopicExchange exchange,RabbitAdmin rabbitAdmin) {
//        Binding binding = BindingBuilder.bind(queueDispatch).to(exchange).with("dispatch-event");
//        rabbitAdmin.declareBinding(binding);
//        return binding;
//    }
    /**
     * 配置消息发送MessageConverter
     * @param rabbitTemplate
     * @return
     */
    @Bean
    public ProtobufMessageConverter protobufMessageConverter() {
        return new ProtobufMessageConverter();
    }
    
    /**
     * 确认消息到达rabbitmq
     * @return ConfirmCallBackListener
     */
//    @Bean
//    public ConfirmCallBackListener confirmCallBackListener() {
//    	return new ConfirmCallBackListener();
//    }
//    
//    /**
//     * 确认消息从rabbitmq的exchange发送到queue
//     * @return ConfirmCallBackListener
//     */
//    @Bean
//    public ReturnCallBackListener returnCallBackListener() {
//    	return new ReturnCallBackListener();
//    }
//    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public RabbitTemplate rabbitMessageTemplate(ConnectionFactory connectionFactory) {
    	  RabbitTemplate nskRabbitTemplate = new RabbitTemplate(connectionFactory);
        return nskRabbitTemplate;
    }
    /**
     * 配置接收消息的MessageConverter
     * @param connectionFactory
     * @return
     */
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(protobufMessageConverter());
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        factory.setConcurrentConsumers(5);
        return factory;
    }
    
    
//    @Bean
//    public NskMessageSender nskMessageSender() {
//    	return new NskMessageSender();
//    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
    public RetryCache reteryCache() {
        return new RetryCache();
    }
}
