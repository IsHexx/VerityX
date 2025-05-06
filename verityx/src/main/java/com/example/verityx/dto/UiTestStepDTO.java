package com.example.verityx.dto;

import lombok.Data;

/**
 * UI测试步骤数据传输对象
 */
@Data
public class UiTestStepDTO {
    /**
     * 步骤ID
     */
    private Integer id;
    
    /**
     * 步骤序号
     */
    private Integer stepIndex;
    
    /**
     * 操作类型：click、input、select、getText、assertText、assertExists、wait、navigate等
     */
    private String actionType;
    
    /**
     * 元素定位类型：id、name、css、xpath、linkText等
     */
    private String locatorType;
    
    /**
     * 元素定位值
     */
    private String locatorValue;
    
    /**
     * 输入/期望值
     */
    private String inputValue;
    
    /**
     * 等待时间(毫秒)
     */
    private Integer waitTime;
    
    /**
     * 目标URL
     */
    private String targetUrl;
    
    /**
     * 步骤描述
     */
    private String description;
} 