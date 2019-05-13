package com.nsk.cms.ptotobuf;

import com.google.protobuf.InvalidProtocolBufferException;

import com.google.protobuf.MessageLite;
import com.nsk.cms.ptotobuf.proto.LogProto;
import com.nsk.cms.ptotobuf.proto.ACKproto.ACK;
import com.nsk.cms.ptotobuf.proto.AcquisitionDataProto.AcquisitionData;
import com.nsk.cms.ptotobuf.proto.AcquisitionWaveProto.AcquisitionWave;
import com.nsk.cms.ptotobuf.proto.ExperimentProto.ExperimentMessage;
import com.nsk.cms.ptotobuf.proto.LogProto.LogMessage;
import com.nsk.cms.ptotobuf.proto.SysLogProto.SysLogMessage;

import lombok.extern.slf4j.Slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.support.converter.MessageConversionException;
import org.springframework.amqp.support.converter.MessageConverter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class ProtobufMessageConverter implements MessageConverter {
	
    private static Map<String, MessageLite.Builder> builderMap;
    
    static {
        builderMap = new HashMap<>();
        builderMap.put(ACK.class.getSimpleName(), ACK.newBuilder());
        builderMap.put(AcquisitionData.class.getSimpleName(), AcquisitionData.newBuilder());
        builderMap.put(ExperimentMessage.class.getSimpleName(), ExperimentMessage.newBuilder());
        builderMap.put(LogMessage.class.getSimpleName(), LogMessage.newBuilder());
        builderMap.put(AcquisitionWave.class.getSimpleName(), AcquisitionWave.newBuilder());
        builderMap.put(SysLogMessage.class.getSimpleName(), SysLogMessage.newBuilder());
    }
    
    public  Message toMessage(Object object, MessageProperties messageProperties) throws MessageConversionException {
    	String messageType = object.getClass().getSimpleName();
        log.info("Convent " + messageType + "!");
//        System.out.println("ACK!");
        if (!builderMap.containsKey(messageType)) {
            throw new MessageConversionException("not support message type:" + messageType);
        }
        messageProperties.setHeader("messageType",  messageType);
//        messageProperties.setMessageId(String.valueOf(reteryCache.generateId()));
//        if(messageProperties.getTimestamp()!=null)
//        messageProperties.setTimestamp(new Date());
        MessageLite messageLite = (MessageLite)object;
        return new Message(messageLite.toByteArray(), messageProperties);
    }
    
    public Object fromMessage(Message message) throws MessageConversionException {
        String messageType = message.getMessageProperties().getHeaders().get("messageType").toString();
//        logger.info(message.toString());
        if (!builderMap.containsKey(messageType)) {
            throw new MessageConversionException("not support message type:" + messageType);
        }
        try {
            MessageLite.Builder builder = builderMap.get(messageType).clear();
            builder = builder.mergeFrom(message.getBody());
            return builder.build();
        } catch (InvalidProtocolBufferException e) {
            throw new MessageConversionException("deserialize message error", e);
        }
    }
}
