package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysSsoConfigDTO;
import com.example.verityx.entity.SysSsoConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 单点登录配置Mapper接口
 */
@Mapper
public interface SysSsoConfigMapper extends BaseMapper<SysSsoConfig> {
    
    /**
     * 分页查询单点登录配置列表
     *
     * @param page   分页参数
     * @param name   配置名称
     * @param type   配置类型
     * @param status 状态
     * @return 配置列表
     */
    IPage<SysSsoConfigDTO> selectSsoConfigPage(
            Page<SysSsoConfig> page,
            @Param("name") String name,
            @Param("type") String type,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询单点登录配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    SysSsoConfigDTO selectSsoConfigById(@Param("id") Long id);
} 