package com.wen.entity;

import com.wen.entityEnum.PersonalProtocolMessageParsingTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * @author: 7wen
 * @Date: 2023-04-21 15:03
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonalProtocolDto {

    /**
     * 指令
     */
    private int command;

    /**
     * version
     */
    private int version;


    /**
     * 客户端类型
     */
    private int clientType;


    /**
     * 消息解析类型 默认json
     */
    private int messageParsingType = PersonalProtocolMessageParsingTypeEnum.Json.getOctalNumber();


    /**
     * appId
     */
    private int appId;


    /**
     * imei长度
     */
    private int imeiLen;


    /**
     * imei数据
     */
    private byte[] imei;


    /**
     * 数据长度
     */
    private int bodyLen;


    /**
     * 数据内容
     */
    private byte[] bodyData;

    @Override
    public String toString() {
        return "PersonalProtocolDto{" +
                "command=" + command +
                ", version=" + version +
                ", clientType=" + clientType +
                ", messageParsingType=" + messageParsingType +
                ", appId=" + appId +
                ", imeiLen=" + imeiLen +
                ", imei=" + new String(imei) +
                ", bodyLen=" + bodyLen +
                ", bodyData=" + new String(bodyData) +
                '}';
    }
}
