package com.nsk.cms.netty.handler;

import org.springframework.stereotype.Component;

import com.nsk.cms.ptotobuf.proto.NettyMessageProto.Header;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.LOGIN_RESP_Body;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.Header.MessageType;

@Component
public class NettyMessageBuilder {

	public static NettyMessage buildLoginResponse(com.nsk.cms.ptotobuf.proto.NettyMessageProto.LOGIN_RESP_Body.ResponseType result, String ip) {
		Header header =  Header.newBuilder().setMessageType(MessageType.LOGIN_RESP).build();
		LOGIN_RESP_Body lBody = LOGIN_RESP_Body.newBuilder().setResponseType(result).setIp(ip).build();
		NettyMessage message = NettyMessage.newBuilder()
				.setHeader(header)
				.setLoginResponseBody(lBody)
				.build();
		return message;
	}
	
	public static NettyMessage buildHeatBeatResponse () {
		Header header = Header.newBuilder().setMessageType(MessageType.HEARTBEAT_RESP).build();
		NettyMessage message = NettyMessage.newBuilder().setHeader(header).build();
		return message;
	}
}
