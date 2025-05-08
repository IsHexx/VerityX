package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysConfigDTO;
import com.example.verityx.dto.request.SysConfigCreateRequest;
import com.example.verityx.dto.request.SysConfigUpdateRequest;

import java.util.List;
import java.util.Map;

/**
 * 系统参数配置服务接口
 */
public interface SysConfigService {
    
    /**
     * 获取系统配置分页列表
     * 
     * @param page 页码
     * @param size 每页大小
     * @param configName 参数名称
     * @param configKey 参数键名
     * @param configType 参数类型
     * @return 分页结果
     */
    IPage<SysConfigDTO> getConfigPage(int page, int size, String configName, String configKey, String configType);
    
    /**
     * 根据ID获取配置详情
     * 
     * @param id 配置ID
     * @return 配置信息
     */
    SysConfigDTO getConfigById(Long id);
    
    /**
     * 根据键名获取配置
     * 
     * @param configKey 配置键名
     * @return 配置信息
     */
    SysConfigDTO getConfigByKey(String configKey);
    
    /**
     * 获取配置值
     * 
     * @param configKey 配置键名
     * @return 配置值
     */
    String getConfigValue(String configKey);
    
    /**
     * 获取配置键值对
     * 
     * @param configKeys 配置键名列表
     * @return 键值对映射
     */
    Map<String, String> getConfigMap(List<String> configKeys);
    
    /**
     * 新增配置
     * 
     * @param request 配置信息
     * @param operator 操作人ID
     * @return 配置ID
     */
    Long createConfig(SysConfigCreateRequest request, Long operator);
    
    /**
     * 更新配置
     * 
     * @param request 配置信息
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean updateConfig(SysConfigUpdateRequest request, Long operator);
    
    /**
     * 批量更新配置
     * 
     * @param configs 配置列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchUpdateConfig(List<SysConfigUpdateRequest> configs, Long operator);
    
    /**
     * 删除配置
     * 
     * @param id 配置ID
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean deleteConfig(Long id, Long operator);
    
    /**
     * 批量删除配置
     * 
     * @param ids 配置ID列表
     * @param operator 操作人ID
     * @return 是否成功
     */
    boolean batchDeleteConfig(List<Long> ids, Long operator);
    
    /**
     * 刷新系统配置缓存
     * 
     * @return 是否成功
     */
    boolean refreshCache();
} 