package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysPerformanceLogDTO;
import com.example.verityx.entity.SysPerformanceLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 系统性能日志Mapper接口
 */
@Mapper
public interface SysPerformanceLogMapper extends BaseMapper<SysPerformanceLog> {
    
    /**
     * 分页查询性能日志
     *
     * @param page          分页参数
     * @param userId        用户ID
     * @param username      用户名
     * @param resourceType  资源类型
     * @param logLevel      日志级别
     * @param keyword       关键词
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return 分页结果
     */
    IPage<SysPerformanceLogDTO> selectPerformanceLogPage(
            Page<SysPerformanceLogDTO> page,
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("resourceType") String resourceType,
            @Param("logLevel") String logLevel,
            @Param("keyword") String keyword,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据ID查询性能日志详情
     *
     * @param id 日志ID
     * @return 性能日志详情
     */
    SysPerformanceLogDTO selectPerformanceLogById(@Param("id") Long id);
    
    /**
     * 清理指定时间之前的性能日志
     *
     * @param beforeTime 时间点
     * @return 清理的记录数
     */
    int deletePerformanceLogBefore(@Param("beforeTime") LocalDateTime beforeTime);
} 