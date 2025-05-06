package com.example.verityx.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * UI测试用例实体类
 */
@Data
public class UiTestCase {
    /**
     * UI测试用例ID，自增主键
     */
    private Long id;
    
    /**
     * 用例编号，如UI-001
     */
    private String caseId;
    
    /**
     * 用例标题
     */
    private String caseTitle;
    
    /**
     * 所属项目ID
     */
    private Integer projectId;
    
    /**
     * 用例优先级，如P0、P1、P2、P3
     */
    private String importanceLevel;
    
    /**
     * 测试环境，如Chrome、Firefox、Edge、Safari
     */
    private String environment;
    
    /**
     * 前置条件
     */
    private String precondition;
    
    /**
     * 用例描述
     */
    private String description;
    
    /**
     * 超时时间(秒)
     */
    private Integer timeout;
    
    /**
     * 失败重试次数
     */
    private Integer retryCount;
    
    /**
     * 截图策略：failureOnly(仅失败时)、everyStep(每个步骤)、none(不截图)
     */
    private String screenshotPolicy;
    
    /**
     * 执行状态：已执行、未执行、执行中
     */
    private String caseStatus;
    
    /**
     * 最后执行结果：通过、失败、部分通过、未执行、执行中
     */
    private String lastResult;
    
    /**
     * 最后执行时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date lastRunAt;
    
    /**
     * 创建人
     */
    private String createdBy;
    
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createdAt;
    
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updatedAt;
    
    // 步骤数量，非数据库字段
    private transient Integer stepCount;
} 