package com.wen.base;

import com.wen.entity.FileDto;
import com.wen.entityEnum.FileUploadEnum;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author: 7wen
 * @Date: 2023-04-20 15:16
 * @description:
 */
public class NettyUploadFileDecode extends ByteToMessageDecoder {
    private final int CUSTOM_LENGTH = 8;

    /**
     * 上传文件的解码器需要有8个字节来存储指令command以及文件名称
     * io数据流的格式为 4字节int数据command + 4字节int数据文件名长度 + 根据文件名长度截取数据包相应长度的数据用作文件名 + 4字节int数据文件内容长度 + 根据内容长度byte[]读取文件内容
     *
     * @author 7wen
     * @date 2023-04-20 15:17
     */
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < CUSTOM_LENGTH) {
            return;
        }
        FileDto fileDto = new FileDto();

        //1.读取指令
        int command = byteBuf.readInt();
        //存入到vo中 以便handler处理
        fileDto.setCommand(command);

        //2.读取文件名长度
        int fileNameLen = byteBuf.readInt();

        //3.根据文件名长度读取文件名数据
        if (byteBuf.readableBytes() < fileNameLen) {
            //如果剩余可读字节长度小于文件名长度 说明数据有误或出现粘包拆包,重置坐标
            byteBuf.resetReaderIndex();
            return;
        }

        //开始读取文件名数据
        //声明容器
        byte[] fileNameData = new byte[fileNameLen];
        //容器装填完毕
        byteBuf.readBytes(fileNameData);
        String fileNameDataStr = new String(fileNameData, "utf-8");
        //写入到vo中
        fileDto.setFileName(fileNameDataStr);
        System.out.println("-------------文件名称读取完毕");

        if (command == FileUploadEnum.Write_File.getCode()) {
            //如果请求指令是写入文件 则继续获取后面的内容 4位int数据文件内容长度 + 根据长度获取文件内容数据
            int fileDataLen = byteBuf.readInt();
            if (byteBuf.readableBytes() < fileDataLen) {
                //如果剩余可读数据长度小于制定的文件数据长度则表示数据丢失或者发生粘包拆包
                byteBuf.resetReaderIndex();
                return;
            }
            //如果数据校验后正常则开始读取文件内容
            //声明容器
            byte[] fileData = new byte[fileDataLen];
            //填装完毕
            byteBuf.readBytes(fileData);
            //写入到vo中
            fileDto.setBytes(fileData);
            System.out.println("-------------文件数据读取完毕");
        }

        //标记本次成功读取文件的读指针下标
        byteBuf.markReaderIndex();

        //传递给下一个handler
        list.add(fileDto);
    }
}
