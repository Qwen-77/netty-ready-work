package com.wen.service;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.HashSet;
import java.util.Set;


/**
 * @author: 7wen
 * @Date: 2023-04-19 15:25
 * @description: 编码器和解码器
 * netty只默认接收ByteBuf类型参数,如果没有编码器和解码器则netty无法处理相应的字符串信息
 * 1.使用官方的编码器和解码器
 *  (1) 在netty初始化方法中加入
 *       Charset gbk = Charset.forName("gbk");
 *       ch.pipeline().addLast("decoder", new StringDecoder(gbk));
 *       ch.pipeline().addLast("encoder", new StringEncoder(gbk));
 *  (2) 编码器 解码器点开源码 其实本质上就是一个handler 继承了 ChannelInboundHandlerAdapter 类
 *
 * 2.TCP的粘包拆包 流式传输 整体会保证信息齐全但是每次传输不一定按照想要的进行传输
 *   注意 需要将该解码器放在业务以及字符串解码器前
 *  (1) 固定长度解码器 new FixedLengthFrameDecoder(7)
 *  (2) 添加终止符解码器 new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer("_".getBytes()));
 *  以上两种方案都不太好,因为发送消息是动态且不固定的,这样较容易导致消息读取问题
 *  (3) 自定义协议来解决粘包拆包
 *
 * 3.自定义协议
 *  在数据包前添加几位为数据包的长度,每次获取消息的时候先读固定的几位长度,获取到数据包的长度后声明长度变量容器然后再取数据
 *      这样可以保证每次读取的数据都是可控完整且是我们想要的
 *  (1)如何设计?
 *      例如传输12345 可以设置一个int类型数字表示长度 因为int类型在内存中占用4个字节即表示32位 足够表示长度了
 *  读取的时候需要用byte进行读取 即读前4个字节为数据包的长度 后面数据即为数据包内容
 *  (2)如何落地?
 *      见实现代码
 *  (3)注意事项
 *      自定义协议注意编码解码器是否需要去掉
 */
public class NettySimpleChat extends ChannelInboundHandlerAdapter {
    static Set<Channel> userChannel = new HashSet<Channel>();

    /**
     * 用户连接服务器
     *
     * @author 7wen
     * @date 2023-04-19 15:46
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //通知他人该用户已上线
        System.out.println("----连接服务器---");
        for (Channel channel : userChannel) {
            channel.writeAndFlush(ctx.channel().remoteAddress() + "上线了");
        }
        userChannel.add(ctx.channel());
        super.channelActive(ctx);
    }


    /**
     * 处理读逻辑
     *
     * @author 7wen
     * @date 2023-04-19 15:46
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //使用解码器前出现异常的代码
//        ByteBuf byteBuffer = (ByteBuf) msg;
        //使用解码器后转换的代码
        String strMesg = (String) msg;
        //获取当前发生读事件的用户管道
        Channel channel = ctx.channel();
        String userType;
        for (Channel allChannel : userChannel) {
            if (channel == allChannel) {
                //如果是自己则发送给自己
                userType = "[自己] ";
            } else {
                //如果是别人则发送给别人
                userType = "[别人] ";
            }
            allChannel.writeAndFlush(userType + channel.remoteAddress() + " :" + strMesg);
        }
        System.out.println("收到数据:" + msg);
        super.channelRead(ctx, msg);
    }


    /**
     * 客户端连接不活跃时的方法
     *
     * @author 7wen
     * @date 2023-04-19 15:47
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        for (Channel c : userChannel) {
            c.writeAndFlush(ctx.channel().remoteAddress() + "已下线");
        }
        userChannel.remove(ctx.channel());
        System.out.println(ctx.channel().remoteAddress() + "已退出连接");
        super.channelInactive(ctx);
    }
}
