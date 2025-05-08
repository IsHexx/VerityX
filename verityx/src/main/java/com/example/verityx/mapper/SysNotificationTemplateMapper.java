package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysNotificationTemplateDTO;
import com.example.verityx.entity.SysNotificationTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 通知模板Mapper接口
 */
@Mapper
public interface SysNotificationTemplateMapper extends BaseMapper<SysNotificationTemplate> {
    
    /**
     * 分页查询通知模板列表
     *
     * @param page   分页参数
     * @param name   模板名称
     * @param code   模板编码
     * @param type   模板类型
     * @param status 状态
     * @return 模板列表
     */
    IPage<SysNotificationTemplateDTO> selectTemplatePage(
            Page<SysNotificationTemplate> page,
            @Param("name") String name,
            @Param("code") String code,
            @Param("type") String type,
            @Param("status") Boolean status);
    
    /**
     * 根据ID查询通知模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    SysNotificationTemplateDTO selectTemplateById(@Param("id") Long id);
    
    /**
     * 根据模板编码查询模板
     *
     * @param code 模板编码
     * @return 模板详情
     */
    SysNotificationTemplateDTO selectTemplateByCode(@Param("code") String code);
} 