package com.nsk.cms.log;

import java.io.Serializable;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.ptotobuf.proto.SysLogProto.SysLogMessage;
import com.nsk.cms.rabbitmq.sender.MessageSender;
import com.nsk.cms.rabbitmq.sender.RabbitMqMessageBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Plugin(name = "Stub", category = "Core", elementType = "appender", printObject = true)
public final class RabbitMqAppender extends AbstractAppender {
	
	@Autowired
	private MessageSender messageSender;
	/**  
	   *构造函数 可自定义参数 这里直接传入一个常量并输出
	  *
	 */ 
	    protected RabbitMqAppender(String name, Filter filter, Layout<? extends Serializable> layout, MessageSender messageSender) {
	        super(name, filter, layout);
	        this.messageSender = messageSender;
	    }

	    @Override
	    public void append(LogEvent event) {
	         if (event != null && event.getMessage() != null) {
	        	 
		        	 SysLogMessage sysLogMessage = RabbitMqMessageBuilder.buildSysLogMessage(event.getSource().getClassName(),
		        			 event.getLevel().name(), 
		        			 event.getMessage().getFormattedMessage());
//		        	 System.out.println(event.getMessage().getFormattedMessage());
//		        	 System.out.println(event.getLevel().name());
//		        	 System.out.println(event.getSource().getClassName());
		             messageSender.send(sysLogMessage);
	          }
	    }
	    
	    /**  接收配置文件中的参数 
	     * 
	     * @PluginAttribute 字面意思都知道，是xml节点的attribute值，如<oKong name="oKong"></oKong> 这里的name 就是 attribute
	     * @PluginElement：表示xml子节点的元素，
	     * 如
	     *     <oKong name="oKong">
	     *         <PatternLayout pattern="[%d{HH:mm:ss:SSS}] [%p] - %l - %m%n"/>
	     *     </oKong>
	     *   其中，PatternLayout就是 的 Layout，其实就是{@link Layout}的实现类。
	     */ 
	    @PluginFactory
	    public static RabbitMqAppender createAppender(
	            @PluginAttribute("name") String name,
	            @PluginElement("Filter") final Filter filter, 
	            @PluginElement("Layout") Layout<? extends Serializable> layout,
	            @PluginElement("messageSender") MessageSender messager) {
	        
	        if (name == null) {
	            LOGGER.error("no name defined in conf."); 
	            return null; 
	        } 
	        //默认使用 PatternLayout
	        if (layout == null) { 
	            layout = PatternLayout.createDefaultLayout(); 
	        } 
	        
	        return new RabbitMqAppender(name, filter, layout, messager);
	    }
	    
	    @Override
	    public void start() {
//	        System.out.println("log4j2-rabbitmq-start方法被调用");
	        super.start();
	    }
	    
	    @Override
	    public void stop() {
	    	System.out.println("log4j2-rabbitmq-stop方法被调用");
	        super.stop();
	    }
}
