package com.example.verityx.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestExecutionCreateRequest;
import com.example.verityx.dto.UiTestExecutionDTO;
import com.example.verityx.dto.UiTestExecutionDetailDTO;
import com.example.verityx.dto.UiTestExecutionLogDTO;
import com.example.verityx.dto.UiTestExecutionStepDTO;
import com.example.verityx.entity.UiTestExecution;
import com.example.verityx.entity.UiTestExecutionDetail;
import com.example.verityx.entity.UiTestExecutionLog;
import com.example.verityx.entity.UiTestExecutionQueue;
import com.example.verityx.entity.UiTestExecutionStep;
import com.example.verityx.mapper.UiTestExecutionDetailMapper;
import com.example.verityx.mapper.UiTestExecutionLogMapper;
import com.example.verityx.mapper.UiTestExecutionMapper;
import com.example.verityx.mapper.UiTestExecutionQueueMapper;
import com.example.verityx.mapper.UiTestExecutionStepMapper;
import com.example.verityx.service.UiTestExecutionService;

/**
 * UI测试执行服务实现类
 */
@Service
public class UiTestExecutionServiceImpl implements UiTestExecutionService {
    
    @Autowired
    private UiTestExecutionMapper executionMapper;
    
    @Autowired
    private UiTestExecutionDetailMapper detailMapper;
    
    @Autowired
    private UiTestExecutionStepMapper stepMapper;
    
    @Autowired
    private UiTestExecutionLogMapper logMapper;
    
    @Autowired
    private UiTestExecutionQueueMapper queueMapper;
    
    @Override
    @Transactional
    public Long createExecution(UiTestExecutionCreateRequest request, String executor) {
        // 创建执行记录
        UiTestExecution execution = new UiTestExecution();
        execution.setExecutionName(request.getExecutionName());
        execution.setExecutionType(request.getExecutionType());
        execution.setExecutionSourceId(request.getExecutionSourceId());
        execution.setExecutionSourceName(request.getExecutionName()); // 暂时使用执行名称作为来源名称
        execution.setStatus("waiting");
        execution.setExecutor(executor);
        execution.setEnvironment(request.getEnvironment());
        execution.setSuccessCount(0);
        execution.setFailCount(0);
        execution.setTotalCount(0);
        execution.setExecutionConfig(request.getExecutionConfig());
        
        executionMapper.insert(execution);
        
        // 创建队列记录
        UiTestExecutionQueue queue = new UiTestExecutionQueue();
        queue.setExecutionId(execution.getId());
        queue.setPriority(request.getPriority() != null ? request.getPriority() : 0);
        queue.setStatus("waiting");
        queue.setQueueTime(new Date());
        
        queueMapper.insert(queue);
        
        // 如果是单用例执行类型，添加详情记录
        if ("case".equals(request.getExecutionType()) && request.getCaseIds() != null && !request.getCaseIds().isEmpty()) {
            List<UiTestExecutionDetail> details = new ArrayList<>();
            
            for (Long caseId : request.getCaseIds()) {
                UiTestExecutionDetail detail = new UiTestExecutionDetail();
                detail.setExecutionId(execution.getId());
                detail.setCaseId(caseId);
                detail.setCaseName("待获取"); // 暂时使用默认值，后续可以获取用例名称
                detail.setStatus("waiting");
                detail.setRetryCount(0);
                detail.setStepCount(0);
                detail.setStepPassed(0);
                detail.setStepFailed(0);
                
                details.add(detail);
            }
            
            if (!details.isEmpty()) {
                detailMapper.batchInsert(details);
                execution.setTotalCount(details.size());
                executionMapper.updateCounts(execution.getId(), 0, 0, details.size());
            }
        }
        
        return execution.getId();
    }
    
    @Override
    public UiTestExecutionDTO getExecutionById(Long id) {
        UiTestExecution execution = executionMapper.selectById(id);
        return convertToDTO(execution);
    }
    
    @Override
    public PageResult<UiTestExecutionDTO> getExecutionList(int page, int pageSize, String keyword, String status, String executionType) {
        int offset = (page - 1) * pageSize;
        
        List<UiTestExecution> executions = executionMapper.selectByPage(keyword, status, executionType, offset, pageSize);
        int total = executionMapper.countByCondition(keyword, status, executionType);
        
        List<UiTestExecutionDTO> dtoList = new ArrayList<>();
        for (UiTestExecution execution : executions) {
            dtoList.add(convertToDTO(execution));
        }
        
        return new PageResult<>(dtoList, total, page, pageSize);
    }
    
    @Override
    @Transactional
    public boolean updateExecutionStatus(Long id, String status) {
        int result = executionMapper.updateStatus(id, status);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean startExecution(Long id) {
        UiTestExecution execution = executionMapper.selectById(id);
        if (execution == null) {
            return false;
        }
        
        execution.setStatus("running");
        execution.setStartTime(new Date());
        executionMapper.update(execution);
        
        // 更新队列状态
        UiTestExecutionQueue queue = queueMapper.selectByExecutionId(id);
        if (queue != null) {
            queue.setStatus("processing");
            queue.setStartTime(new Date());
            queueMapper.update(queue);
        }
        
        // 记录开始执行日志
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(id);
        log.setLogLevel("info");
        log.setLogContent("开始执行测试");
        log.setTimestamp(new Date());
        logMapper.insert(log);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean finishExecution(Long id, boolean success, String errorMessage) {
        UiTestExecution execution = executionMapper.selectById(id);
        if (execution == null) {
            return false;
        }
        
        execution.setStatus(success ? "completed" : "failed");
        execution.setEndTime(new Date());
        
        if (execution.getStartTime() != null) {
            long duration = (execution.getEndTime().getTime() - execution.getStartTime().getTime()) / 1000;
            execution.setDuration((int) duration);
        }
        
        executionMapper.update(execution);
        
        // 更新队列状态
        UiTestExecutionQueue queue = queueMapper.selectByExecutionId(id);
        if (queue != null) {
            queue.setStatus("completed");
            queue.setEndTime(new Date());
            queueMapper.update(queue);
        }
        
        // 记录完成执行日志
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(id);
        log.setLogLevel(success ? "info" : "error");
        log.setLogContent(success ? "测试执行完成" : "测试执行失败: " + errorMessage);
        log.setTimestamp(new Date());
        logMapper.insert(log);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean abortExecution(Long id, String reason) {
        UiTestExecution execution = executionMapper.selectById(id);
        if (execution == null) {
            return false;
        }
        
        execution.setStatus("aborted");
        execution.setEndTime(new Date());
        
        if (execution.getStartTime() != null) {
            long duration = (execution.getEndTime().getTime() - execution.getStartTime().getTime()) / 1000;
            execution.setDuration((int) duration);
        }
        
        executionMapper.update(execution);
        
        // 更新队列状态
        UiTestExecutionQueue queue = queueMapper.selectByExecutionId(id);
        if (queue != null) {
            queue.setStatus("completed");
            queue.setEndTime(new Date());
            queueMapper.update(queue);
        }
        
        // 更新所有等待中的详情记录状态为跳过
        List<UiTestExecutionDetail> details = detailMapper.selectByExecutionIdAndStatus(id, "waiting");
        for (UiTestExecutionDetail detail : details) {
            detail.setStatus("skipped");
            detailMapper.update(detail);
        }
        
        // 记录中止执行日志
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(id);
        log.setLogLevel("warning");
        log.setLogContent("测试执行被中止: " + (reason != null ? reason : "用户手动中止"));
        log.setTimestamp(new Date());
        logMapper.insert(log);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean deleteExecution(Long id) {
        // 删除相关的日志、步骤和详情记录
        List<UiTestExecutionDetail> details = detailMapper.selectByExecutionId(id);
        for (UiTestExecutionDetail detail : details) {
            stepMapper.deleteByDetailId(detail.getId());
            logMapper.deleteByDetailId(detail.getId());
        }
        
        logMapper.deleteByExecutionId(id);
        detailMapper.deleteByExecutionId(id);
        queueMapper.deleteByExecutionId(id);
        
        int result = executionMapper.deleteById(id);
        return result > 0;
    }
    
    @Override
    public List<UiTestExecutionDetailDTO> getExecutionDetails(Long executionId) {
        List<UiTestExecutionDetail> details = detailMapper.selectByExecutionId(executionId);
        List<UiTestExecutionDetailDTO> dtoList = new ArrayList<>();
        
        for (UiTestExecutionDetail detail : details) {
            dtoList.add(convertToDetailDTO(detail));
        }
        
        return dtoList;
    }
    
    @Override
    public UiTestExecutionDetailDTO getExecutionDetail(Long detailId) {
        UiTestExecutionDetail detail = detailMapper.selectById(detailId);
        return convertToDetailDTO(detail);
    }
    
    @Override
    @Transactional
    public boolean updateDetailStatus(Long detailId, String status) {
        int result = detailMapper.updateStatus(detailId, status);
        return result > 0;
    }
    
    @Override
    @Transactional
    public boolean startDetail(Long detailId) {
        UiTestExecutionDetail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            return false;
        }
        
        detail.setStatus("running");
        detail.setStartTime(new Date());
        detailMapper.update(detail);
        
        // 记录开始执行日志
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(detail.getExecutionId());
        log.setDetailId(detailId);
        log.setLogLevel("info");
        log.setLogContent("开始执行用例: " + detail.getCaseName());
        log.setTimestamp(new Date());
        logMapper.insert(log);
        
        return true;
    }
    
    @Override
    @Transactional
    public boolean finishDetail(Long detailId, boolean success, String errorMessage) {
        UiTestExecutionDetail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            return false;
        }
        
        detail.setStatus(success ? "passed" : "failed");
        detail.setEndTime(new Date());
        
        if (detail.getStartTime() != null) {
            long duration = (detail.getEndTime().getTime() - detail.getStartTime().getTime()) / 1000;
            detail.setDuration((int) duration);
        }
        
        if (!success && errorMessage != null) {
            detail.setErrorMessage(errorMessage);
        }
        
        // 更新步骤统计信息
        int stepCount = stepMapper.countSteps(detailId);
        int stepPassed = stepMapper.countPassedSteps(detailId);
        int stepFailed = stepMapper.countFailedSteps(detailId);
        
        detail.setStepCount(stepCount);
        detail.setStepPassed(stepPassed);
        detail.setStepFailed(stepFailed);
        
        detailMapper.update(detail);
        
        // 更新执行记录的统计信息
        UiTestExecution execution = executionMapper.selectById(detail.getExecutionId());
        if (execution != null) {
            List<UiTestExecutionDetail> details = detailMapper.selectByExecutionId(detail.getExecutionId());
            
            int successCount = 0;
            int failCount = 0;
            int totalCount = details.size();
            
            for (UiTestExecutionDetail d : details) {
                if ("passed".equals(d.getStatus())) {
                    successCount++;
                } else if ("failed".equals(d.getStatus())) {
                    failCount++;
                }
            }
            
            executionMapper.updateCounts(detail.getExecutionId(), successCount, failCount, totalCount);
        }
        
        // 记录完成执行日志
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(detail.getExecutionId());
        log.setDetailId(detailId);
        log.setLogLevel(success ? "info" : "error");
        log.setLogContent(success ? "用例执行完成: " + detail.getCaseName() : "用例执行失败: " + detail.getCaseName() + " - " + errorMessage);
        log.setTimestamp(new Date());
        logMapper.insert(log);
        
        return true;
    }
    
    @Override
    public List<UiTestExecutionStepDTO> getExecutionSteps(Long detailId) {
        List<UiTestExecutionStep> steps = stepMapper.selectByDetailId(detailId);
        List<UiTestExecutionStepDTO> dtoList = new ArrayList<>();
        
        for (UiTestExecutionStep step : steps) {
            dtoList.add(convertToStepDTO(step));
        }
        
        return dtoList;
    }
    
    @Override
    @Transactional
    public Long recordStepResult(UiTestExecutionStepDTO stepDTO) {
        UiTestExecutionStep step = new UiTestExecutionStep();
        step.setDetailId(stepDTO.getDetailId());
        step.setStepIndex(stepDTO.getStepIndex());
        step.setStepName(stepDTO.getStepName());
        step.setActionType(stepDTO.getActionType());
        step.setTarget(stepDTO.getTarget());
        step.setInputValue(stepDTO.getInputValue());
        step.setExpectedResult(stepDTO.getExpectedResult());
        step.setActualResult(stepDTO.getActualResult());
        step.setStatus(stepDTO.getStatus());
        step.setScreenshotPath(stepDTO.getScreenshotPath());
        step.setErrorMessage(stepDTO.getErrorMessage());
        step.setDuration(stepDTO.getDuration());
        step.setTimestamp(stepDTO.getTimestamp() != null ? stepDTO.getTimestamp() : new Date());
        
        stepMapper.insert(step);
        
        // 更新详情记录的步骤统计信息
        int stepCount = stepMapper.countSteps(stepDTO.getDetailId());
        int stepPassed = stepMapper.countPassedSteps(stepDTO.getDetailId());
        int stepFailed = stepMapper.countFailedSteps(stepDTO.getDetailId());
        
        detailMapper.updateStepCounts(stepDTO.getDetailId(), stepCount, stepPassed, stepFailed);
        
        return step.getId();
    }
    
    @Override
    public PageResult<UiTestExecutionLogDTO> getExecutionLogs(Long executionId, String logLevel, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        
        List<UiTestExecutionLog> logs = logMapper.selectByExecutionIdWithPage(executionId, logLevel, offset, pageSize);
        int total = logMapper.countByExecutionId(executionId, logLevel);
        
        List<UiTestExecutionLogDTO> dtoList = new ArrayList<>();
        for (UiTestExecutionLog log : logs) {
            dtoList.add(convertToLogDTO(log));
        }
        
        return new PageResult<>(dtoList, total, page, pageSize);
    }
    
    @Override
    public List<UiTestExecutionLogDTO> getDetailLogs(Long detailId) {
        List<UiTestExecutionLog> logs = logMapper.selectByDetailId(detailId);
        List<UiTestExecutionLogDTO> dtoList = new ArrayList<>();
        
        for (UiTestExecutionLog log : logs) {
            dtoList.add(convertToLogDTO(log));
        }
        
        return dtoList;
    }
    
    @Override
    @Transactional
    public Long recordLog(UiTestExecutionLogDTO logDTO) {
        UiTestExecutionLog log = new UiTestExecutionLog();
        log.setExecutionId(logDTO.getExecutionId());
        log.setDetailId(logDTO.getDetailId());
        log.setLogLevel(logDTO.getLogLevel());
        log.setLogContent(logDTO.getLogContent());
        log.setStepIndex(logDTO.getStepIndex());
        log.setStepName(logDTO.getStepName());
        log.setTimestamp(logDTO.getTimestamp() != null ? logDTO.getTimestamp() : new Date());
        
        logMapper.insert(log);
        
        return log.getId();
    }
    
    @Override
    public PageResult<UiTestExecutionDTO> getExecutionQueue(String status, int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        
        List<UiTestExecutionQueue> queues = queueMapper.selectByPage(status, offset, pageSize);
        int total = queueMapper.countByStatus(status);
        
        List<UiTestExecutionDTO> dtoList = new ArrayList<>();
        for (UiTestExecutionQueue queue : queues) {
            UiTestExecution execution = executionMapper.selectById(queue.getExecutionId());
            if (execution != null) {
                dtoList.add(convertToDTO(execution));
            }
        }
        
        return new PageResult<>(dtoList, total, page, pageSize);
    }
    
    @Override
    public UiTestExecutionDTO getNextWaitingExecution() {
        UiTestExecutionQueue queue = queueMapper.selectNextWaiting();
        if (queue == null) {
            return null;
        }
        
        UiTestExecution execution = executionMapper.selectById(queue.getExecutionId());
        return convertToDTO(execution);
    }
    
    // DTO转换方法
    private UiTestExecutionDTO convertToDTO(UiTestExecution execution) {
        if (execution == null) {
            return null;
        }
        
        UiTestExecutionDTO dto = new UiTestExecutionDTO();
        dto.setId(execution.getId());
        dto.setExecutionName(execution.getExecutionName());
        dto.setExecutionType(execution.getExecutionType());
        dto.setExecutionSourceId(execution.getExecutionSourceId());
        dto.setExecutionSourceName(execution.getExecutionSourceName());
        dto.setStatus(execution.getStatus());
        dto.setExecutor(execution.getExecutor());
        dto.setEnvironment(execution.getEnvironment());
        dto.setStartTime(execution.getStartTime());
        dto.setEndTime(execution.getEndTime());
        dto.setDuration(execution.getDuration());
        dto.setSuccessCount(execution.getSuccessCount());
        dto.setFailCount(execution.getFailCount());
        dto.setTotalCount(execution.getTotalCount());
        dto.setExecutionConfig(execution.getExecutionConfig());
        dto.setCreatedAt(execution.getCreatedAt());
        
        // 设置前端展示字段
        setDisplayFields(dto);
        
        return dto;
    }
    
    private void setDisplayFields(UiTestExecutionDTO dto) {
        // 设置状态文本和类型
        switch (dto.getStatus()) {
            case "waiting":
                dto.setStatusText("等待中");
                dto.setStatusType("info");
                break;
            case "running":
                dto.setStatusText("执行中");
                dto.setStatusType("warning");
                break;
            case "completed":
                dto.setStatusText("已完成");
                dto.setStatusType("success");
                break;
            case "failed":
                dto.setStatusText("失败");
                dto.setStatusType("danger");
                break;
            case "aborted":
                dto.setStatusText("已中止");
                dto.setStatusType("warning");
                break;
            default:
                dto.setStatusText(dto.getStatus());
                dto.setStatusType("info");
        }
        
        // 设置持续时间文本
        if (dto.getDuration() != null) {
            int seconds = dto.getDuration();
            int hours = seconds / 3600;
            int minutes = (seconds % 3600) / 60;
            seconds = seconds % 60;
            
            StringBuilder sb = new StringBuilder();
            if (hours > 0) {
                sb.append(hours).append("小时");
            }
            if (minutes > 0 || hours > 0) {
                sb.append(minutes).append("分");
            }
            sb.append(seconds).append("秒");
            
            dto.setDurationText(sb.toString());
        }
        
        // 设置成功率
        if (dto.getTotalCount() != null && dto.getTotalCount() > 0) {
            double successRate = (double) dto.getSuccessCount() / dto.getTotalCount() * 100;
            dto.setSuccessRate(Math.round(successRate * 10) / 10.0);
        } else {
            dto.setSuccessRate(0.0);
        }
    }
    
    private UiTestExecutionDetailDTO convertToDetailDTO(UiTestExecutionDetail detail) {
        if (detail == null) {
            return null;
        }
        
        UiTestExecutionDetailDTO dto = new UiTestExecutionDetailDTO();
        dto.setId(detail.getId());
        dto.setExecutionId(detail.getExecutionId());
        dto.setCaseId(detail.getCaseId());
        dto.setCaseName(detail.getCaseName());
        dto.setStatus(detail.getStatus());
        dto.setStartTime(detail.getStartTime());
        dto.setEndTime(detail.getEndTime());
        dto.setDuration(detail.getDuration());
        dto.setErrorMessage(detail.getErrorMessage());
        dto.setScreenshotPath(detail.getScreenshotPath());
        dto.setRetryCount(detail.getRetryCount());
        dto.setStepCount(detail.getStepCount());
        dto.setStepPassed(detail.getStepPassed());
        dto.setStepFailed(detail.getStepFailed());
        
        // 设置前端展示字段
        setDetailDisplayFields(dto);
        
        return dto;
    }
    
    private void setDetailDisplayFields(UiTestExecutionDetailDTO dto) {
        // 设置状态文本和类型
        switch (dto.getStatus()) {
            case "waiting":
                dto.setStatusText("等待中");
                dto.setStatusType("info");
                break;
            case "running":
                dto.setStatusText("执行中");
                dto.setStatusType("warning");
                break;
            case "passed":
                dto.setStatusText("通过");
                dto.setStatusType("success");
                break;
            case "failed":
                dto.setStatusText("失败");
                dto.setStatusType("danger");
                break;
            case "skipped":
                dto.setStatusText("已跳过");
                dto.setStatusType("info");
                break;
            default:
                dto.setStatusText(dto.getStatus());
                dto.setStatusType("info");
        }
        
        // 设置持续时间文本
        if (dto.getDuration() != null) {
            int seconds = dto.getDuration();
            int minutes = seconds / 60;
            seconds = seconds % 60;
            
            StringBuilder sb = new StringBuilder();
            if (minutes > 0) {
                sb.append(minutes).append("分");
            }
            sb.append(seconds).append("秒");
            
            dto.setDurationText(sb.toString());
        }
        
        // 设置成功率
        if (dto.getStepCount() != null && dto.getStepCount() > 0) {
            double successRate = (double) dto.getStepPassed() / dto.getStepCount() * 100;
            dto.setSuccessRate(Math.round(successRate * 10) / 10.0);
        } else {
            dto.setSuccessRate(0.0);
        }
    }
    
    private UiTestExecutionStepDTO convertToStepDTO(UiTestExecutionStep step) {
        if (step == null) {
            return null;
        }
        
        UiTestExecutionStepDTO dto = new UiTestExecutionStepDTO();
        dto.setId(step.getId());
        dto.setDetailId(step.getDetailId());
        dto.setStepIndex(step.getStepIndex());
        dto.setStepName(step.getStepName());
        dto.setActionType(step.getActionType());
        dto.setTarget(step.getTarget());
        dto.setInputValue(step.getInputValue());
        dto.setExpectedResult(step.getExpectedResult());
        dto.setActualResult(step.getActualResult());
        dto.setStatus(step.getStatus());
        dto.setScreenshotPath(step.getScreenshotPath());
        dto.setErrorMessage(step.getErrorMessage());
        dto.setDuration(step.getDuration());
        dto.setTimestamp(step.getTimestamp());
        
        // 设置前端展示字段
        setStepDisplayFields(dto);
        
        return dto;
    }
    
    private void setStepDisplayFields(UiTestExecutionStepDTO dto) {
        // 设置状态类型
        switch (dto.getStatus()) {
            case "passed":
                dto.setStatusType("success");
                break;
            case "failed":
                dto.setStatusType("danger");
                break;
            case "skipped":
                dto.setStatusType("info");
                break;
            default:
                dto.setStatusType("warning");
        }
        
        // 设置操作类型文本
        switch (dto.getActionType()) {
            case "navigate":
                dto.setActionTypeText("浏览器导航");
                break;
            case "click":
                dto.setActionTypeText("点击");
                break;
            case "input":
                dto.setActionTypeText("输入");
                break;
            case "select":
                dto.setActionTypeText("下拉选择");
                break;
            case "wait":
                dto.setActionTypeText("等待");
                break;
            case "assertText":
                dto.setActionTypeText("文本断言");
                break;
            case "assertExists":
                dto.setActionTypeText("存在断言");
                break;
            case "getText":
                dto.setActionTypeText("获取文本");
                break;
            default:
                dto.setActionTypeText(dto.getActionType());
        }
        
        // 设置持续时间文本
        if (dto.getDuration() != null) {
            if (dto.getDuration() < 1000) {
                dto.setDurationText(dto.getDuration() + "毫秒");
            } else {
                float seconds = dto.getDuration() / 1000f;
                dto.setDurationText(String.format("%.1f秒", seconds));
            }
        }
    }
    
    private UiTestExecutionLogDTO convertToLogDTO(UiTestExecutionLog log) {
        if (log == null) {
            return null;
        }
        
        UiTestExecutionLogDTO dto = new UiTestExecutionLogDTO();
        dto.setId(log.getId());
        dto.setExecutionId(log.getExecutionId());
        dto.setDetailId(log.getDetailId());
        dto.setLogLevel(log.getLogLevel());
        dto.setLogContent(log.getLogContent());
        dto.setStepIndex(log.getStepIndex());
        dto.setStepName(log.getStepName());
        dto.setTimestamp(log.getTimestamp());
        
        // 设置前端展示字段
        switch (dto.getLogLevel()) {
            case "error":
                dto.setLogLevelType("danger");
                break;
            case "warning":
                dto.setLogLevelType("warning");
                break;
            case "info":
                dto.setLogLevelType("info");
                break;
            case "debug":
                dto.setLogLevelType("success");
                break;
            default:
                dto.setLogLevelType("info");
        }
        
        // 格式化时间
        if (dto.getTimestamp() != null) {
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            dto.setFormattedTime(sdf.format(dto.getTimestamp()));
        }
        
        return dto;
    }
} 