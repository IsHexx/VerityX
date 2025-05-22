package com.example.verityx.service.impl;

import com.example.verityx.dto.UiTestCaseDTO;
import com.example.verityx.dto.UiTestParameterDTO;
import com.example.verityx.dto.UiTestStepDTO;
import com.example.verityx.entity.UiTestCase;
import com.example.verityx.entity.UiTestParameter;
import com.example.verityx.entity.UiTestStep;
import com.example.verityx.mapper.UiTestCaseMapper;
import com.example.verityx.mapper.UiTestParameterMapper;
import com.example.verityx.mapper.UiTestStepMapper;
import com.example.verityx.service.UiTestCaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * UI测试用例服务实现类
 */
@Slf4j
@Service
public class UiTestCaseServiceImpl implements UiTestCaseService {
    private final UiTestCaseMapper uiTestCaseMapper;
    private final UiTestStepMapper uiTestStepMapper;
    private final UiTestParameterMapper uiTestParameterMapper;

    public UiTestCaseServiceImpl(UiTestCaseMapper uiTestCaseMapper, 
                               UiTestStepMapper uiTestStepMapper,
                               UiTestParameterMapper uiTestParameterMapper) {
        this.uiTestCaseMapper = uiTestCaseMapper;
        this.uiTestStepMapper = uiTestStepMapper;
        this.uiTestParameterMapper = uiTestParameterMapper;
    }

    @Override
    @Transactional
    public String createUiTestCase(UiTestCaseDTO uiTestCaseDTO) {
        // 生成用例ID
        String caseId = uiTestCaseMapper.generateCaseId();
        
        // 转换为实体类
        UiTestCase uiTestCase = new UiTestCase();
        BeanUtils.copyProperties(uiTestCaseDTO, uiTestCase);
        
        // 设置默认值
        uiTestCase.setCaseId(caseId);
        uiTestCase.setCaseStatus("未执行");
        uiTestCase.setLastResult("未执行");
        
        // 插入用例
        uiTestCaseMapper.insert(uiTestCase);
        
        // 保存步骤
        if (uiTestCaseDTO.getSteps() != null && !uiTestCaseDTO.getSteps().isEmpty()) {
            List<UiTestStep> steps = new ArrayList<>();
            int stepIndex = 1;
            
            for (UiTestStepDTO stepDTO : uiTestCaseDTO.getSteps()) {
                UiTestStep step = new UiTestStep();
                BeanUtils.copyProperties(stepDTO, step);
                step.setCaseId(caseId);
                step.setStepIndex(stepIndex++);
                steps.add(step);
            }
            
            uiTestStepMapper.batchInsert(steps);
        }
        
        // 保存参数
        if (uiTestCaseDTO.getParameters() != null && !uiTestCaseDTO.getParameters().isEmpty()) {
            List<UiTestParameter> parameters = new ArrayList<>();
            
            for (UiTestParameterDTO paramDTO : uiTestCaseDTO.getParameters()) {
                UiTestParameter parameter = new UiTestParameter();
                parameter.setCaseId(caseId);
                parameter.setParamName(paramDTO.getName());
                parameter.setParamValue(paramDTO.getValue());
                parameters.add(parameter);
            }
            
            uiTestParameterMapper.batchInsert(parameters);
        }
        
        return caseId;
    }

        @Override
    @Transactional
    public boolean updateUiTestCase(UiTestCaseDTO uiTestCaseDTO) {
        String caseId = uiTestCaseDTO.getCaseId();
        Integer projectId = uiTestCaseDTO.getProjectId();
        
        // 检查用例是否存在
        UiTestCase existingCase = uiTestCaseMapper.selectByCaseId(caseId, projectId);
        if (existingCase == null) {
            log.error("UI测试用例不存在: {}, projectId: {}", caseId, projectId);
            return false;
        }
        
        // 更新用例基本信息
        UiTestCase uiTestCase = new UiTestCase();
        BeanUtils.copyProperties(uiTestCaseDTO, uiTestCase);
        uiTestCaseMapper.update(uiTestCase);
        
        // 更新步骤: 先删除后添加
        uiTestStepMapper.deleteByCaseId(caseId);
        if (uiTestCaseDTO.getSteps() != null && !uiTestCaseDTO.getSteps().isEmpty()) {
            List<UiTestStep> steps = new ArrayList<>();
            int stepIndex = 1;
            
            for (UiTestStepDTO stepDTO : uiTestCaseDTO.getSteps()) {
                UiTestStep step = new UiTestStep();
                BeanUtils.copyProperties(stepDTO, step);
                step.setCaseId(caseId);
                step.setStepIndex(stepIndex++);
                steps.add(step);
            }
            
            uiTestStepMapper.batchInsert(steps);
        }
        
        // 更新参数: 先删除后添加
        uiTestParameterMapper.deleteByCaseId(caseId);
        if (uiTestCaseDTO.getParameters() != null && !uiTestCaseDTO.getParameters().isEmpty()) {
            List<UiTestParameter> parameters = new ArrayList<>();
            
            for (UiTestParameterDTO paramDTO : uiTestCaseDTO.getParameters()) {
                UiTestParameter parameter = new UiTestParameter();
                parameter.setCaseId(caseId);
                parameter.setParamName(paramDTO.getName());
                parameter.setParamValue(paramDTO.getValue());
                parameters.add(parameter);
            }
            
            uiTestParameterMapper.batchInsert(parameters);
        }
        
        return true;
    }

        @Override
    @Transactional
    public boolean deleteUiTestCase(String caseId, Integer projectId) {
        // 检查用例是否存在
        UiTestCase existingCase = uiTestCaseMapper.selectByCaseId(caseId, projectId);
        if (existingCase == null) {
            log.error("UI测试用例不存在: {}, projectId: {}", caseId, projectId);
            return false;
        }
        
        // 删除参数
        uiTestParameterMapper.deleteByCaseId(caseId);
        
        // 删除步骤
        uiTestStepMapper.deleteByCaseId(caseId);
        
        // 删除用例
        uiTestCaseMapper.delete(caseId, projectId);
        
        return true;
    }

        @Override
    public UiTestCaseDTO getUiTestCaseDetail(String caseId, Integer projectId) {
        // 获取用例基本信息
        UiTestCase uiTestCase = uiTestCaseMapper.selectByCaseId(caseId, projectId);
        if (uiTestCase == null) {
            log.error("UI测试用例不存在: {}, projectId: {}", caseId, projectId);
            return null;
        }
        
        // 转换为DTO
        UiTestCaseDTO uiTestCaseDTO = new UiTestCaseDTO();
        BeanUtils.copyProperties(uiTestCase, uiTestCaseDTO);
        
        // 获取步骤
        List<UiTestStep> steps = uiTestStepMapper.selectByCaseId(caseId);
        List<UiTestStepDTO> stepDTOs = new ArrayList<>();
        
        for (UiTestStep step : steps) {
            UiTestStepDTO stepDTO = new UiTestStepDTO();
            BeanUtils.copyProperties(step, stepDTO);
            stepDTOs.add(stepDTO);
        }
        
        uiTestCaseDTO.setSteps(stepDTOs);
        
        // 获取参数
        List<UiTestParameter> parameters = uiTestParameterMapper.selectByCaseId(caseId);
        List<UiTestParameterDTO> parameterDTOs = new ArrayList<>();
        
        for (UiTestParameter parameter : parameters) {
            UiTestParameterDTO parameterDTO = new UiTestParameterDTO();
            parameterDTO.setId(parameter.getId());
            parameterDTO.setName(parameter.getParamName());
            parameterDTO.setValue(parameter.getParamValue());
            parameterDTOs.add(parameterDTO);
        }
        
        uiTestCaseDTO.setParameters(parameterDTOs);
        uiTestCaseDTO.setStepCount(steps.size());
        
        return uiTestCaseDTO;
    }

        @Override
    public Map<String, Object> getUiTestCaseList(String keyword, String status, Integer projectId, int page, int pageSize) {
        // 计算偏移量
        int offset = (page - 1) * pageSize;
        
        // 查询列表
        List<UiTestCase> uiTestCases = uiTestCaseMapper.selectByPage(keyword, status, projectId, offset, pageSize);
        
        // 查询总数
        int total = uiTestCaseMapper.countTotal(keyword, status, projectId);
        
        // 转换为DTO
        List<UiTestCaseDTO> uiTestCaseDTOs = new ArrayList<>();
        
        for (UiTestCase uiTestCase : uiTestCases) {
            UiTestCaseDTO uiTestCaseDTO = new UiTestCaseDTO();
            BeanUtils.copyProperties(uiTestCase, uiTestCaseDTO);
            
            // 获取步骤数量
            int stepCount = uiTestStepMapper.countStepsByCaseId(uiTestCase.getCaseId());
            uiTestCaseDTO.setStepCount(stepCount);
            
            uiTestCaseDTOs.add(uiTestCaseDTO);
        }
        
        // 返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("list", uiTestCaseDTOs);
        result.put("total", total);
        
        return result;
    }

        @Override
    @Transactional
    public String executeUiTestCase(String caseId, Integer projectId) {
        // 检查用例是否存在
        UiTestCase existingCase = uiTestCaseMapper.selectByCaseId(caseId, projectId);
        if (existingCase == null) {
            log.error("UI测试用例不存在: {}", caseId);
            return null;
        }
        
        // 更新用例状态为执行中
        uiTestCaseMapper.updateExecutionStatus(caseId, "执行中", "执行中");
        
        // TODO: 实际执行UI测试用例的逻辑
        // 这里只是模拟执行，实际应该异步执行
        
        // 生成执行ID
        String executionId = UUID.randomUUID().toString();
        
        // 模拟执行结果
        // 实际项目中应该是异步更新
        uiTestCaseMapper.updateExecutionStatus(caseId, "已执行", "通过");
        
        return executionId;
    }
} 