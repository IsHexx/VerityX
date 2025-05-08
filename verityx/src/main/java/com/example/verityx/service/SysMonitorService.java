package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysMonitorDataDTO;
import com.example.verityx.dto.SysOperationLogDTO;
import com.example.verityx.dto.SysErrorLogDTO;
import com.example.verityx.dto.SysPerformanceLogDTO;
import com.example.verityx.dto.request.SysLogQueryRequest;
import com.example.verityx.dto.request.SysCleanupRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统监控服务接口
 */
public interface SysMonitorService {
    
    /**
     * 获取系统状态概览
     *
     * @return 系统状态数据
     */
    Map<String, Object> getSystemStatus();
    
    /**
     * 获取服务器状态指标
     *
     * @return 服务器状态指标
     */
    List<SysMonitorDataDTO> getServerMetrics();
    
    /**
     * 获取数据库状态指标
     *
     * @return 数据库状态指标
     */
    List<SysMonitorDataDTO> getDatabaseMetrics();
    
    /**
     * 获取测试执行引擎状态指标
     *
     * @return 测试执行引擎状态指标
     */
    List<SysMonitorDataDTO> getTestEngineMetrics();
    
    /**
     * 获取系统资源使用趋势
     *
     * @param metricsType 指标类型（cpu, memory, disk等）
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 系统资源使用趋势数据
     */
    List<SysMonitorDataDTO> getResourceTrend(String metricsType, LocalDateTime startTime, LocalDateTime endTime);
    
    /**
     * 分页查询操作日志
     *
     * @param page     页码
     * @param size     每页大小
     * @param request  查询参数
     * @return 操作日志分页结果
     */
    IPage<SysOperationLogDTO> getOperationLogPage(int page, int size, SysLogQueryRequest request);
    
    /**
     * 获取操作日志详情
     *
     * @param id 日志ID
     * @return 操作日志详情
     */
    SysOperationLogDTO getOperationLogById(Long id);
    
    /**
     * 导出操作日志
     *
     * @param request 查询参数
     * @return 文件下载地址
     */
    String exportOperationLog(SysLogQueryRequest request);
    
    /**
     * 分页查询异常日志
     *
     * @param page     页码
     * @param size     每页大小
     * @param request  查询参数
     * @return 异常日志分页结果
     */
    IPage<SysErrorLogDTO> getErrorLogPage(int page, int size, SysLogQueryRequest request);
    
    /**
     * 获取异常日志详情
     *
     * @param id 日志ID
     * @return 异常日志详情
     */
    SysErrorLogDTO getErrorLogById(Long id);
    
    /**
     * 导出异常日志
     *
     * @param request 查询参数
     * @return 文件下载地址
     */
    String exportErrorLog(SysLogQueryRequest request);
    
    /**
     * 分页查询性能日志
     *
     * @param page 页码
     * @param size 每页大小
     * @param request 查询条件
     * @return 分页结果
     */
    IPage<SysPerformanceLogDTO> getPerformanceLogPage(int page, int size, SysLogQueryRequest request);
    
    /**
     * 根据ID获取性能日志详情
     *
     * @param id 日志ID
     * @return 性能日志详情
     */
    SysPerformanceLogDTO getPerformanceLogById(Long id);
    
    /**
     * 导出性能日志
     *
     * @param request 查询条件
     * @return 导出文件路径
     */
    String exportPerformanceLog(SysLogQueryRequest request);
    
    /**
     * 清理系统数据
     *
     * @param request 清理参数
     * @param operatorId 操作人ID
     * @return 清理结果
     */
    boolean cleanupSystemData(SysCleanupRequest request, Long operatorId);
    
    /**
     * 清理测试报告
     *
     * @param keepDays 保留天数
     * @param operatorId 操作人ID
     * @return 清理结果
     */
    boolean cleanupReports(int keepDays, Long operatorId);
    
    /**
     * 清理日志数据
     *
     * @param keepDays 保留天数
     * @param operatorId 操作人ID
     * @return 清理结果
     */
    boolean cleanupLogs(int keepDays, Long operatorId);
    
    /**
     * 清理监控数据
     *
     * @param keepDays 保留天数
     * @param operatorId 操作人ID
     * @return 清理结果
     */
    boolean cleanupMonitoringData(int keepDays, Long operatorId);
    
    /**
     * 清理临时文件
     *
     * @param cleanupPath 清理路径
     * @param operatorId 操作人ID
     * @return 清理结果
     */
    boolean cleanupTempFiles(String cleanupPath, Long operatorId);
    
    /**
     * 归档测试报告
     *
     * @param archiveDays 归档阈值天数
     * @param archivePath 归档路径
     * @param operatorId 操作人ID
     * @return 归档结果
     */
    boolean archiveReports(int archiveDays, String archivePath, Long operatorId);
} 