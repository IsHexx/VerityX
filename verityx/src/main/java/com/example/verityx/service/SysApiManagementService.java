package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysApiManagementDTO;
import com.example.verityx.dto.request.SysApiManagementRequest;

import java.util.List;

/**
 * API管理服务接口
 */
public interface SysApiManagementService {

    /**
     * 分页查询API管理列表
     *
     * @param page     页码
     * @param size     每页大小
     * @param name     API名称
     * @param status   状态
     * @return 分页数据
     */
    IPage<SysApiManagementDTO> getApiManagementPage(int page, int size, String name, Boolean status);

    /**
     * 根据ID查询API管理详情
     *
     * @param id API ID
     * @return API详情
     */
    SysApiManagementDTO getApiManagementById(Long id);

    /**
     * 创建API管理
     *
     * @param request    API管理请求对象
     * @param operatorId 操作人ID
     * @return 新增API ID
     */
    Long createApiManagement(SysApiManagementRequest request, Long operatorId);

    /**
     * 更新API管理
     *
     * @param request    API管理请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateApiManagement(SysApiManagementRequest request, Long operatorId);

    /**
     * 删除API管理
     *
     * @param id         API ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteApiManagement(Long id, Long operatorId);

    /**
     * 批量删除API管理
     *
     * @param ids        API ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteApiManagement(List<Long> ids, Long operatorId);

    /**
     * 修改API管理状态
     *
     * @param id         API ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateApiManagementStatus(Long id, Boolean status, Long operatorId);

    /**
     * 重新生成API密钥
     *
     * @param id         API ID
     * @param operatorId 操作人ID
     * @return 新生成的API密钥
     */
    String regenerateApiKey(Long id, Long operatorId);
} 