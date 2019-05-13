package com.nsk.cms.netty;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nsk.cms.common.Constants;
import com.nsk.cms.netty.handler.AcceptorIdleStateTrigger;
import com.nsk.cms.netty.handler.AcqConfigurationHandler;
import com.nsk.cms.netty.handler.HeartBeatRespHandler;
import com.nsk.cms.netty.handler.LoginAuthRespHandler;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;

@Component
public class NettyServerHandlers extends ChannelInitializer<SocketChannel>{

	@Autowired
	private AcceptorIdleStateTrigger acceptorIdleStateTrigger;
	
	@Autowired
	private LoginAuthRespHandler loginAuthRespHandler;
	
	@Autowired
	private HeartBeatRespHandler heartBeatRespHandler;
	
	@Autowired
	private AcqConfigurationHandler acqConfigurationHandler;
	
	@Override
	protected void initChannel(SocketChannel sc) throws Exception {
//		sc.pipeline().addLast(new NettyMessageDecoder(1024*1024*5, 4, 4));
//		sc.pipeline().addLast(new NettyMessageEncoder());
		ChannelPipeline pipeline = sc.pipeline();
		pipeline.addLast("idleStateHandler",new IdleStateHandler(Constants.IDLE_TIME, 0, 0, TimeUnit.SECONDS));
		
		
		pipeline.addLast("protobufVarint32FrameDecoder",new ProtobufVarint32FrameDecoder());
		pipeline.addLast("protobufDecoder",new ProtobufDecoder(NettyMessage.getDefaultInstance()));
		pipeline.addLast("protobufVarint32LengthFieldPrepender",new ProtobufVarint32LengthFieldPrepender());
		pipeline.addLast("protobufEncoder",new ProtobufEncoder());

		pipeline.addLast("LoginAuthHandler", this.loginAuthRespHandler);
		pipeline.addLast("HeartBeatHandler", this.heartBeatRespHandler);
		pipeline.addLast("AcqConfigurationHandler", this.acqConfigurationHandler);
		
		pipeline.addLast("idleSatateTrigger", this.acceptorIdleStateTrigger);
 	}
}
