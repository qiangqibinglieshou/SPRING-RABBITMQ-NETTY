package com.nsk.cms.netty.sender;

import org.springframework.stereotype.Component;

import com.nsk.cms.netty.NettyServer;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NskNettySender implements NettySender {

	@Override
	public boolean sendMessage(String ip, NettyMessage nettyMessage) {
		try {
			Channel channel=NettyServer.getNodeCheckChannel(ip);
			if(channel.isActive() && channel != null){
				  log.info("Server send {} message to {}", nettyMessage.getHeader().getMessageType(), ip);
			    channel.writeAndFlush(nettyMessage);
			}else{
			    return false;//不在线
			}
			return true;
		}
		catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

}
