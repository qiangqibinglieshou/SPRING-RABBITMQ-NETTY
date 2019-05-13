package com.nsk.cms.netty;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import com.nsk.cms.common.Constants;
import com.nsk.cms.netty.handler.AcceptorIdleStateTrigger;
import com.nsk.cms.netty.handler.AcqConfigurationHandler;
import com.nsk.cms.netty.handler.HeartBeatRespHandler;
import com.nsk.cms.netty.handler.LoginAuthRespHandler;
import com.nsk.cms.netty.sender.NettySender;
import com.nsk.cms.ptotobuf.proto.NettyMessageProto.NettyMessage;
import com.nsk.cms.rabbitmq.impl.NskRabbitListenerBuilder;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NettyServer{
	
	private static Map<String, Channel> nodeCheck = new ConcurrentHashMap<String, Channel>();

	@Autowired
	private NettyServerHandlers nettyServerHandlers;
	
	@PostConstruct
	public void init(){
			//ONE:
			//1 用于接受客户端连接的线程工作组
			EventLoopGroup boss = new NioEventLoopGroup();
			//2 用于对接受客户端连接读写操作的线程工作组
			EventLoopGroup work = new NioEventLoopGroup();
			
			//TWO:
			//3 辅助类。用于帮助我们创建NETTY服务
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, work)	//绑定两个工作线程组
			 .channel(NioServerSocketChannel.class)	//设置NIO的模式
			 .option(ChannelOption.SO_BACKLOG, 1024*1024)	//设置TCP缓冲区
			 .option(ChannelOption.TCP_NODELAY, true)
			 //.option(ChannelOption.SO_SNDBUF, 32*1024)	// 设置发送数据的缓存大小
	//		 .option(ChannelOption.SO_RCVBUF, 32*1024)	// 设置接受数据的缓存大小
			 .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE)	// 设置保持连接
	//		 .childOption(ChannelOption.SO_SNDBUF, 32*1024)
			 // 初始化绑定服务通道
			 .childHandler(nettyServerHandlers);
	//		b.bind(Constants.REMOTEIP,Constants.PORT);
	//		b.bind(Constants.PORT);
			
			try {
				ChannelFuture cf = b.bind(Constants.PORT).sync();
			} catch (InterruptedException interruptedException) {
				// TODO Auto-generated catch block
				interruptedException.printStackTrace();
			}catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		log.info("Netty server start ok at"+ (":" + Constants.PORT));
	}

	public static Channel getNodeCheckChannel(String ip) {
		return NettyServer.nodeCheck.get(ip);
	}
	
	public static void putNodeCheck(String ip, Channel channel) {
		NettyServer.nodeCheck.put(ip, channel);
	}
	
	public static void removeNodeCheck(String ip) {
		NettyServer.nodeCheck.remove(ip);
	}
	
	public static boolean containsIp(String ip) {
		return NettyServer.nodeCheck.containsKey(ip);
	}

}
















