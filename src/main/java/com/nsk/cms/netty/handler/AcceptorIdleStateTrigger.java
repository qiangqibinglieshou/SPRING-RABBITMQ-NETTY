package com.nsk.cms.netty.handler;

import java.net.InetSocketAddress;

import org.springframework.stereotype.Component;

import com.nsk.cms.common.Constants;
import com.nsk.cms.netty.NettyServer;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import io.netty.channel.ChannelHandler.Sharable;

@Slf4j
@Component
@Sharable
public class AcceptorIdleStateTrigger extends ChannelInboundHandlerAdapter {  
	
	private int loss_connect_time = 0;
	
    @Override  
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {  
    	if (evt instanceof IdleStateEvent) {
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.READER_IDLE) {
				loss_connect_time++;
				log.error("{}秒没有接收到客户端的信息了", Constants.IDLE_TIME);
				if (loss_connect_time > 2) {
					loss_connect_time = 0;
					log.error("关闭这个不活跃的channel");
					ctx.channel().close();
					ctx.flush();
				}
			}
		} else {
			super.userEventTriggered(ctx, evt);
		}
    }
    
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = address.getAddress().getHostAddress();
		log.error("Somting wrong with {}:{}",ip ,cause.getMessage());
		if(loss_connect_time > 2) {
			loss_connect_time = 0;
			if(NettyServer.containsIp(ip)) {
				
				if(!ctx.channel().isActive()) {
					ctx.channel().close();
					
				}
			}else {
				ctx.channel().close();
			}
			ctx.flush();
		}
	}
}  
