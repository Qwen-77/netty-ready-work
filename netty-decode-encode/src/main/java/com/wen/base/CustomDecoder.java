package com.wen.base;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.nio.charset.Charset;
import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-04-19 17:14
 * @description:
 */


 /**
  * 引申: 如何解决Tcp流式传输遗留下的问题
  *
  * netty中byteBuf(读索引 , 写索引 , 总长度)
  * api:
  *     Unpool.buffer(10) 创建byteBuf对象 该对象内部包含一个字节数组byte[10]
  *     byteBuf.writeByte(0) 写入数据 更新写索引
  *     byteBuf.getByte(0) 获取数据 注意并不会更新读索引
  *     byteBuf.readByte() 读出数据 更新读索引
  *
  *     byteBuf.readableBytes() 剩下可读字节长度
  *
  *     byteBuf.markReaderIndex() 标记本次读索引
  *     byteBuf.resetReaderIndex() 重置到上一次标记的读索引
  * 底层: 双指针模型 分别为读指针(0,readIndex)和写指针(readIndex,writeIndex) + 未写区域(writeIndex,length)
  *
  *
  * @author 7wen
  * @date 2023-04-19 17:54
  */
public class CustomDecoder extends ByteToMessageDecoder {

    /**
     * 实现解码方法
     * 十分精妙 建议通读 list表示传递给下一个handler的msg是什么
     * @author 7wen
     * @date 2023-04-19 17:18
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        //起到纠正规范和初始化数据容器的作用
        int i = byteBuf.readInt();
        //如果获取的数据包长度大于当前流传输的数据长度 则表示错读数据
        if (i > byteBuf.readableBytes()) {
            //将读指针重置为上一次正确读取数据的节点坐标 如果没有则获取当前读坐标
            byteBuf.resetReaderIndex();
            return;
        }
        //如果通过了上一个if 就算流中数据大于正常数据 但是容器容量只有正确数据那么多 所以只会装那么多 剩下数据阻塞住了流 等待下一次数据的填充 往复循环读取正确数据
        byte[] bytes = new byte[i];
        byteBuf.readBytes(bytes);
        System.out.println(new String(bytes,"utf-8"));
        //如果本次读取数据正确则标记本次的数据读坐标
        byteBuf.markReaderIndex();
    }
}
