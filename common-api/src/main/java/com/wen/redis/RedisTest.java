package com.wen.redis;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;

/**
 * @author: 7wen
 * @Date: 2023-04-25 18:05
 * @description:
 */
public class RedisTest {
    public static void main(String[] args) {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://120.25.154.0:6379");
        singleServerConfig.setPassword("7wen");

        RedissonClient redissonClient = Redisson.create(config);
        RBucket<Object> im = redissonClient.getBucket("im");
        im.set("test");
    }
}
