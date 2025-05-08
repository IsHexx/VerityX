package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysMonitorDataDTO;
import com.example.verityx.entity.SysMonitorData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 系统监控数据Mapper接口
 */
@Mapper
public interface SysMonitorDataMapper extends BaseMapper<SysMonitorData> {
    
    /**
     * 分页查询监控数据
     *
     * @param page        分页参数
     * @param monitorType 监控类型
     * @param status      状态
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 分页结果
     */
    IPage<SysMonitorDataDTO> selectMonitorDataPage(Page<SysMonitorDataDTO> page,
                                                  @Param("monitorType") String monitorType,
                                                  @Param("status") String status,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
    
    /**
     * 根据监控类型查询最新的监控数据
     *
     * @param monitorType 监控类型
     * @return 监控数据列表
     */
    List<SysMonitorDataDTO> selectLatestMonitorDataByType(@Param("monitorType") String monitorType);
    
    /**
     * 根据监控类型和时间范围查询监控数据，用于趋势图
     *
     * @param monitorType 监控类型
     * @param metricName  指标名称
     * @param startTime   开始时间
     * @param endTime     结束时间
     * @return 监控数据列表
     */
    List<SysMonitorDataDTO> selectMonitorDataTrend(@Param("monitorType") String monitorType,
                                                  @Param("metricName") String metricName,
                                                  @Param("startTime") LocalDateTime startTime,
                                                  @Param("endTime") LocalDateTime endTime);
    
    /**
     * 清理指定时间之前的监控数据
     *
     * @param beforeTime 时间点
     * @return 清理的记录数
     */
    int deleteMonitorDataBefore(@Param("beforeTime") LocalDateTime beforeTime);
} 