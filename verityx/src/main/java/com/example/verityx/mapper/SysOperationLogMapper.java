package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysOperationLogDTO;
import com.example.verityx.entity.SysOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 系统操作日志Mapper接口
 */
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {
    
    /**
     * 分页查询操作日志
     *
     * @param page          分页参数
     * @param userId        用户ID
     * @param username      用户名
     * @param logType       日志类型
     * @param logLevel      日志级别
     * @param module        操作模块
     * @param keyword       关键词
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return 分页结果
     */
    IPage<SysOperationLogDTO> selectOperationLogPage(
            Page<SysOperationLogDTO> page,
            @Param("userId") Long userId,
            @Param("username") String username,
            @Param("logType") String logType,
            @Param("logLevel") String logLevel,
            @Param("module") String module,
            @Param("keyword") String keyword,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据ID查询操作日志详情
     *
     * @param id 日志ID
     * @return 操作日志详情
     */
    SysOperationLogDTO selectOperationLogById(@Param("id") Long id);
    
    /**
     * 清理指定时间之前的操作日志
     *
     * @param beforeTime 时间点
     * @return 清理的记录数
     */
    int deleteOperationLogBefore(@Param("beforeTime") LocalDateTime beforeTime);
} 