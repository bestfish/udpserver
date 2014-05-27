package com.fish.play.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.fish.play.server.schedule.VarietyStats;

public class DefaultServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
	private static final Log log = LogFactory.getLog(DefaultServerHandler.class);
	private static final String SEPARATOR = ":";
	
	public DefaultServerHandler() {
		log.info("the server handler is ready for receiving data...");
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		String content = msg.content().toString(CharsetUtil.UTF_8);
		int index = content.indexOf(SEPARATOR);
		if (index > 0) {
			String key = content.substring(0, index);
			String value = content.substring(index + 1);
			VarietyStats.incr(key);
			log.info("key:" + key + ", content:" + value);
		}
		
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		log.error("error captured by exceptionCaught()", cause);
	}
	

}
