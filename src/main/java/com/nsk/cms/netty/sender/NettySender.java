package com.nsk.cms.netty.sender;

import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;

public interface NettySender {
	
	public boolean sendMessage(String ip, NettyMessage nettyMessage);
	
}
