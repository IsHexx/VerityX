package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysSsoConfigDTO;
import com.example.verityx.dto.request.SysSsoConfigRequest;

import java.util.List;

/**
 * 单点登录配置服务接口
 */
public interface SysSsoConfigService {

    /**
     * 分页查询单点登录配置列表
     *
     * @param page   页码
     * @param size   每页大小
     * @param name   配置名称
     * @param type   配置类型
     * @param status 状态
     * @return 分页数据
     */
    IPage<SysSsoConfigDTO> getSsoConfigPage(int page, int size, String name, String type, Boolean status);

    /**
     * 根据ID查询单点登录配置详情
     *
     * @param id 配置ID
     * @return 配置详情
     */
    SysSsoConfigDTO getSsoConfigById(Long id);

    /**
     * 创建单点登录配置
     *
     * @param request    配置请求对象
     * @param operatorId 操作人ID
     * @return 新增配置ID
     */
    Long createSsoConfig(SysSsoConfigRequest request, Long operatorId);

    /**
     * 更新单点登录配置
     *
     * @param request    配置请求对象
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateSsoConfig(SysSsoConfigRequest request, Long operatorId);

    /**
     * 删除单点登录配置
     *
     * @param id         配置ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean deleteSsoConfig(Long id, Long operatorId);

    /**
     * 批量删除单点登录配置
     *
     * @param ids        配置ID列表
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteSsoConfig(List<Long> ids, Long operatorId);

    /**
     * 修改单点登录配置状态
     *
     * @param id         配置ID
     * @param status     状态
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean updateSsoConfigStatus(Long id, Boolean status, Long operatorId);

    /**
     * 设置默认单点登录配置
     *
     * @param id         配置ID
     * @param operatorId 操作人ID
     * @return 是否成功
     */
    boolean setDefaultSsoConfig(Long id, Long operatorId);

    /**
     * 测试单点登录配置连接
     *
     * @param id 配置ID
     * @return 测试结果
     */
    boolean testSsoConfigConnection(Long id);
} 