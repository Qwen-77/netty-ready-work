package com.wen.netty.service;

import com.wen.entity.PersonalProtocolDto;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 * @author: 7wen
 * @Date: 2023-04-21 15:33
 * @description:
 */
public class NettyPersonalProtocolServiceHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        System.out.println("??");
        //如果通过解码器之后传输的数据为对象则进行逻辑
        if (o instanceof PersonalProtocolDto) {
            System.out.println(o.toString());
        }
    }
}
