package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysApiManagementDTO;
import com.example.verityx.entity.SysApiManagement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * API管理Mapper接口
 */
@Mapper
public interface SysApiManagementMapper extends BaseMapper<SysApiManagement> {
    
    /**
     * 分页查询API管理列表
     *
     * @param page        分页参数
     * @param name        API名称
     * @param status      状态
     * @return API列表
     */
    IPage<SysApiManagementDTO> selectApiManagementPage(
            Page<SysApiManagement> page,
            @Param("name") String name,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询API管理详情
     *
     * @param id API ID
     * @return API详情
     */
    SysApiManagementDTO selectApiManagementById(@Param("id") Long id);
} 