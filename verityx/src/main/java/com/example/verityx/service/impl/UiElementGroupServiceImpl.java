package com.example.verityx.service.impl;

import com.example.verityx.dto.UiElementGroupDTO;
import com.example.verityx.entity.UiElementGroup;
import com.example.verityx.mapper.UiElementGroupMapper;
import com.example.verityx.mapper.UiElementMapper;
import com.example.verityx.service.UiElementGroupService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI元素分组Service实现
 */
@Service
public class UiElementGroupServiceImpl implements UiElementGroupService {

    private static final Logger logger = LoggerFactory.getLogger(UiElementGroupServiceImpl.class);

    @Autowired
    private UiElementGroupMapper uiElementGroupMapper;

    @Autowired
    private UiElementMapper uiElementMapper;

    @Override
    public List<UiElementGroupDTO> getElementGroups() {
        List<UiElementGroup> groups = uiElementGroupMapper.selectAll();
        
        return groups.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UiElementGroupDTO getElementGroupById(Long id) {
        UiElementGroup group = uiElementGroupMapper.selectById(id);
        if (group == null) {
            logger.error("UI元素分组不存在, ID: {}", id);
            throw new RuntimeException("UI元素分组不存在: " + id);
        }
        
        return convertToDTO(group);
    }

    @Override
    @Transactional
    public Long createElementGroup(UiElementGroupDTO uiElementGroupDTO) {
        UiElementGroup group = new UiElementGroup();
        BeanUtils.copyProperties(uiElementGroupDTO, group);
        
        // 设置初始元素数量为0
        group.setElementCount(0);
        
        // 设置创建信息
        group.setCreatedBy("当前用户"); // 实际应当从当前登录用户获取
        group.setCreatedAt(new Date());
        
        uiElementGroupMapper.insert(group);
        
        return group.getId();
    }

    @Override
    @Transactional
    public void updateElementGroup(Long id, UiElementGroupDTO uiElementGroupDTO) {
        UiElementGroup existingGroup = uiElementGroupMapper.selectById(id);
        if (existingGroup == null) {
            logger.error("UI元素分组不存在, ID: {}", id);
            throw new RuntimeException("UI元素分组不存在: " + id);
        }
        
        // 复制属性
        BeanUtils.copyProperties(uiElementGroupDTO, existingGroup, "id", "elementCount", "createdBy", "createdAt");
        
        // 设置更新时间
        existingGroup.setUpdatedAt(new Date());
        
        uiElementGroupMapper.updateById(existingGroup);
    }

    @Override
    @Transactional
    public boolean deleteElementGroup(Long id) {
        UiElementGroup group = uiElementGroupMapper.selectById(id);
        if (group == null) {
            logger.error("UI元素分组不存在, ID: {}", id);
            throw new RuntimeException("UI元素分组不存在: " + id);
        }
        
        // 检查分组内是否有元素
        int elementCount = uiElementMapper.countByGroupId(id);
        if (elementCount > 0) {
            logger.warn("无法删除分组: 分组内还有{}个元素, ID: {}", elementCount, id);
            return false;
        }
        
        uiElementGroupMapper.deleteById(id);
        return true;
    }

    @Override
    @Transactional
    public void syncElementCount(Long groupId) {
        if (groupId == null) {
            return;
        }
        
        UiElementGroup group = uiElementGroupMapper.selectById(groupId);
        if (group == null) {
            logger.error("UI元素分组不存在, ID: {}", groupId);
            return;
        }
        
        // 获取分组中的元素数量
        int count = uiElementMapper.countByGroupId(groupId);
        
        // 更新分组中的元素数量
        uiElementGroupMapper.updateElementCount(groupId, count);
        
        logger.info("已同步元素分组计数, 分组ID: {}, 元素数量: {}", groupId, count);
    }

    /**
     * 将实体对象转换为DTO
     */
    private UiElementGroupDTO convertToDTO(UiElementGroup group) {
        if (group == null) {
            return null;
        }
        
        UiElementGroupDTO dto = new UiElementGroupDTO();
        BeanUtils.copyProperties(group, dto);
        
        return dto;
    }
} 