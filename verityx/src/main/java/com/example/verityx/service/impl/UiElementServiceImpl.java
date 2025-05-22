package com.example.verityx.service.impl;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiElementDTO;
import com.example.verityx.entity.UiElement;
import com.example.verityx.entity.UiElementGroup;
import com.example.verityx.mapper.UiElementGroupMapper;
import com.example.verityx.mapper.UiElementMapper;
import com.example.verityx.service.UiElementGroupService;
import com.example.verityx.service.UiElementService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * UI元素Service实现
 */
@Service
public class UiElementServiceImpl implements UiElementService {

    private static final Logger logger = LoggerFactory.getLogger(UiElementServiceImpl.class);

    @Autowired
    private UiElementMapper uiElementMapper;

    @Autowired
    private UiElementGroupMapper uiElementGroupMapper;
    
    @Autowired
    private UiElementGroupService uiElementGroupService;
    
    @Value("${ui.element.screenshot.path:/upload/screenshots}")
    private String screenshotPath;

    @Override
    public PageResult<UiElementDTO> getElementList(Integer page, Integer pageSize, String keyword, Long groupId, String locatorType, Integer projectId) {
        // 参数校验
        if (page == null || page < 1) {
            page = 1;
        }
        if (pageSize == null || pageSize < 1) {
            pageSize = 10;
        }
        
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询数据
        List<UiElement> uiElements = uiElementMapper.selectByPage(offset, pageSize, keyword, groupId, locatorType, projectId);
        
        // 转换为DTO
        List<UiElementDTO> elementDTOs = uiElements.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        // 填充分组名称
        fillGroupNames(elementDTOs);
        
        // 获取总记录数
        int total = uiElementMapper.countTotal(keyword, groupId, locatorType, projectId);
        
        // 构建结果
        PageResult<UiElementDTO> result = new PageResult<>();
        result.setList(elementDTOs);
        result.setTotal(total);
        result.setPage(page);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public UiElementDTO getElementById(Long id, Integer projectId) {
        UiElement uiElement = uiElementMapper.selectById(id, projectId);
        if (uiElement == null) {
            logger.error("UI元素不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("UI元素不存在: " + id);
        }
        
        UiElementDTO dto = convertToDTO(uiElement);
        
        // 填充分组名称
        List<UiElementDTO> dtos = new ArrayList<>();
        dtos.add(dto);
        fillGroupNames(dtos);
        
        return dto;
    }

    @Override
    @Transactional
    public Long createElement(UiElementDTO uiElementDTO) {
        UiElement uiElement = new UiElement();
        BeanUtils.copyProperties(uiElementDTO, uiElement);
        
        // ID映射
        uiElement.setId(uiElementDTO.getElementId());
        
        // 设置创建信息
        uiElement.setCreatedBy("当前用户"); // 实际应当从当前登录用户获取
        uiElement.setCreatedAt(new Date());
        
        uiElementMapper.insert(uiElement);
        
        // 更新分组元素计数
        if (uiElement.getGroupId() != null) {
            uiElementGroupService.syncElementCount(uiElement.getGroupId());
        }
        
        return uiElement.getId();
    }

    @Override
    @Transactional
    public void updateElement(Long id, UiElementDTO uiElementDTO) {
        Integer projectId = uiElementDTO.getProjectId();
        UiElement existingElement = uiElementMapper.selectById(id, projectId);
        if (existingElement == null) {
            logger.error("UI元素不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("UI元素不存在: " + id);
        }
        
        // 保存旧的分组ID，用于更新元素计数
        Long oldGroupId = existingElement.getGroupId();
        
        // 复制属性
        BeanUtils.copyProperties(uiElementDTO, existingElement, "id", "createdBy", "createdAt");
        
        // ID映射
        existingElement.setId(id);
        
        // 设置更新时间
        existingElement.setUpdatedAt(new Date());
        
        uiElementMapper.updateById(existingElement);
        
        // 如果分组ID有变化，更新两个分组的元素计数
        if ((oldGroupId == null && existingElement.getGroupId() != null) || 
            (oldGroupId != null && !oldGroupId.equals(existingElement.getGroupId()))) {
            
            if (oldGroupId != null) {
                uiElementGroupService.syncElementCount(oldGroupId);
            }
            
            if (existingElement.getGroupId() != null) {
                uiElementGroupService.syncElementCount(existingElement.getGroupId());
            }
        }
    }

    @Override
    @Transactional
    public void deleteElement(Long id, Integer projectId) {
        UiElement uiElement = uiElementMapper.selectById(id, projectId);
        if (uiElement == null) {
            logger.error("UI元素不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("UI元素不存在: " + id);
        }
        
        // 保存分组ID，用于更新元素计数
        Long groupId = uiElement.getGroupId();
        
        // 删除元素
        uiElementMapper.deleteById(id, projectId);
        
        // 删除截图文件
        if (StringUtils.hasText(uiElement.getScreenshotPath())) {
            try {
                Path path = Paths.get(uiElement.getScreenshotPath());
                Files.deleteIfExists(path);
            } catch (IOException e) {
                logger.warn("删除元素截图失败: {}", e.getMessage());
            }
        }
        
        // 更新分组元素计数
        if (groupId != null) {
            uiElementGroupService.syncElementCount(groupId);
        }
    }

    @Override
    public UiElementDTO.ValidateResponse validateLocator(UiElementDTO.ValidateRequest validateRequest) {
        // 实际项目中，这里应该调用Selenium或其他自动化工具来验证定位器
        // 这里使用模拟实现
        UiElementDTO.ValidateResponse response = new UiElementDTO.ValidateResponse();
        
        // 简单判断定位器值是否有效
        boolean isValid = validateRequest.getLocatorValue() != null && 
                          !validateRequest.getLocatorValue().trim().isEmpty();
        
        response.setValid(isValid);
        if (isValid) {
            response.setMessage("定位器验证成功");
            // 模拟返回一个Base64编码的截图数据
            response.setScreenshotBase64("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAA..."); // 实际应返回真实截图
        } else {
            response.setMessage("定位器验证失败：值不能为空");
        }
        
        return response;
    }

    @Override
    public String uploadElementScreenshot(Long id, MultipartFile file, Integer projectId) {
        if (file.isEmpty()) {
            throw new RuntimeException("上传的文件为空");
        }
        
        UiElement uiElement = uiElementMapper.selectById(id, projectId);
        if (uiElement == null) {
            logger.error("UI元素不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("UI元素不存在: " + id);
        }
        
        // 创建上传目录
        File uploadDir = new File(screenshotPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        
        // 生成文件名
        String fileName = "element_" + id + "_" + UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
        String filePath = screenshotPath + File.separator + fileName;
        
        try {
            // 保存文件
            file.transferTo(new File(filePath));
            
            // 更新数据库
            uiElementMapper.updateScreenshotPath(id, filePath, projectId);
            
            return filePath;
        } catch (IOException e) {
            logger.error("保存截图失败: {}", e.getMessage());
            throw new RuntimeException("保存截图失败: " + e.getMessage());
        }
    }

    /**
     * 将实体对象转换为DTO
     */
    private UiElementDTO convertToDTO(UiElement uiElement) {
        if (uiElement == null) {
            return null;
        }
        
        UiElementDTO dto = new UiElementDTO();
        BeanUtils.copyProperties(uiElement, dto);
        
        // ID映射
        dto.setElementId(uiElement.getId());
        
        return dto;
    }
    
    /**
     * 为元素DTO列表填充分组名称
     */
    private void fillGroupNames(List<UiElementDTO> elementDTOs) {
        if (elementDTOs == null || elementDTOs.isEmpty()) {
            return;
        }
        
        // 从第一个元素获取projectId
        Integer projectId = elementDTOs.get(0).getProjectId();
        
        // 获取所有分组
        List<UiElementGroup> groups = uiElementGroupMapper.selectAll(projectId);
        
        // 创建分组ID到名称的映射
        java.util.Map<Long, String> groupMap = groups.stream()
                .collect(Collectors.toMap(UiElementGroup::getId, UiElementGroup::getGroupName));
        
        // 填充分组名称
        for (UiElementDTO dto : elementDTOs) {
            if (dto.getGroupId() != null) {
                dto.setGroupName(groupMap.get(dto.getGroupId()));
            }
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        if (filename == null) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0) {
            return "";
        }
        return filename.substring(dotIndex);
    }
} 