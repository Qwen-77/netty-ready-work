package com.wen.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: 7wen 速度
 * @Date: 2023-04-20 15:11
 * @description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileDto {
    private String fileName;
    private Integer command;
    private byte[] bytes;
}
