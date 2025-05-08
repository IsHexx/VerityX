package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysErrorLogDTO;
import com.example.verityx.entity.SysErrorLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 系统异常日志Mapper接口
 */
@Mapper
public interface SysErrorLogMapper extends BaseMapper<SysErrorLog> {
    
    /**
     * 分页查询异常日志
     *
     * @param page          分页参数
     * @param userId        用户ID
     * @param username      用户名
     * @param requestMethod 请求方法
     * @param keyword       关键词
     * @param startTime     开始时间
     * @param endTime       结束时间
     * @return 分页结果
     */
    IPage<SysErrorLogDTO> selectErrorLogPage(Page<SysErrorLogDTO> page,
                                            @Param("userId") Long userId,
                                            @Param("username") String username,
                                            @Param("requestMethod") String requestMethod,
                                            @Param("keyword") String keyword,
                                            @Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据ID查询异常日志详情
     *
     * @param id 日志ID
     * @return 异常日志详情
     */
    SysErrorLogDTO selectErrorLogById(@Param("id") Long id);
    
    /**
     * 清理指定时间之前的异常日志
     *
     * @param beforeTime 时间点
     * @return 清理的记录数
     */
    int deleteErrorLogBefore(@Param("beforeTime") LocalDateTime beforeTime);
} 