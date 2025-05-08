package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.util.Result;
import com.example.verityx.dto.SysMonitorDataDTO;
import com.example.verityx.dto.SysOperationLogDTO;
import com.example.verityx.dto.SysErrorLogDTO;
import com.example.verityx.dto.SysPerformanceLogDTO;
import com.example.verityx.dto.request.SysLogQueryRequest;
import com.example.verityx.dto.request.SysCleanupRequest;
import com.example.verityx.service.SysMonitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 系统监控控制器
 */
@RestController
@RequestMapping("/api/system/monitor")
public class SysMonitorController {
    
    @Autowired
    private SysMonitorService sysMonitorService;
    
    /**
     * 获取系统状态概览
     */
    @GetMapping("/status")
    @PreAuthorize("hasAuthority('system:monitor:view')")
    public Result<Map<String, Object>> getSystemStatus() {
        return Result.success(sysMonitorService.getSystemStatus());
    }
    
    /**
     * 获取服务器状态指标
     */
    @GetMapping("/server/metrics")
    @PreAuthorize("hasAuthority('system:monitor:view')")
    public Result<List<SysMonitorDataDTO>> getServerMetrics() {
        return Result.success(sysMonitorService.getServerMetrics());
    }
    
    /**
     * 获取数据库状态指标
     */
    @GetMapping("/database/metrics")
    @PreAuthorize("hasAuthority('system:monitor:view')")
    public Result<List<SysMonitorDataDTO>> getDatabaseMetrics() {
        return Result.success(sysMonitorService.getDatabaseMetrics());
    }
    
    /**
     * 获取测试引擎状态指标
     */
    @GetMapping("/test-engine/metrics")
    @PreAuthorize("hasAuthority('system:monitor:view')")
    public Result<List<SysMonitorDataDTO>> getTestEngineMetrics() {
        return Result.success(sysMonitorService.getTestEngineMetrics());
    }
    
    /**
     * 获取系统资源使用趋势
     */
    @GetMapping("/resource/trend")
    @PreAuthorize("hasAuthority('system:monitor:view')")
    public Result<List<SysMonitorDataDTO>> getResourceTrend(
            @RequestParam String metricsType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(sysMonitorService.getResourceTrend(metricsType, startTime, endTime));
    }
    
    /**
     * 分页查询操作日志
     */
    @GetMapping("/logs/operation")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<IPage<SysOperationLogDTO>> getOperationLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            SysLogQueryRequest request) {
        return Result.success(sysMonitorService.getOperationLogPage(page, size, request));
    }
    
    /**
     * 获取操作日志详情
     */
    @GetMapping("/logs/operation/{id}")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<SysOperationLogDTO> getOperationLogById(@PathVariable Long id) {
        return Result.success(sysMonitorService.getOperationLogById(id));
    }
    
    /**
     * 导出操作日志
     */
    @PostMapping("/logs/operation/export")
    @PreAuthorize("hasAuthority('system:log:export')")
    public Result<String> exportOperationLog(@RequestBody SysLogQueryRequest request) {
        return Result.success(sysMonitorService.exportOperationLog(request));
    }
    
    /**
     * 分页查询异常日志
     */
    @GetMapping("/logs/error")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<IPage<SysErrorLogDTO>> getErrorLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            SysLogQueryRequest request) {
        return Result.success(sysMonitorService.getErrorLogPage(page, size, request));
    }
    
    /**
     * 获取异常日志详情
     */
    @GetMapping("/logs/error/{id}")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<SysErrorLogDTO> getErrorLogById(@PathVariable Long id) {
        return Result.success(sysMonitorService.getErrorLogById(id));
    }
    
    /**
     * 导出异常日志
     */
    @PostMapping("/logs/error/export")
    @PreAuthorize("hasAuthority('system:log:export')")
    public Result<String> exportErrorLog(@RequestBody SysLogQueryRequest request) {
        return Result.success(sysMonitorService.exportErrorLog(request));
    }
    
    /**
     * 分页查询性能日志
     */
    @GetMapping("/logs/performance")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<IPage<SysPerformanceLogDTO>> getPerformanceLogPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            SysLogQueryRequest request) {
        return Result.success(sysMonitorService.getPerformanceLogPage(page, size, request));
    }
    
    /**
     * 获取性能日志详情
     */
    @GetMapping("/logs/performance/{id}")
    @PreAuthorize("hasAuthority('system:log:view')")
    public Result<SysPerformanceLogDTO> getPerformanceLogById(@PathVariable Long id) {
        return Result.success(sysMonitorService.getPerformanceLogById(id));
    }
    
    /**
     * 导出性能日志
     */
    @PostMapping("/logs/performance/export")
    @PreAuthorize("hasAuthority('system:log:export')")
    public Result<String> exportPerformanceLog(@RequestBody SysLogQueryRequest request) {
        return Result.success(sysMonitorService.exportPerformanceLog(request));
    }
    
    /**
     * 清理系统数据
     */
    @PostMapping("/cleanup")
    @PreAuthorize("hasAuthority('system:cleanup:execute')")
    public Result<Boolean> cleanupSystemData(@RequestBody @Valid SysCleanupRequest request) {
        // 实际应用中应该从SecurityContext获取当前用户ID
        Long operatorId = 1L; // 假设当前用户ID为1
        return Result.success(sysMonitorService.cleanupSystemData(request, operatorId));
    }
} 