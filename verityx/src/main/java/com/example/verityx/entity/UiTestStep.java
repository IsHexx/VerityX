package com.example.verityx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UI测试步骤实体类
 */
@Data
public class UiTestStep {
    /**
     * 步骤ID，自增主键
     */
    private Integer id;
    
    /**
     * 关联用例ID
     */
    private String caseId;
    
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
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;
} 