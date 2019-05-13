package com.nsk.cms.netty.handler;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.log.MarkersSet;
import com.nsk.cms.manager.CmsManager;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;
import com.nsk.cms.rabbitmq.impl.NskMessageListenerAdapter;
import com.nsk.cms.rabbitmq.impl.NskRabbitListenerBuilder;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.ACQUISITION_REQ_Body.RequestType;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.CONFIGURATION_RESP_Body.ResponseType;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.Header.MessageType;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.netty.channel.ChannelHandler.Sharable;

@Slf4j
@AllArgsConstructor
@Component
@Sharable
public class AcqConfigurationHandler extends ChannelInboundHandlerAdapter {

	@Autowired
	private CmsManager cmsManager;
	
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = address.getAddress().getHostAddress();
		if (message.getHeader() != null 
				&& message.getHeader().getMessageType() == MessageType.CONFIGURATION_RESP
				&& message.getConfigurationResponseBody() != null) {
			if(message.getConfigurationResponseBody().getResponseType() == ResponseType.SUCCESS) {
					cmsManager.deviceAcquisitionStartAction(ip);
			}else {
				log.error(MarkersSet.DB, "CLIENT:" + ip + " 配置失败!");
			}
		}else {
			ctx.fireChannelRead(msg);
		}
	}
}
