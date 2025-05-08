package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysNotificationTemplateDTO;
import com.example.verityx.dto.request.SysNotificationTemplateRequest;

import java.util.List;

/**
 * 通知模板服务接口
 */
public interface SysNotificationTemplateService {

    /**
     * 分页查询通知模板列表
     *
     * @param page   页码
     * @param size   每页大小
     * @param name   模板名称
     * @param code   模板编码
     * @param type   模板类型
     * @param status 状态
     * @return 分页数据
     */
    IPage<SysNotificationTemplateDTO> getTemplatePage(int page, int size, String name, String code, String type, Boolean status);

    /**
     * 根据ID查询通知模板详情
     *
     * @param id 模板ID
     * @return 模板详情
     */
    SysNotificationTemplateDTO getTemplateById(Long id);

    /**
     * 根据编码查询通知模板
     *
     * @param code 模板编码
     * @return 模板详情
     */
    SysNotificationTemplateDTO getTemplateByCode(String code);

    /**
     * 创建通知模板
     *
     * @param request    模板请求对象
     * @param operatorId 操作人ID
     * @return 新增模板ID
     */
    Long createTemplate(SysNotificationTemplateRequest request, Long operatorId);

    /**
     * 更新通知模板
     *
     * @param request    模板请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateTemplate(SysNotificationTemplateRequest request, Long operatorId);

    /**
     * 删除通知模板
     *
     * @param id         模板ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteTemplate(Long id, Long operatorId);

    /**
     * 批量删除通知模板
     *
     * @param ids        模板ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteTemplate(List<Long> ids, Long operatorId);

    /**
     * 修改通知模板状态
     *
     * @param id         模板ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateTemplateStatus(Long id, Boolean status, Long operatorId);
} 