package com.example.verityx.service;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiElementDTO;

import org.springframework.web.multipart.MultipartFile;

/**
 * UI元素Service接口
 */
public interface UiElementService {
    
    /**
     * 分页获取UI元素列表
     *
     * @param page 页码
     * @param pageSize 页大小
     * @param keyword 关键词
     * @param groupId 分组ID
     * @param locatorType 定位器类型
     * @return 分页UI元素列表
     */
    PageResult<UiElementDTO> getElementList(Integer page, Integer pageSize, String keyword, Long groupId, String locatorType);
    
    /**
     * 根据ID获取UI元素
     *
     * @param id UI元素ID
     * @return UI元素DTO
     */
    UiElementDTO getElementById(Long id);
    
    /**
     * 创建UI元素
     *
     * @param uiElementDTO UI元素DTO
     * @return 新创建的UI元素ID
     */
    Long createElement(UiElementDTO uiElementDTO);
    
    /**
     * 更新UI元素
     *
     * @param id UI元素ID
     * @param uiElementDTO UI元素DTO
     */
    void updateElement(Long id, UiElementDTO uiElementDTO);
    
    /**
     * 删除UI元素
     *
     * @param id UI元素ID
     */
    void deleteElement(Long id);
    
    /**
     * 验证元素定位器
     *
     * @param validateRequest 验证请求
     * @return 验证响应
     */
    UiElementDTO.ValidateResponse validateLocator(UiElementDTO.ValidateRequest validateRequest);
    
    /**
     * 上传元素截图
     *
     * @param id 元素ID
     * @param file 截图文件
     * @return 截图路径
     */
    String uploadElementScreenshot(Long id, MultipartFile file);
} 