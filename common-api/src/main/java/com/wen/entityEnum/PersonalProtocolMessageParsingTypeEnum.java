package com.wen.entityEnum;

import lombok.Data;

/**
 * @author: 7wen
 * @Date: 2023-04-21 15:10
 * @description:
 */
public enum PersonalProtocolMessageParsingTypeEnum {

    Json(0x0, "json数据"),
    Protobuf(0x2, "prototbuf类型数据"),
    Xml(0x3, "xml类型数据");


    /**
     * 八进制数 表示读取数据解析类型code
     */
    private int octalNumber;


    /**
     * 代表含义
     */
    private String value;

    PersonalProtocolMessageParsingTypeEnum(int octalNumber, String value) {
        this.octalNumber = octalNumber;
        this.value = value;
    }

    public int getOctalNumber() {
        return octalNumber;
    }

    public String getValue() {
        return value;
    }
}
