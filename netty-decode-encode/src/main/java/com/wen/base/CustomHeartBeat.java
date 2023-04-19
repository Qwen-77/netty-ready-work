package com.wen.base;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @author: 7wen
 * @Date: 2023-04-19 22:40
 * @description:
 *
 * 当维护一个长连接的时候需要心跳机制维护客户端与服务器之间的连接
 *
 * 官方提供的心跳机制:
 *  new IdleStateHandler(2, 0, 0) //var1 读超时 var2 写超时 var3 全超时
 *
 * 当心跳机制检测到了相应的事件会传递到实现了userEventTriggered的方法中
 *
 * 底层实现:
 * 1.官方的心跳机制派生一个task任务去读取下一个handler中的userEventTriggered实现的逻辑;
 * 2.开始读事件超时判定:
 *  超时时间 初始化为自定义的超时时间 根据当前时间与最后一次读事件的时间动态变动
 *  超时时间 -=当前时间 - 最后一次读事件的时间(在客户端最后一次发送消息的时候会进行记录) 例如2 -= 5 - 2 = -1
 *  源码:
 *  如果超时时间<=0 则执行task任务读取下一个handler逻辑
 *  如果超时时间>0 则继续执行当前方法
 *
 *  理解:
 *  如果当前时间距离最后一次读事件时间越远则超时时间会越小 表示越有可能超时 结果为->执行task
 *  如果当前时间距离最后一次读事件时间越近则超时时间会越大 表示客户端发送消息越频繁 结果为->不执行task 重新执行本次run方法 也就不会创建新的读事件
 *
 *   (ps.只要看到netty的方法带有fire就表示要调用下一个handler的xxx方法)
 *
 *   当前所有主流的注册中心都是使用类似的方法检查服务是否在线 不同的是可能使用的是http或者自定rpc进行通信 方法都很类似
 */
public class CustomHeartBeat extends ChannelInboundHandlerAdapter {
    int readTimeOut = 0;
    /**
     * 处理心跳机制下的逻辑
     * @author 7wen
     * @date 2023-04-19 23:31
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt){
        IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
        if (idleStateEvent.state() == IdleState.READER_IDLE) {
            readTimeOut++;
        }
        //客户端读事件超过3次没有响应则关闭客户端
        if (readTimeOut >= 3) {
            System.out.println("客户端没有输入事件达3次,关闭客户端连接");
            ctx.close();
        }
    }
}
