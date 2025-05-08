package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysToolIntegrationDTO;
import com.example.verityx.entity.SysToolIntegration;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 第三方工具集成Mapper接口
 */
@Mapper
public interface SysToolIntegrationMapper extends BaseMapper<SysToolIntegration> {
    
    /**
     * 分页查询第三方工具集成列表
     *
     * @param page   分页参数
     * @param name   集成名称
     * @param type   集成类型
     * @param status 状态
     * @return 集成列表
     */
    IPage<SysToolIntegrationDTO> selectToolIntegrationPage(
            Page<SysToolIntegration> page,
            @Param("name") String name,
            @Param("type") String type,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询第三方工具集成详情
     *
     * @param id 集成ID
     * @return 集成详情
     */
    SysToolIntegrationDTO selectToolIntegrationById(@Param("id") Long id);
} 