package com.wen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 7wen
 * @Date: 2023-04-21 16:31
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NettyYamlConfig {
    private TcpConfig im;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TcpConfig {

        /**
         * tcp端口
         */
        private Integer tcpPort;

        /**
         * socket端口
         */
        private Integer socketPort;

        /**
         * boss线程可执行最大数
         */
        private Integer bossThreadSize;

        /**
         * work线程可执行最大数
         */
        private Integer workThreadSize;

        /**
         * 心跳最大延迟时间
         */
        private Integer heartBeatTime;
    }

    @Override
    public String toString() {
        return "NettyYamlConfig{" +
                "im=" + im +
                '}';
    }
}
