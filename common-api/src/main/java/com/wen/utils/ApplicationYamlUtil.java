package com.wen.utils;

import com.wen.entity.NettyYamlConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.*;

/**
 * @author: 7wen
 * @Date: 2023-04-21 16:25
 * @description: 将resource中的配置读取出来
 * <p>
 * 使用snakeYaml工具包可以将yaml文件配置读取到对象当中
 */
public class ApplicationYamlUtil {

    public static NettyYamlConfig transYamlToClass() {
        Yaml yaml = new Yaml();
        try {
            String path = NettyYamlConfig.class.getClassLoader().getResource("application.yml").getPath();
            return yaml.loadAs(new FileInputStream(new File(path)), NettyYamlConfig.class);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("读取配置文件出错,无法加载");
        }
    }
}
