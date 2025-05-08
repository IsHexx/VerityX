package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.verityx.dto.SysNotificationTemplateDTO;
import com.example.verityx.dto.request.SysNotificationTemplateRequest;
import com.example.verityx.entity.SysNotificationTemplate;
import com.example.verityx.mapper.SysNotificationTemplateMapper;
import com.example.verityx.service.SysNotificationTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 通知模板服务实现类
 */
@Slf4j
@Service
public class SysNotificationTemplateServiceImpl extends ServiceImpl<SysNotificationTemplateMapper, SysNotificationTemplate> implements SysNotificationTemplateService {

    @Autowired
    private SysNotificationTemplateMapper templateMapper;

    @Override
    public IPage<SysNotificationTemplateDTO> getTemplatePage(int page, int size, String name, String code, String type, Boolean status) {
        Page<SysNotificationTemplate> pageParam = new Page<>(page, size);
        return templateMapper.selectTemplatePage(pageParam, name, code, type, status);
    }

    @Override
    public SysNotificationTemplateDTO getTemplateById(Long id) {
        return templateMapper.selectTemplateById(id);
    }

    @Override
    public SysNotificationTemplateDTO getTemplateByCode(String code) {
        return templateMapper.selectTemplateByCode(code);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createTemplate(SysNotificationTemplateRequest request, Long operatorId) {
        // 检查模板编码是否已存在
        SysNotificationTemplateDTO existTemplate = templateMapper.selectTemplateByCode(request.getCode());
        if (existTemplate != null) {
            throw new RuntimeException("模板编码已存在");
        }

        SysNotificationTemplate template = new SysNotificationTemplate();
        BeanUtils.copyProperties(request, template);
        template.setCreatorId(operatorId);
        template.setUpdaterId(operatorId);
        
        save(template);
        return template.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplate(SysNotificationTemplateRequest request, Long operatorId) {
        if (request.getId() == null) {
            throw new RuntimeException("模板ID不能为空");
        }
        
        SysNotificationTemplate template = getById(request.getId());
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        
        // 如果修改了编码，需要检查新编码是否已存在
        if (!template.getCode().equals(request.getCode())) {
            SysNotificationTemplateDTO existTemplate = templateMapper.selectTemplateByCode(request.getCode());
            if (existTemplate != null) {
                throw new RuntimeException("模板编码已存在");
            }
        }
        
        BeanUtils.copyProperties(request, template);
        template.setUpdaterId(operatorId);
        
        return updateById(template);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTemplate(Long id, Long operatorId) {
        // 检查是否有通知规则引用了该模板
        // 实际项目中应该检查通知规则表是否有使用该模板的记录
        // 如果有引用，应该抛出异常或返回false，禁止删除
        
        return removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTemplate(List<Long> ids, Long operatorId) {
        // 检查是否有通知规则引用了这些模板
        // 实际项目中应该检查通知规则表是否有使用这些模板的记录
        // 如果有引用，应该抛出异常或返回false，禁止删除
        
        return removeByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTemplateStatus(Long id, Boolean status, Long operatorId) {
        SysNotificationTemplate template = getById(id);
        if (template == null) {
            throw new RuntimeException("模板不存在");
        }
        
        template.setStatus(status);
        template.setUpdaterId(operatorId);
        return updateById(template);
    }
} 