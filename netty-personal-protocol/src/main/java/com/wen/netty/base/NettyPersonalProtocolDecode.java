package com.wen.netty.base;

import com.wen.entity.PersonalProtocolDto;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-04-21 14:53
 * @description: 私有化协议 根据业务进行自由定制, 需要包括
 * <p>
 * //请求头（指令 4位
 * // 版本 4位
 * // clientType 4位
 * // 消息解析类型 4位
 * // appId 4位
 * // imei长度 4位
 * // bodylen 4位）+
 *   imei号 + 请求体
 */
public class NettyPersonalProtocolDecode extends MessageToMessageDecoder {
    private final int INIT_NUMBER = 28;

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, Object o, List list) {
        ByteBuf byteBuf = (ByteBuf) o;
        //如果剩余可读数据长度总体小于制定的标识数据长度则表示数据不完成或出现粘包拆包现象
        if (byteBuf.readableBytes() < INIT_NUMBER) {
            return;
        }

        //声明容器
        PersonalProtocolDto personalProtocolDto = new PersonalProtocolDto();

        //开始读取数据

        //指令
        personalProtocolDto.setCommand(byteBuf.readInt());

        //版本
        personalProtocolDto.setVersion(byteBuf.readInt());

        //clientType
        personalProtocolDto.setClientType(byteBuf.readInt());

        //messageType
        personalProtocolDto.setMessageParsingType(byteBuf.readInt());

        //appId
        personalProtocolDto.setAppId(byteBuf.readInt());

        //imeiLen
        int imeiLen = byteBuf.readInt();
        personalProtocolDto.setImeiLen(imeiLen);

        //获取bodyLen
        int bodyLen = byteBuf.readInt();
        personalProtocolDto.setBodyLen(bodyLen);


        if (byteBuf.readableBytes() < imeiLen + bodyLen) {
            //如果剩余可读数据小于imei+body的长度说明出现粘包拆包现象
            //重置上一次成功读取的读下标
            byteBuf.resetReaderIndex();
            //让当前流数据暂时停滞等待数据
            return;
        }

        //读取imei数据
        //设置容器并存储数据
        byte[] byteData = new byte[imeiLen];
        byteBuf.readBytes(byteData);
        personalProtocolDto.setImei(byteData);


        //获取body数据
        //初始化容器并存储数据
        byteData = new byte[bodyLen];
        byteBuf.readBytes(byteData);
        personalProtocolDto.setBodyData(byteData);

        //本次成功后标记读坐标
        byteBuf.markReaderIndex();

        //包装数据传递给下一个handler
        list.add(personalProtocolDto);
    }
}
