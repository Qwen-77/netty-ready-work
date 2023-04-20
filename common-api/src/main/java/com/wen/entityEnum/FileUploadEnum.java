package com.wen.entityEnum;

import java.util.PriorityQueue;

/**
 * @author: 7wen
 * @Date: 2023-04-20 15:29
 * @description:
 */
public enum FileUploadEnum{

    Create_File(1, "创建文件"),
    Write_File(2, "写入文件");

    private Integer code;
    private String means;

    FileUploadEnum(Integer code, String means) {
        this.code = code;
        this.means = means;
    }

    public Integer getCode() {
        return code;
    }

    public String getMeans() {
        return means;
    }
}
