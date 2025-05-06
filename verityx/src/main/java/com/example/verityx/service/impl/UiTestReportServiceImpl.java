package com.example.verityx.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestExecutionDTO;
import com.example.verityx.dto.UiTestExecutionDetailDTO;
import com.example.verityx.dto.UiTestExecutionStepDTO;
import com.example.verityx.dto.UiTestReportCreateRequest;
import com.example.verityx.dto.UiTestReportDTO;
import com.example.verityx.dto.UiTestReportDetailDTO;
import com.example.verityx.dto.UiTestStepResultDTO;
import com.example.verityx.entity.UiTestReport;
import com.example.verityx.entity.UiTestReportDetail;
import com.example.verityx.entity.UiTestReportStep;
import com.example.verityx.mapper.UiTestReportDetailMapper;
import com.example.verityx.mapper.UiTestReportMapper;
import com.example.verityx.mapper.UiTestReportStepMapper;
import com.example.verityx.service.UiTestExecutionService;
import com.example.verityx.service.UiTestReportService;

/**
 * UI测试报告服务实现类
 */
@Service
public class UiTestReportServiceImpl implements UiTestReportService {
    
    private static final Logger logger = LoggerFactory.getLogger(UiTestReportServiceImpl.class);
    
    @Autowired
    private UiTestReportMapper reportMapper;
    
    @Autowired
    private UiTestReportDetailMapper detailMapper;
    
    @Autowired
    private UiTestReportStepMapper stepMapper;
    
    @Autowired
    private UiTestExecutionService executionService;
    
    @Override
    @Transactional
    public Long createReport(UiTestReportCreateRequest request) {
        // 创建报告
        UiTestReport report = new UiTestReport();
        report.setExecutionId(request.getExecutionId());
        report.setReportName(request.getReportName());
        report.setReportType(request.getReportType());
        report.setEnvironment(request.getEnvironment());
        report.setBrowser(request.getBrowser());
        report.setStartTime(request.getStartTime());
        report.setEndTime(request.getEndTime());
        report.setDuration(request.getDuration());
        report.setTotalCases(request.getTotalCases());
        report.setPassedCases(request.getPassedCases());
        report.setFailedCases(request.getFailedCases());
        report.setSkippedCases(request.getSkippedCases());
        
        // 计算通过率
        if (report.getTotalCases() != null && report.getTotalCases() > 0) {
            Double passRate = (double) report.getPassedCases() / report.getTotalCases() * 100;
            report.setPassRate(passRate);
        } else {
            report.setPassRate(0.0);
        }
        
        report.setExecutor(request.getExecutor());
        report.setReportContent(request.getReportContent());
        report.setReportStatus("已生成");
        report.setGeneratedTime(new Date());
        report.setCreatedAt(new Date());
        report.setUpdatedAt(new Date());
        
        reportMapper.insert(report);
        
        return report.getId();
    }
    
    @Override
    @Transactional
    public boolean updateReport(Long id, UiTestReportCreateRequest request) {
        UiTestReport report = reportMapper.selectById(id);
        if (report == null) {
            return false;
        }
        
        // 更新报告基本信息
        if (request.getReportName() != null) {
            report.setReportName(request.getReportName());
        }
        if (request.getReportType() != null) {
            report.setReportType(request.getReportType());
        }
        if (request.getEnvironment() != null) {
            report.setEnvironment(request.getEnvironment());
        }
        if (request.getBrowser() != null) {
            report.setBrowser(request.getBrowser());
        }
        if (request.getStartTime() != null) {
            report.setStartTime(request.getStartTime());
        }
        if (request.getEndTime() != null) {
            report.setEndTime(request.getEndTime());
        }
        if (request.getDuration() != null) {
            report.setDuration(request.getDuration());
        }
        if (request.getTotalCases() != null) {
            report.setTotalCases(request.getTotalCases());
        }
        if (request.getPassedCases() != null) {
            report.setPassedCases(request.getPassedCases());
        }
        if (request.getFailedCases() != null) {
            report.setFailedCases(request.getFailedCases());
        }
        if (request.getSkippedCases() != null) {
            report.setSkippedCases(request.getSkippedCases());
        }
        
        // 重新计算通过率
        if (report.getTotalCases() != null && report.getTotalCases() > 0) {
            Double passRate = (double) report.getPassedCases() / report.getTotalCases() * 100;
            report.setPassRate(passRate);
        }
        
        if (request.getExecutor() != null) {
            report.setExecutor(request.getExecutor());
        }
        if (request.getReportContent() != null) {
            report.setReportContent(request.getReportContent());
        }
        
        report.setUpdatedAt(new Date());
        
        return reportMapper.update(report) > 0;
    }
    
    @Override
    public UiTestReportDTO getReportById(Long id) {
        UiTestReport report = reportMapper.selectById(id);
        if (report == null) {
            return null;
        }
        
        return convertToDTO(report);
    }
    
    @Override
    public UiTestReportDTO getReportByExecutionId(Long executionId) {
        UiTestReport report = reportMapper.selectByExecutionId(executionId);
        if (report == null) {
            return null;
        }
        
        return convertToDTO(report);
    }
    
    @Override
    public PageResult<UiTestReportDTO> getReportList(int page, int pageSize, String keyword, String reportType, String startDate, String endDate, String status) {
        int offset = (page - 1) * pageSize;
        
        // 添加状态过滤逻辑
        String passRateCondition = null;
        if (status != null && !status.isEmpty()) {
            if ("passed".equalsIgnoreCase(status)) {
                // 通过：通过率 = 100%（所有用例都成功）
                passRateCondition = "pass_rate = 100";
            } else if ("failed".equalsIgnoreCase(status)) {
                // 失败：通过率 < 100%（有失败的用例）
                passRateCondition = "pass_rate < 100";
            }
        }
        
        List<UiTestReport> reports = reportMapper.selectByPage(keyword, reportType, startDate, endDate, passRateCondition, offset, pageSize);
        int total = reportMapper.countByCondition(keyword, reportType, startDate, endDate, passRateCondition);
        
        List<UiTestReportDTO> reportDTOs = reports.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(reportDTOs, total, page, pageSize);
    }
    
    @Override
    @Transactional
    public boolean deleteReport(Long id) {
        // 先删除报告步骤
        stepMapper.deleteByReportId(id);
        
        // 删除报告详情
        detailMapper.deleteByReportId(id);
        
        // 删除报告
        return reportMapper.deleteById(id) > 0;
    }
    
    @Override
    @Transactional
    public boolean batchDeleteReports(List<Long> ids) {
        for (Long id : ids) {
            // 删除报告步骤
            stepMapper.deleteByReportId(id);
            
            // 删除报告详情
            detailMapper.deleteByReportId(id);
        }
        
        // 批量删除报告
        return reportMapper.batchDelete(ids) > 0;
    }
    
    @Override
    @Transactional
    public boolean deleteReportByExecutionId(Long executionId) {
        UiTestReport report = reportMapper.selectByExecutionId(executionId);
        if (report == null) {
            return false;
        }
        
        return deleteReport(report.getId());
    }
    
    @Override
    public PageResult<UiTestReportDetailDTO> getReportDetails(Long reportId, int page, int pageSize, String result) {
        int offset = (page - 1) * pageSize;
        
        List<UiTestReportDetail> details = detailMapper.selectByPage(reportId, result, offset, pageSize);
        int total = detailMapper.countByCondition(reportId, result);
        
        List<UiTestReportDetailDTO> detailDTOs = details.stream()
                .map(this::convertToDetailDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(detailDTOs, total, page, pageSize);
    }
    
    @Override
    public UiTestReportDetailDTO getReportDetail(Long detailId) {
        UiTestReportDetail detail = detailMapper.selectById(detailId);
        if (detail == null) {
            return null;
        }
        
        return convertToDetailDTO(detail);
    }
    
    @Override
    public List<UiTestStepResultDTO> getReportSteps(Long detailId) {
        List<UiTestReportStep> steps = stepMapper.selectByReportDetailId(detailId);
        
        return steps.stream()
                .map(this::convertToStepDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Long generateReportFromExecution(Long executionId) {
        // 获取执行信息
        UiTestExecutionDTO execution = executionService.getExecutionById(executionId);
        if (execution == null) {
            logger.error("生成报告失败：执行ID不存在 {}", executionId);
            return null;
        }
        
        // 检查执行状态，只有已完成或失败的执行才能生成报告
        if (!"completed".equals(execution.getStatus()) && !"failed".equals(execution.getStatus())) {
            logger.error("生成报告失败：执行未完成 {}, 状态: {}", executionId, execution.getStatus());
            return null;
        }
        
        // 检查是否已存在报告
        UiTestReport existingReport = reportMapper.selectByExecutionId(executionId);
        if (existingReport != null) {
            logger.info("执行 {} 的报告已存在，ID: {}", executionId, existingReport.getId());
            return existingReport.getId();
        }
        
        try {
            // 创建报告请求
            UiTestReportCreateRequest request = new UiTestReportCreateRequest();
            request.setExecutionId(executionId);
            request.setReportName(execution.getExecutionName() + " 执行报告");
            request.setReportType(execution.getExecutionType());
            request.setEnvironment(execution.getEnvironment());
            request.setBrowser(execution.getEnvironment()); // 假设环境名称包含浏览器信息
            request.setStartTime(execution.getStartTime());
            request.setEndTime(execution.getEndTime());
            request.setDuration(execution.getDuration());
            
            // 获取执行详情列表，用于统计结果
            List<UiTestExecutionDetailDTO> executionDetails = executionService.getExecutionDetails(executionId);
            
            // 统计测试结果
            int totalCases = executionDetails.size();
            int passedCases = 0;
            int failedCases = 0;
            int skippedCases = 0;
            
            for (UiTestExecutionDetailDTO detail : executionDetails) {
                if ("passed".equals(detail.getStatus())) {
                    passedCases++;
                } else if ("failed".equals(detail.getStatus())) {
                    failedCases++;
                } else if ("skipped".equals(detail.getStatus())) {
                    skippedCases++;
                }
            }
            
            request.setTotalCases(totalCases);
            request.setPassedCases(passedCases);
            request.setFailedCases(failedCases);
            request.setSkippedCases(skippedCases);
            request.setExecutor(execution.getExecutor());
            
            // 创建报告
            Long reportId = createReport(request);
            
            // 创建报告详情
            for (UiTestExecutionDetailDTO executionDetail : executionDetails) {
                UiTestReportDetail reportDetail = new UiTestReportDetail();
                reportDetail.setReportId(reportId);
                reportDetail.setExecutionDetailId(executionDetail.getId());
                reportDetail.setCaseName(executionDetail.getCaseName());
                reportDetail.setCaseType(executionDetail.getCaseType());
                reportDetail.setCaseResult(executionDetail.getStatus());
                reportDetail.setDuration(executionDetail.getDuration());
                reportDetail.setStartTime(executionDetail.getStartTime());
                reportDetail.setEndTime(executionDetail.getEndTime());
                
                // 获取执行步骤列表，用于统计步骤结果
                List<UiTestExecutionStepDTO> executionSteps = executionService.getExecutionSteps(executionDetail.getId());
                
                int totalSteps = executionSteps.size();
                int passedSteps = 0;
                int failedSteps = 0;
                int skippedSteps = 0;
                String errorMessage = null;
                
                for (UiTestExecutionStepDTO step : executionSteps) {
                    if ("passed".equals(step.getStatus())) {
                        passedSteps++;
                    } else if ("failed".equals(step.getStatus())) {
                        failedSteps++;
                        // 记录第一个错误信息
                        if (errorMessage == null && step.getErrorMessage() != null) {
                            errorMessage = step.getErrorMessage();
                        }
                    } else if ("skipped".equals(step.getStatus())) {
                        skippedSteps++;
                    }
                }
                
                reportDetail.setTotalSteps(totalSteps);
                reportDetail.setPassedSteps(passedSteps);
                reportDetail.setFailedSteps(failedSteps);
                reportDetail.setSkippedSteps(skippedSteps);
                reportDetail.setErrorMessage(errorMessage);
                reportDetail.setCreatedAt(new Date());
                reportDetail.setUpdatedAt(new Date());
                
                // 记录截图URLs
                List<String> screenshotUrls = executionSteps.stream()
                        .filter(step -> step.getScreenshotAfter() != null)
                        .map(UiTestExecutionStepDTO::getScreenshotAfter)
                        .collect(Collectors.toList());
                
                if (!screenshotUrls.isEmpty()) {
                    reportDetail.setScreenshotUrls(String.join(",", screenshotUrls));
                }
                
                detailMapper.insert(reportDetail);
                
                // 创建报告步骤
                List<UiTestReportStep> reportSteps = new ArrayList<>();
                for (UiTestExecutionStepDTO executionStep : executionSteps) {
                    UiTestReportStep reportStep = new UiTestReportStep();
                    reportStep.setReportDetailId(reportDetail.getId());
                    reportStep.setStepIndex(executionStep.getStepIndex());
                    reportStep.setStepName(executionStep.getStepName());
                    reportStep.setActionType(executionStep.getActionType());
                    reportStep.setTarget(executionStep.getTarget());
                    reportStep.setInputValue(executionStep.getInputValue());
                    reportStep.setExpectedResult(executionStep.getExpectedResult());
                    reportStep.setActualResult(executionStep.getActualResult());
                    reportStep.setStatus(executionStep.getStatus());
                    reportStep.setDuration(executionStep.getDuration());
                    reportStep.setScreenshotBefore(executionStep.getScreenshotBefore());
                    reportStep.setScreenshotAfter(executionStep.getScreenshotAfter());
                    reportStep.setErrorMessage(executionStep.getErrorMessage());
                    reportStep.setExecutedAt(executionStep.getTimestamp());
                    reportStep.setCreatedAt(new Date());
                    reportStep.setUpdatedAt(new Date());
                    
                    reportSteps.add(reportStep);
                }
                
                if (!reportSteps.isEmpty()) {
                    stepMapper.batchInsert(reportSteps);
                }
            }
            
            logger.info("成功为执行 {} 生成报告，ID: {}", executionId, reportId);
            return reportId;
        } catch (Exception e) {
            logger.error("生成报告失败：", e);
            throw e;
        }
    }
    
    @Override
    public String exportReport(Long id, String format) {
        // TODO: 实现报告导出功能
        UiTestReportDTO report = getReportById(id);
        if (report == null) {
            return null;
        }
        
        // 这里需要根据实际需求实现导出逻辑
        return "/api/ui-test-reports/" + id + "/download?format=" + format;
    }
    
    /**
     * 将实体转换为DTO
     * 
     * @param report 测试报告实体
     * @return 测试报告DTO
     */
    private UiTestReportDTO convertToDTO(UiTestReport report) {
        UiTestReportDTO dto = new UiTestReportDTO();
        dto.setId(report.getId());
        dto.setExecutionId(report.getExecutionId());
        dto.setReportName(report.getReportName());
        dto.setReportType(report.getReportType());
        dto.setEnvironment(report.getEnvironment());
        dto.setBrowser(report.getBrowser());
        dto.setStartTime(report.getStartTime());
        dto.setEndTime(report.getEndTime());
        dto.setDuration(report.getDuration());
        dto.setTotalCases(report.getTotalCases());
        dto.setPassedCases(report.getPassedCases());
        dto.setFailedCases(report.getFailedCases());
        dto.setSkippedCases(report.getSkippedCases());
        dto.setPassRate(report.getPassRate());
        dto.setExecutor(report.getExecutor());
        dto.setReportUrl("/api/ui-test-reports/" + report.getId());
        dto.setReportStatus(report.getReportStatus());
        dto.setGeneratedTime(report.getGeneratedTime());
        dto.setCreatedAt(report.getCreatedAt());
        
        // 设置前端展示用的附加字段
        dto.setDurationText(formatDuration(report.getDuration()));
        
        // 设置状态文本
        String status = report.getReportStatus();
        dto.setStatusText(status);
        
        // 设置状态类型
        String statusType = "info"; // 默认
        if ("已生成".equals(status)) {
            statusType = "success";
        } else if ("生成中".equals(status)) {
            statusType = "warning";
        } else if ("生成失败".equals(status)) {
            statusType = "danger";
        }
        dto.setStatusType(statusType);
        
        return dto;
    }
    
    /**
     * 将详情实体转换为DTO
     * 
     * @param detail 测试报告详情实体
     * @return 测试报告详情DTO
     */
    private UiTestReportDetailDTO convertToDetailDTO(UiTestReportDetail detail) {
        UiTestReportDetailDTO dto = new UiTestReportDetailDTO();
        dto.setId(detail.getId());
        dto.setReportId(detail.getReportId());
        dto.setExecutionDetailId(detail.getExecutionDetailId());
        dto.setCaseName(detail.getCaseName());
        dto.setCaseType(detail.getCaseType());
        dto.setCaseResult(detail.getCaseResult());
        dto.setDuration(detail.getDuration());
        dto.setTotalSteps(detail.getTotalSteps());
        dto.setPassedSteps(detail.getPassedSteps());
        dto.setFailedSteps(detail.getFailedSteps());
        dto.setSkippedSteps(detail.getSkippedSteps());
        dto.setErrorMessage(detail.getErrorMessage());
        
        // 处理截图URLs
        if (detail.getScreenshotUrls() != null && !detail.getScreenshotUrls().isEmpty()) {
            dto.setScreenshotUrls(Arrays.asList(detail.getScreenshotUrls().split(",")));
        } else {
            dto.setScreenshotUrls(new ArrayList<>());
        }
        
        dto.setStartTime(detail.getStartTime());
        dto.setEndTime(detail.getEndTime());
        
        // 设置前端展示用的附加字段
        dto.setDurationText(formatDuration(detail.getDuration()));
        
        // 设置结果类型
        String resultType = "info"; // 默认
        if ("passed".equals(detail.getCaseResult())) {
            resultType = "success";
        } else if ("failed".equals(detail.getCaseResult())) {
            resultType = "danger";
        } else if ("skipped".equals(detail.getCaseResult())) {
            resultType = "warning";
        }
        dto.setResultType(resultType);
        
        return dto;
    }
    
    /**
     * 将步骤实体转换为DTO
     * 
     * @param step 测试报告步骤实体
     * @return 测试步骤结果DTO
     */
    private UiTestStepResultDTO convertToStepDTO(UiTestReportStep step) {
        UiTestStepResultDTO dto = new UiTestStepResultDTO();
        dto.setId(step.getId());
        dto.setReportDetailId(step.getReportDetailId());
        dto.setStepIndex(step.getStepIndex());
        dto.setStepName(step.getStepName());
        dto.setActionType(step.getActionType());
        dto.setTarget(step.getTarget());
        dto.setInputValue(step.getInputValue());
        dto.setExpectedResult(step.getExpectedResult());
        dto.setActualResult(step.getActualResult());
        dto.setStatus(step.getStatus());
        dto.setDuration(step.getDuration());
        dto.setScreenshotBefore(step.getScreenshotBefore());
        dto.setScreenshotAfter(step.getScreenshotAfter());
        dto.setErrorMessage(step.getErrorMessage());
        dto.setExecutedAt(step.getExecutedAt());
        
        // 设置前端展示用的附加字段
        dto.setDurationText(formatDuration(step.getDuration()));
        
        // 设置状态类型
        String statusType = "info"; // 默认
        if ("passed".equals(step.getStatus())) {
            statusType = "success";
        } else if ("failed".equals(step.getStatus())) {
            statusType = "danger";
        } else if ("skipped".equals(step.getStatus())) {
            statusType = "warning";
        }
        dto.setStatusType(statusType);
        
        return dto;
    }
    
    /**
     * 格式化持续时间，转换为可读文本
     * 
     * @param durationInMillis 毫秒数
     * @return 格式化的持续时间文本
     */
    private String formatDuration(Integer durationInMillis) {
        if (durationInMillis == null) {
            return "0毫秒";
        }
        
        if (durationInMillis < 1000) {
            return durationInMillis + "毫秒";
        }
        
        long minutes = TimeUnit.MILLISECONDS.toMinutes(durationInMillis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % 60;
        
        if (minutes > 0) {
            return minutes + "分钟" + seconds + "秒";
        } else {
            return seconds + "秒";
        }
    }
} 