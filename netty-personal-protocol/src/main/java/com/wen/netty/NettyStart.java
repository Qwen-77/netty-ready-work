package com.wen.netty;

import com.wen.entity.NettyYamlConfig;
import com.wen.netty.base.NettyInit;
import com.wen.utils.ApplicationYamlUtil;

/**
 * @author: 7wen
 * @Date: 2023-04-21 11:09
 * @description:
 */
public class NettyStart {
    public static void main(String[] args) {
        new NettyInit(ApplicationYamlUtil.transYamlToClass().getIm());
    }
}
