package com.nsk.cms.rabbitmq.sender;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.objenesis.instantiator.basic.NewInstanceInstantiator;

import com.nsk.cms.ptotobuf.proto.LogProto;
import com.nsk.cms.ptotobuf.proto.SysLogProto;
import com.nsk.cms.ptotobuf.proto.LogProto.LogMessage;

public class RabbitMqMessageBuilder {

	public static LogProto.LogMessage buildLogMessage(int logId) {
		LogProto.LogMessage.Builder logBuilder = LogMessage.newBuilder().setId(logId);
				return logBuilder.build();
	}
	
	public static SysLogProto.SysLogMessage buildSysLogMessage(String location, String level, String message) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SysLogProto.SysLogMessage.Builder sysLogBuilder = SysLogProto.SysLogMessage.newBuilder()
				.setLevel(level)
				.setLocation(location)
				.setNote(message)
				.setTime(sdf.format(new Date()));
				return sysLogBuilder.build();
	}
}
