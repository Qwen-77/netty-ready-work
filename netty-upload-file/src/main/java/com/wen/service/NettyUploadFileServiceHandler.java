package com.wen.service;

import com.wen.entity.FileDto;
import com.wen.entityEnum.FileUploadEnum;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.*;

/**
 * @author: 7wen
 * @Date: 2023-04-20 15:10
 * @description: 完成文件上传的操作
 */
public class NettyUploadFileServiceHandler extends ChannelInboundHandlerAdapter {
    private int mark = 0;
    private final String FILE_PREFIX = "W://";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        mark++;
        if (msg instanceof FileDto) {
            FileDto file = (FileDto) msg;
            //将msg转换为FileDto
            if (file.getCommand().equals(FileUploadEnum.Create_File.getCode())) {
                //如果为创建文件则:
                File createFile = new File("W://" + file.getFileName());
                if (!createFile.exists()) {
                    createFile.createNewFile();
                }
            } else if (file.getCommand().equals(FileUploadEnum.Write_File.getCode())) {
                //如果为写入文件
                saveFile(FILE_PREFIX + file.getFileName(), file.getBytes());
            }
        }
        super.channelRead(ctx, msg);
    }

    private void saveFile(String fileName, byte[] bytes) {
        System.out.println(mark);
        OutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            File file = new File(fileName);
            //声明string连接器
//            StringBuilder sb = new StringBuilder();
            //声明字节流
//            inputStream = new FileInputStream(file);
            //声明容器
//            byte[] fileData = new byte[1024];
            //声明计数器
//            int count;
            //读取文件内容
//            while ((count = inputStream.read(fileData)) != -1) {
//                sb.append(new String(fileData, 0, count));
//            }
            //声明输出流
            outputStream = new FileOutputStream(file,true);
            //写入原文件的数据
//            outputStream.write(sb.toString().getBytes());
//            outputStream.flush();
            //写入新数据
            outputStream.write(bytes);
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
