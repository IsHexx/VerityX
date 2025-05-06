package com.example.verityx.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.Result;
import com.example.verityx.dto.UiTestExecutionCreateRequest;
import com.example.verityx.dto.UiTestExecutionDTO;
import com.example.verityx.dto.UiTestExecutionDetailDTO;
import com.example.verityx.dto.UiTestExecutionLogDTO;
import com.example.verityx.dto.UiTestExecutionStepDTO;
import com.example.verityx.service.UiTestExecutionService;

/**
 * UI测试执行控制器
 */
@RestController
@RequestMapping("/api/ui-test-executions")
public class UiTestExecutionController {
    
    @Autowired
    private UiTestExecutionService uiTestExecutionService;
    
    /**
     * 创建执行记录
     * 
     * @param request 创建请求
     * @return 执行ID
     */
    @PostMapping("/create")
    public Result<Long> createExecution(@RequestBody UiTestExecutionCreateRequest request) {
        // TODO: 从当前会话获取用户
        String executor = "admin"; // 暂时硬编码，后续从登录会话获取
        Long id = uiTestExecutionService.createExecution(request, executor);
        return Result.success(id);
    }
    
    /**
     * 获取执行详情
     * 
     * @param id 执行ID
     * @return 执行详情
     */
    @GetMapping("/{id}")
    public Result<UiTestExecutionDTO> getExecutionById(@PathVariable("id") Long id) {
        UiTestExecutionDTO execution = uiTestExecutionService.getExecutionById(id);
        return Result.success(execution);
    }
    
    /**
     * 分页查询执行记录
     * 
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 关键字
     * @param status 状态
     * @param executionType 执行类型
     * @return 分页结果
     */
    @GetMapping("/list")
    public Result<PageResult<UiTestExecutionDTO>> getExecutionList(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "executionType", required = false) String executionType) {
        PageResult<UiTestExecutionDTO> result = uiTestExecutionService.getExecutionList(
                page, pageSize, keyword, status, executionType);
        return Result.success(result);
    }
    
    /**
     * 获取执行队列
     * 
     * @param page 页码
     * @param pageSize 页大小
     * @param status 状态
     * @return 分页结果
     */
    @GetMapping("/queue")
    public Result<PageResult<UiTestExecutionDTO>> getExecutionQueue(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,
            @RequestParam(value = "status", required = false) String status) {
        PageResult<UiTestExecutionDTO> result = uiTestExecutionService.getExecutionQueue(
                status, page, pageSize);
        return Result.success(result);
    }
    
    /**
     * 获取执行用例详情列表
     * 
     * @param executionId 执行ID
     * @return 执行用例详情列表
     */
    @GetMapping("/{id}/details")
    public Result<List<UiTestExecutionDetailDTO>> getExecutionDetails(@PathVariable("id") Long executionId) {
        List<UiTestExecutionDetailDTO> details = uiTestExecutionService.getExecutionDetails(executionId);
        return Result.success(details);
    }
    
    /**
     * 获取执行用例详情
     * 
     * @param detailId 详情ID
     * @return 执行用例详情
     */
    @GetMapping("/details/{detailId}")
    public Result<UiTestExecutionDetailDTO> getExecutionDetail(@PathVariable("detailId") Long detailId) {
        UiTestExecutionDetailDTO detail = uiTestExecutionService.getExecutionDetail(detailId);
        return Result.success(detail);
    }
    
    /**
     * 获取执行步骤列表
     * 
     * @param detailId 详情ID
     * @return 执行步骤列表
     */
    @GetMapping("/details/{detailId}/steps")
    public Result<List<UiTestExecutionStepDTO>> getExecutionSteps(@PathVariable("detailId") Long detailId) {
        List<UiTestExecutionStepDTO> steps = uiTestExecutionService.getExecutionSteps(detailId);
        return Result.success(steps);
    }
    
    /**
     * 获取执行日志
     * 
     * @param executionId 执行ID
     * @param logLevel 日志级别
     * @param page 页码
     * @param pageSize 页大小
     * @return 分页结果
     */
    @GetMapping("/{id}/logs")
    public Result<PageResult<UiTestExecutionLogDTO>> getExecutionLogs(
            @PathVariable("id") Long executionId,
            @RequestParam(value = "logLevel", required = false) String logLevel,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "pageSize", defaultValue = "50") int pageSize) {
        PageResult<UiTestExecutionLogDTO> result = uiTestExecutionService.getExecutionLogs(
                executionId, logLevel, page, pageSize);
        return Result.success(result);
    }
    
    /**
     * 获取用例执行日志
     * 
     * @param detailId 详情ID
     * @return 日志列表
     */
    @GetMapping("/details/{detailId}/logs")
    public Result<List<UiTestExecutionLogDTO>> getDetailLogs(@PathVariable("detailId") Long detailId) {
        List<UiTestExecutionLogDTO> logs = uiTestExecutionService.getDetailLogs(detailId);
        return Result.success(logs);
    }
    
    /**
     * 开始执行
     * 
     * @param id 执行ID
     * @return 是否成功
     */
    @PostMapping("/{id}/start")
    public Result<Boolean> startExecution(@PathVariable("id") Long id) {
        boolean success = uiTestExecutionService.startExecution(id);
        return Result.success(success);
    }
    
    /**
     * 中止执行
     * 
     * @param id 执行ID
     * @param reason 原因
     * @return 是否成功
     */
    @PostMapping("/{id}/abort")
    public Result<Boolean> abortExecution(
            @PathVariable("id") Long id,
            @RequestParam(value = "reason", required = false) String reason) {
        boolean success = uiTestExecutionService.abortExecution(id, reason);
        return Result.success(success);
    }
    
    /**
     * 删除执行记录
     * 
     * @param id 执行ID
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteExecution(@PathVariable("id") Long id) {
        boolean success = uiTestExecutionService.deleteExecution(id);
        return Result.success(success);
    }
    
    /**
     * 记录执行步骤结果
     * 
     * @param step 步骤结果
     * @return 步骤ID
     */
    @PostMapping("/steps/record")
    public Result<Long> recordStepResult(@RequestBody UiTestExecutionStepDTO step) {
        Long stepId = uiTestExecutionService.recordStepResult(step);
        return Result.success(stepId);
    }
    
    /**
     * 记录日志
     * 
     * @param log 日志记录
     * @return 日志ID
     */
    @PostMapping("/logs/record")
    public Result<Long> recordLog(@RequestBody UiTestExecutionLogDTO log) {
        Long logId = uiTestExecutionService.recordLog(log);
        return Result.success(logId);
    }
    
    /**
     * 更新执行状态
     * 
     * @param id 执行ID
     * @param status 状态
     * @return 是否成功
     */
    @PutMapping("/{id}/status")
    public Result<Boolean> updateExecutionStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") String status) {
        boolean success = uiTestExecutionService.updateExecutionStatus(id, status);
        return Result.success(success);
    }
} 