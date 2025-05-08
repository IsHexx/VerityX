package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysToolIntegrationDTO;
import com.example.verityx.dto.request.SysToolIntegrationRequest;

import java.util.List;

/**
 * 第三方工具集成服务接口
 */
public interface SysToolIntegrationService {

    /**
     * 分页查询第三方工具集成列表
     *
     * @param page   页码
     * @param size   每页大小
     * @param name   集成名称
     * @param type   集成类型
     * @param status 状态
     * @return 分页数据
     */
    IPage<SysToolIntegrationDTO> getToolIntegrationPage(int page, int size, String name, String type, Boolean status);

    /**
     * 根据ID查询第三方工具集成详情
     *
     * @param id 集成ID
     * @return 集成详情
     */
    SysToolIntegrationDTO getToolIntegrationById(Long id);

    /**
     * 创建第三方工具集成
     *
     * @param request    集成请求对象
     * @param operatorId 操作人ID
     * @return 新增集成ID
     */
    Long createToolIntegration(SysToolIntegrationRequest request, Long operatorId);

    /**
     * 更新第三方工具集成
     *
     * @param request    集成请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateToolIntegration(SysToolIntegrationRequest request, Long operatorId);

    /**
     * 删除第三方工具集成
     *
     * @param id         集成ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteToolIntegration(Long id, Long operatorId);

    /**
     * 批量删除第三方工具集成
     *
     * @param ids        集成ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteToolIntegration(List<Long> ids, Long operatorId);

    /**
     * 修改第三方工具集成状态
     *
     * @param id         集成ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateToolIntegrationStatus(Long id, Boolean status, Long operatorId);

    /**
     * 测试第三方工具连接
     *
     * @param id 集成ID
     * @return 测试结果
     */
    boolean testToolIntegrationConnection(Long id);
} 