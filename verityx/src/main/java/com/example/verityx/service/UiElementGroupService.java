package com.example.verityx.service;

import com.example.verityx.dto.UiElementGroupDTO;

import java.util.List;

/**
 * UI元素分组Service接口
 */
public interface UiElementGroupService {
    
    /**
     * 获取所有UI元素分组
     *
     * @return UI元素分组列表
     */
    List<UiElementGroupDTO> getElementGroups();
    
    /**
     * 根据ID获取UI元素分组
     *
     * @param id UI元素分组ID
     * @return UI元素分组DTO
     */
    UiElementGroupDTO getElementGroupById(Long id);
    
    /**
     * 创建UI元素分组
     *
     * @param uiElementGroupDTO UI元素分组DTO
     * @return 新创建的UI元素分组ID
     */
    Long createElementGroup(UiElementGroupDTO uiElementGroupDTO);
    
    /**
     * 更新UI元素分组
     *
     * @param id UI元素分组ID
     * @param uiElementGroupDTO UI元素分组DTO
     */
    void updateElementGroup(Long id, UiElementGroupDTO uiElementGroupDTO);
    
    /**
     * 删除UI元素分组
     *
     * @param id UI元素分组ID
     * @return 是否成功删除
     */
    boolean deleteElementGroup(Long id);
    
    /**
     * 同步元素分组计数
     *
     * @param groupId 分组ID
     */
    void syncElementCount(Long groupId);
} 