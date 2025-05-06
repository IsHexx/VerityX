package com.example.verityx.dto;

import lombok.Data;

/**
 * UI测试参数数据传输对象
 */
@Data
public class UiTestParameterDTO {
    /**
     * 参数ID
     */
    private Integer id;
    
    /**
     * 参数名
     */
    private String name;
    
    /**
     * 参数值
     */
    private String value;
} 