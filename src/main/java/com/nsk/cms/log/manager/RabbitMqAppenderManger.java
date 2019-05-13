package com.nsk.cms.log.manager;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.db.jdbc.ColumnConfig;
import org.apache.logging.log4j.core.appender.db.jdbc.JdbcAppender;
import org.apache.logging.log4j.core.appender.mom.kafka.KafkaAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.filter.MarkerFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.common.util.DBConnectUtil;
import com.nsk.cms.log.RabbitMqAppender;
import com.nsk.cms.log.SingleFilterBuilder;
import com.nsk.cms.rabbitmq.sender.MessageSender;

@Component
public class RabbitMqAppenderManger {
	
	@Autowired
	private MessageSender messageSender;
	
	@PostConstruct
	public void init(){
		final LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
		final Configuration config = ctx.getConfiguration();
		final Logger interLogger = ctx.getLogger("com.nsk.cms");  //需要写日志到数据库的包名
	    //配置Marker过滤器(标记过滤器)
		MarkerFilter filter = SingleFilterBuilder.builderMarkerFilter("dblog");
		Appender rabbitmqAppender = RabbitMqAppender.createAppender("rabbitmqAppander", filter, null ,messageSender);
		config.addAppender(rabbitmqAppender);
		interLogger.addAppender(rabbitmqAppender);
		rabbitmqAppender.start();
	    ctx.updateLoggers();    
	}
}
