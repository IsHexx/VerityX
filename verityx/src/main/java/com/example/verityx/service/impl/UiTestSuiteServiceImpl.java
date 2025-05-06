package com.example.verityx.service.impl;

import com.example.verityx.dto.PageResult;
import com.example.verityx.dto.UiTestSuiteDTO;
import com.example.verityx.entity.UiTestCase;
import com.example.verityx.entity.UiTestSuite;
import com.example.verityx.entity.UiTestSuiteCaseRel;
import com.example.verityx.mapper.UiTestCaseMapper;
import com.example.verityx.mapper.UiTestSuiteCaseRelMapper;
import com.example.verityx.mapper.UiTestSuiteMapper;
import com.example.verityx.service.UiTestSuiteService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * UI测试套件服务实现类
 */
@Service
public class UiTestSuiteServiceImpl implements UiTestSuiteService {

    @Autowired
    private UiTestSuiteMapper uiTestSuiteMapper;

    @Autowired
    private UiTestCaseMapper uiTestCaseMapper;

    @Autowired
    private UiTestSuiteCaseRelMapper uiTestSuiteCaseRelMapper;

    @Override
    public PageResult<UiTestSuiteDTO> getUiTestSuiteList(Integer page, Integer pageSize, String keyword, String status) {
        // 计算分页参数
        Integer offset = (page - 1) * pageSize;
        
        // 获取总数
        Integer total = uiTestSuiteMapper.countTestSuites(keyword, status);
        
        // 获取分页数据
        List<UiTestSuite> uiTestSuites = uiTestSuiteMapper.selectByPage(keyword, status, offset, pageSize);
        
        // 转换为DTO
        List<UiTestSuiteDTO> uiTestSuiteDTOs = uiTestSuites.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(uiTestSuiteDTOs, total, page, pageSize);
    }

    @Override
    public UiTestSuiteDTO getUiTestSuiteById(Long id) {
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id);
        if (uiTestSuite == null) {
            throw new RuntimeException("测试套件不存在: " + id);
        }
        return convertToDTO(uiTestSuite);
    }

    @Override
    @Transactional
    public Long createUiTestSuite(UiTestSuiteDTO uiTestSuiteDTO) {
        UiTestSuite uiTestSuite = new UiTestSuite();
        BeanUtils.copyProperties(uiTestSuiteDTO, uiTestSuite);
        
        // 设置初始状态
        uiTestSuite.setSuiteStatus("未执行");
        uiTestSuite.setLastResult("未执行");
        uiTestSuite.setCreatedBy("当前用户"); // 实际应从当前登录用户获取
        uiTestSuite.setCreatedAt(new Date());
        
        uiTestSuiteMapper.insert(uiTestSuite);
        return uiTestSuite.getId();
    }

    @Override
    @Transactional
    public void updateUiTestSuite(Long id, UiTestSuiteDTO uiTestSuiteDTO) {
        UiTestSuite existingSuite = uiTestSuiteMapper.selectById(id);
        if (existingSuite == null) {
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        // 更新基本信息，但保留一些字段不变
        BeanUtils.copyProperties(uiTestSuiteDTO, existingSuite, "id", "createdBy", "createdAt", "caseCount", "suiteStatus", "lastResult", "lastRunAt");
        
        // 设置更新时间
        existingSuite.setUpdatedAt(new Date());
        
        uiTestSuiteMapper.updateById(existingSuite);
    }

    @Override
    @Transactional
    public void deleteUiTestSuite(Long id) {
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id);
        if (uiTestSuite == null) {
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        // 删除套件和用例的关联关系
        uiTestSuiteCaseRelMapper.deleteBySuiteId(id);
        
        // 删除套件
        uiTestSuiteMapper.deleteById(id);
    }

    @Override
    @Transactional
    public void executeUiTestSuite(Long id) {
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id);
        if (uiTestSuite == null) {
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        // 更新套件状态为运行中
        uiTestSuite.setSuiteStatus("运行中");
        uiTestSuite.setLastResult("执行中");
        uiTestSuite.setLastRunAt(new Date());
        uiTestSuite.setTriggerMode("手动");
        
        uiTestSuiteMapper.updateById(uiTestSuite);
        
        // 实际执行逻辑应该在这里启动一个异步任务
        // 这里省略执行实现...
    }

    @Override
    public List<UiTestSuiteDTO.CaseInfo> getSuiteCases(Long suiteId) {
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId);
        if (uiTestSuite == null) {
            throw new RuntimeException("测试套件不存在: " + suiteId);
        }
        
        // 获取套件关联的用例ID及排序信息
        List<UiTestSuiteCaseRel> relations = uiTestSuiteCaseRelMapper.selectBySuiteId(suiteId);
        if (relations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有用例信息
        List<Long> caseIds = relations.stream()
                .map(UiTestSuiteCaseRel::getCaseId)
                .collect(Collectors.toList());
        
        List<UiTestCase> cases = uiTestCaseMapper.selectByIds(caseIds);
        
        // 转换为DTO并设置排序索引
        return cases.stream().map(testCase -> {
            UiTestSuiteDTO.CaseInfo caseInfo = new UiTestSuiteDTO.CaseInfo();
            caseInfo.setCaseId(testCase.getId());
            caseInfo.setCaseTitle(testCase.getCaseTitle());
            caseInfo.setImportanceLevel(testCase.getImportanceLevel());
            
            // 查找关系表中的排序索引
            UiTestSuiteCaseRel relation = relations.stream()
                    .filter(r -> r.getCaseId().equals(testCase.getId()))
                    .findFirst()
                    .orElse(null);
            
            if (relation != null) {
                caseInfo.setOrderIndex(relation.getOrderIndex());
            } else {
                caseInfo.setOrderIndex(0);
            }
            
            return caseInfo;
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void updateSuiteCases(Long suiteId, List<Long> caseIds) {
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId);
        if (uiTestSuite == null) {
            throw new RuntimeException("测试套件不存在: " + suiteId);
        }
        
        // 删除现有关联关系
        uiTestSuiteCaseRelMapper.deleteBySuiteId(suiteId);
        
        // 创建新的关联关系
        if (caseIds != null && !caseIds.isEmpty()) {
            List<UiTestSuiteCaseRel> relations = new ArrayList<>();
            for (int i = 0; i < caseIds.size(); i++) {
                UiTestSuiteCaseRel relation = new UiTestSuiteCaseRel();
                relation.setSuiteId(suiteId);
                relation.setCaseId(caseIds.get(i));
                relation.setOrderIndex(i + 1);
                relations.add(relation);
            }
            
            // 批量插入关联关系
            uiTestSuiteCaseRelMapper.batchInsert(relations);
            
            // 更新套件的用例数量
            uiTestSuite.setCaseCount(caseIds.size());
            uiTestSuiteMapper.updateById(uiTestSuite);
        } else {
            // 如果没有用例，将用例数量设为0
            uiTestSuite.setCaseCount(0);
            uiTestSuiteMapper.updateById(uiTestSuite);
        }
    }

    /**
     * 将实体对象转换为DTO对象
     *
     * @param uiTestSuite 实体对象
     * @return DTO对象
     */
    private UiTestSuiteDTO convertToDTO(UiTestSuite uiTestSuite) {
        if (uiTestSuite == null) {
            return null;
        }
        
        UiTestSuiteDTO dto = new UiTestSuiteDTO();
        BeanUtils.copyProperties(uiTestSuite, dto);
        return dto;
    }
} 