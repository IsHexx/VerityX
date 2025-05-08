package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysMonitorDataDTO;
import com.example.verityx.dto.SysOperationLogDTO;
import com.example.verityx.dto.SysErrorLogDTO;
import com.example.verityx.dto.SysPerformanceLogDTO;
import com.example.verityx.dto.request.SysLogQueryRequest;
import com.example.verityx.dto.request.SysCleanupRequest;
import com.example.verityx.entity.SysMonitorData;
import com.example.verityx.mapper.SysMonitorDataMapper;
import com.example.verityx.mapper.SysOperationLogMapper;
import com.example.verityx.mapper.SysErrorLogMapper;
import com.example.verityx.mapper.SysPerformanceLogMapper;
import com.example.verityx.service.SysMonitorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 系统监控服务实现类
 */
@Service
public class SysMonitorServiceImpl extends ServiceImpl<SysMonitorDataMapper, SysMonitorData> implements SysMonitorService {
    
    private final Logger logger = LoggerFactory.getLogger(SysMonitorServiceImpl.class);
    
    @Autowired
    private SysMonitorDataMapper sysMonitorDataMapper;
    
    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;
    
    @Autowired
    private SysErrorLogMapper sysErrorLogMapper;

    @Autowired
    private SysPerformanceLogMapper sysPerformanceLogMapper;
    
    @Override
    public Map<String, Object> getSystemStatus() {
        Map<String, Object> result = new HashMap<>();
        
        // 获取各类状态数据
        List<SysMonitorDataDTO> serverMetrics = getServerMetrics();
        List<SysMonitorDataDTO> databaseMetrics = getDatabaseMetrics();
        List<SysMonitorDataDTO> testEngineMetrics = getTestEngineMetrics();
        
        // 组织数据结构
        Map<String, Object> serverStatus = new HashMap<>();
        serverStatus.put("metrics", serverMetrics);
        serverStatus.put("status", getOverallStatus(serverMetrics));
        
        Map<String, Object> databaseStatus = new HashMap<>();
        databaseStatus.put("metrics", databaseMetrics);
        databaseStatus.put("status", getOverallStatus(databaseMetrics));
        
        Map<String, Object> testEngineStatus = new HashMap<>();
        testEngineStatus.put("metrics", testEngineMetrics);
        testEngineStatus.put("status", getOverallStatus(testEngineMetrics));
        
        result.put("server", serverStatus);
        result.put("database", databaseStatus);
        result.put("testEngine", testEngineStatus);
        
        return result;
    }
    
    /**
     * 根据监控指标获取整体状态
     */
    private String getOverallStatus(List<SysMonitorDataDTO> metrics) {
        if (metrics == null || metrics.isEmpty()) {
            return "UNKNOWN";
        }
        
        boolean hasError = false;
        boolean hasWarning = false;
        
        for (SysMonitorDataDTO metric : metrics) {
            if ("ERROR".equals(metric.getStatus())) {
                hasError = true;
                break;
            } else if ("WARNING".equals(metric.getStatus())) {
                hasWarning = true;
            }
        }
        
        if (hasError) {
            return "ERROR";
        } else if (hasWarning) {
            return "WARNING";
        } else {
            return "NORMAL";
        }
    }
    
    @Override
    public List<SysMonitorDataDTO> getServerMetrics() {
        // 从数据库获取最新的服务器监控数据
        return sysMonitorDataMapper.selectLatestMonitorDataByType("SERVER");
    }
    
    @Override
    public List<SysMonitorDataDTO> getDatabaseMetrics() {
        // 从数据库获取最新的数据库监控数据
        return sysMonitorDataMapper.selectLatestMonitorDataByType("DATABASE");
    }
    
    @Override
    public List<SysMonitorDataDTO> getTestEngineMetrics() {
        // 从数据库获取最新的测试引擎监控数据
        return sysMonitorDataMapper.selectLatestMonitorDataByType("TEST_ENGINE");
    }
    
    @Override
    public List<SysMonitorDataDTO> getResourceTrend(String metricsType, LocalDateTime startTime, LocalDateTime endTime) {
        if (startTime == null) {
            // 默认24小时前
            startTime = LocalDateTime.now().minusHours(24);
        }
        
        if (endTime == null) {
            endTime = LocalDateTime.now();
        }
        
        return sysMonitorDataMapper.selectMonitorDataTrend("SERVER", metricsType, startTime, endTime);
    }
    
    @Override
    public IPage<SysOperationLogDTO> getOperationLogPage(int page, int size, SysLogQueryRequest request) {
        Page<SysOperationLogDTO> pageParam = new Page<>(page, size);
        
        // 解析请求参数
        Long userId = null;
        String username = null;
        String logType = null;
        String logLevel = null;
        String module = null;
        String keyword = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        
        if (request != null) {
            userId = request.getUserId();
            username = request.getUsername();
            logType = request.getLogType();
            logLevel = request.getLevels() != null && !request.getLevels().isEmpty() ? request.getLevels().get(0) : null;
            keyword = request.getKeyword();
            startTime = request.getStartTime();
            endTime = request.getEndTime();
        }
        
        return sysOperationLogMapper.selectOperationLogPage(pageParam, userId, username, logType, 
                logLevel, module, keyword, startTime, endTime);
    }
    
    @Override
    public SysOperationLogDTO getOperationLogById(Long id) {
        return sysOperationLogMapper.selectOperationLogById(id);
    }
    
    @Override
    public String exportOperationLog(SysLogQueryRequest request) {
        // 导出日志的实现，可以生成Excel或CSV文件
        // 这里简化实现，实际开发中需要根据查询条件查询数据并生成文件
        logger.info("导出操作日志：{}", request);
        return "操作日志导出成功，文件路径：/exports/operation_log_" + System.currentTimeMillis() + ".xlsx";
    }
    
    @Override
    public IPage<SysErrorLogDTO> getErrorLogPage(int page, int size, SysLogQueryRequest request) {
        Page<SysErrorLogDTO> pageParam = new Page<>(page, size);
        
        // 解析请求参数
        Long userId = null;
        String username = null;
        String requestMethod = null;
        String keyword = null;
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        
        if (request != null) {
            userId = request.getUserId();
            username = request.getUsername();
            requestMethod = request.getRequestMethod();
            keyword = request.getKeyword();
            startTime = request.getStartTime();
            endTime = request.getEndTime();
        }
        
        return sysErrorLogMapper.selectErrorLogPage(pageParam, userId, username, requestMethod, 
                keyword, startTime, endTime);
    }
    
    @Override
    public SysErrorLogDTO getErrorLogById(Long id) {
        return sysErrorLogMapper.selectErrorLogById(id);
    }
    
    @Override
    public String exportErrorLog(SysLogQueryRequest request) {
        // 导出日志的实现，可以生成Excel或CSV文件
        // 这里简化实现，实际开发中需要根据查询条件查询数据并生成文件
        logger.info("导出异常日志：{}", request);
        return "异常日志导出成功，文件路径：/exports/error_log_" + System.currentTimeMillis() + ".xlsx";
    }
    
    /**
     * 分页查询性能日志
     *
     * @param page    页码
     * @param size    每页大小
     * @param request 查询条件
     * @return 分页结果
     */
    @Override
    public IPage<SysPerformanceLogDTO> getPerformanceLogPage(int page, int size, SysLogQueryRequest request) {
        // 构建分页对象
        Page<SysPerformanceLogDTO> pageParam = new Page<>(page, size);
        
        // 获取查询参数
        Long userId = request.getUserId();
        String username = request.getUsername();
        String resourceType = request.getResourceType();  // 资源类型，如CPU, MEMORY等
        String logLevel = request.getLogLevel();
        String keyword = request.getKeyword();
        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();
        
        // 执行分页查询
        IPage<SysPerformanceLogDTO> result = sysPerformanceLogMapper.selectPerformanceLogPage(
                pageParam,
                userId,
                username,
                resourceType,
                logLevel,
                keyword,
                startTime,
                endTime
        );
        
        return result;
    }
    
    /**
     * 根据ID获取性能日志详情
     *
     * @param id 日志ID
     * @return 性能日志详情
     */
    @Override
    public SysPerformanceLogDTO getPerformanceLogById(Long id) {
        // 根据ID查询性能日志详情
        return sysPerformanceLogMapper.selectPerformanceLogById(id);
    }
    
    /**
     * 导出性能日志
     *
     * @param request 查询条件
     * @return 导出文件路径
     */
    @Override
    public String exportPerformanceLog(SysLogQueryRequest request) {
        // 实现导出性能日志的逻辑，这里仅为示例
        // 实际应用中，可能需要生成Excel或CSV文件
        logger.info("导出性能日志，查询条件: {}", request);
        
        // 返回导出文件的路径（示例）
        return "/exports/performance_logs_" + System.currentTimeMillis() + ".xlsx";
    }
    
    /**
     * 清理系统数据
     */
    @Override
    @Transactional
    public boolean cleanupSystemData(SysCleanupRequest request, Long operatorId) {
        boolean result = true;
        
        // 根据请求中的参数，执行不同的清理操作
        if (request.isCleanupReports()) {
            result = result && cleanupReports(request.getKeepReportDays(), operatorId);
        }
        
        if (request.isCleanupLogs()) {
            result = result && cleanupLogs(request.getKeepLogDays(), operatorId);
        }
        
        if (request.isCleanupMonitoringData()) {
            result = result && cleanupMonitoringData(request.getKeepMonitoringDays(), operatorId);
        }
        
        if (request.isCleanupTempFiles()) {
            result = result && cleanupTempFiles(request.getTempCleanupPath(), operatorId);
        }
        
        return result;
    }
    
    @Override
    public boolean cleanupReports(int keepDays, Long operatorId) {
        // 实现清理测试报告的逻辑
        logger.info("清理测试报告，保留{}天以内的数据，操作人ID: {}", keepDays, operatorId);
        return true;
    }
    
    @Override
    public boolean cleanupLogs(int keepDays, Long operatorId) {
        boolean result = true;
        LocalDateTime cutoffTime = LocalDateTime.now().minusDays(keepDays);
        
        try {
            // 清理操作日志
            int operationLogCount = sysOperationLogMapper.deleteOperationLogBefore(cutoffTime);
            logger.info("已清理{}条操作日志，保留{}天以内的数据", operationLogCount, keepDays);
            
            // 清理错误日志
            int errorLogCount = sysErrorLogMapper.deleteErrorLogBefore(cutoffTime);
            logger.info("已清理{}条错误日志，保留{}天以内的数据", errorLogCount, keepDays);
            
            // 清理性能日志
            int performanceLogCount = sysPerformanceLogMapper.deletePerformanceLogBefore(cutoffTime);
            logger.info("已清理{}条性能日志，保留{}天以内的数据", performanceLogCount, keepDays);
        } catch (Exception e) {
            logger.error("清理日志数据失败", e);
            result = false;
        }
        
        return result;
    }
    
    @Override
    public boolean cleanupMonitoringData(int keepDays, Long operatorId) {
        // 实现清理监控数据的逻辑
        logger.info("清理监控数据，保留{}天以内的数据，操作人ID: {}", keepDays, operatorId);
        return true;
    }
    
    @Override
    public boolean cleanupTempFiles(String cleanupPath, Long operatorId) {
        // 实现清理临时文件的逻辑
        logger.info("清理临时文件，路径: {}，操作人ID: {}", cleanupPath, operatorId);
        return true;
    }
    
    @Override
    public boolean archiveReports(int archiveDays, String archivePath, Long operatorId) {
        // 实现归档测试报告的逻辑
        logger.info("归档测试报告，归档{}天以前的数据，归档路径: {}，操作人ID: {}", archiveDays, archivePath, operatorId);
        return true;
    }
} 