package com.example.verityx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * UI测试参数实体类
 */
@Data
public class UiTestParameter {
    /**
     * 参数ID，自增主键
     */
    private Integer id;
    
    /**
     * 关联用例ID
     */
    private String caseId;
    
    /**
     * 参数名
     */
    private String paramName;
    
    /**
     * 参数值
     */
    private String paramValue;
    
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