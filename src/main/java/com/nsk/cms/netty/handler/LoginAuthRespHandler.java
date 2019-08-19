package com.nsk.cms.netty.handler;

import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.manager.CmsManager;
import com.nsk.cms.log.MarkersSet;
import com.nsk.cms.netty.NettyServer;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.Header;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.LOGIN_RESP_Body;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.Header.MessageType;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.LOGIN_RESP_Body.ResponseType;
import com.nsk.cms.rabbitmq.impl.NskRabbitListenerBuilder;
import com.rabbitmq.http.client.domain.ConnectionInfo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
@Component
@Sharable
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
	/**
	 * 考虑到安全，链路的建立需要通过基于IP地址或者号段的黑白名单安全认证机制，本例中，多个IP通过逗号隔开
	 */
	@Autowired
	private CmsManager cmsManager;
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
    	String ip = address.getAddress().getHostAddress();
	    if(NettyServer.containsIp(ip)) {
	    	if(cmsManager.deviceConnectAction(ip)) {
		    	NettyServer.removeNodeCheck(ip);
		    	log.error(MarkersSet.DB,"CLIENT:{} 离线!",ip);
	    	}
	    }//最后去掉else这里的处理。直接忽略
	    else{
	    	log.info("connectionInfo为空!");
	    }
	    ctx.close();
	    ctx.flush();
	}

	/**
	 * Calls {@link ChannelHandlerContext#fireChannelRead(Object)} to forward to
	 * the next {@link ChannelHandler} in the {@link ChannelPipeline}.
	 * 
	 * Sub-classes may override this method to change behavior.
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		NettyMessage message = (NettyMessage) msg;

		InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
		String ip = address.getAddress().getHostAddress();
		// 如果是握手请求消息，处理，其它消息透传
		if (message.getHeader() != null && message.getHeader().getMessageType() == MessageType.LOGIN_REQ) {
			log.info("{}",message.getHeader().getMessageType());
			NettyMessage loginResp = null;
			// 重复登陆，拒绝
			boolean isOK = true;
			if (NettyServer.containsIp(ip)) {
				if(NettyServer.getNodeCheckChannel(ip).isActive()) {
					log.error("重复登录,拒绝请求!");
					isOK = false;
				}else {
					log.info("old");
//					NettyServer.getNodeCheckChannel(ip).close();
					NettyServer.putNodeCheck(ip, ctx.channel());
					cmsManager.deviceConnectAction(ip);
					log.info(MarkersSet.DB,"CLIENT:{} 上线了!",ip);
				}
			}else if(isOK) {
				log.info("new");
				NettyServer.putNodeCheck(ip, ctx.channel());
				cmsManager.deviceConnectAction(ip);
				log.info(MarkersSet.DB,"CLIENT:{} 上线了!",ip);
			}
			loginResp = isOK ? NettyMessageBuilder.buildLoginResponse(ResponseType.SUCCESS, ip) : NettyMessageBuilder.buildLoginResponse(ResponseType.FAIL, ip);	
			ctx.writeAndFlush(loginResp);
		} else  if(NettyServer.containsIp(ip)){
			ctx.fireChannelRead(msg);
		}
	}
}
