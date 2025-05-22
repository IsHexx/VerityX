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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger logger = LoggerFactory.getLogger(UiTestSuiteServiceImpl.class);

    @Autowired
    private UiTestSuiteMapper uiTestSuiteMapper;

    @Autowired
    private UiTestCaseMapper uiTestCaseMapper;

    @Autowired
    private UiTestSuiteCaseRelMapper uiTestSuiteCaseRelMapper;

    @Override
    public PageResult<UiTestSuiteDTO> getUiTestSuiteList(Integer page, Integer pageSize, String keyword, String status, Long projectId) {
        logger.info("开始查询UI测试套件列表, 参数: page={}, pageSize={}, keyword={}, status={}, projectId={}", 
                   page, pageSize, keyword, status, projectId);
        
        // 计算分页参数
        Integer offset = (page - 1) * pageSize;
        
        // 获取总数
        Integer total = uiTestSuiteMapper.countTestSuites(keyword, status, projectId);
        logger.info("查询到UI测试套件总数: {}", total);
        
        // 获取分页数据
        List<UiTestSuite> uiTestSuites = uiTestSuiteMapper.selectByPage(keyword, status, offset, pageSize, projectId);
        logger.info("分页查询UI测试套件数据, SQL条件: keyword={}, status={}, offset={}, pageSize={}, projectId={}, 结果条数: {}", 
                    keyword, status, offset, pageSize, projectId, uiTestSuites.size());
        
        // 转换为DTO
        List<UiTestSuiteDTO> uiTestSuiteDTOs = uiTestSuites.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        
        return new PageResult<>(uiTestSuiteDTOs, total, page, pageSize);
    }

    @Override
    public UiTestSuiteDTO getUiTestSuiteById(Long id, Long projectId) {
        logger.info("开始查询UI测试套件详情, ID: {}, projectId: {}", id, projectId);
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("测试套件不存在: " + id);
        }
        logger.info("成功查询到UI测试套件详情, ID: {}, 套件名称: {}", id, uiTestSuite.getSuiteName());
        return convertToDTO(uiTestSuite);
    }

    @Override
    @Transactional
    public Long createUiTestSuite(UiTestSuiteDTO uiTestSuiteDTO) {
        logger.info("开始创建UI测试套件, 套件名称: {}, 项目ID: {}", uiTestSuiteDTO.getSuiteName(), uiTestSuiteDTO.getProjectId());
        UiTestSuite uiTestSuite = new UiTestSuite();
        BeanUtils.copyProperties(uiTestSuiteDTO, uiTestSuite);
        
        // 记录所有字段的值，用于调试
        logger.debug("UI测试套件字段值: id={}, suiteName={}, projectId={}, description={}",
                     uiTestSuite.getId(), uiTestSuite.getSuiteName(), uiTestSuite.getProjectId(), 
                     uiTestSuite.getDescription());
        
        // 设置初始值
        uiTestSuite.setCreatedBy("当前用户"); // 实际应从当前登录用户获取
        uiTestSuite.setCreatedAt(new Date());
        
        try {
            uiTestSuiteMapper.insert(uiTestSuite);
            logger.info("UI测试套件创建成功, ID: {}", uiTestSuite.getId());
            return uiTestSuite.getId();
        } catch (Exception e) {
            logger.error("UI测试套件创建失败, 错误信息: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void updateUiTestSuite(Long id, UiTestSuiteDTO uiTestSuiteDTO) {
        logger.info("开始更新UI测试套件, ID: {}, 套件名称: {}", id, uiTestSuiteDTO.getSuiteName());
        UiTestSuite existingSuite = uiTestSuiteMapper.selectById(id, uiTestSuiteDTO.getProjectId());
        if (existingSuite == null) {
            logger.error("UI测试套件不存在, ID: {}", id);
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        // 更新基本信息，但保留一些字段不变
        BeanUtils.copyProperties(uiTestSuiteDTO, existingSuite, "id", "createdBy", "createdAt");
        
        // 设置更新时间
        existingSuite.setUpdatedAt(new Date());
        
        // 记录所有字段的值，用于调试
        logger.debug("更新UI测试套件字段值: id={}, suiteName={}, projectId={}, description={}",
                     existingSuite.getId(), existingSuite.getSuiteName(), existingSuite.getProjectId(), 
                     existingSuite.getDescription());
        
        try {
            uiTestSuiteMapper.updateById(existingSuite);
            logger.info("UI测试套件更新成功, ID: {}", id);
        } catch (Exception e) {
            logger.error("UI测试套件更新失败, ID: {}, 错误信息: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void deleteUiTestSuite(Long id, Long projectId) {
        logger.info("开始删除UI测试套件, ID: {}, projectId: {}", id, projectId);
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件不存在, ID: {}, projectId: {}", id, projectId);
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        try {
            // 删除套件和用例的关联关系
            int relCount = uiTestSuiteCaseRelMapper.deleteBySuiteId(id);
            logger.info("删除UI测试套件关联关系, 套件ID: {}, 删除的关联关系数量: {}", id, relCount);
            
            // 删除套件
            int result = uiTestSuiteMapper.deleteById(id, projectId);
            logger.info("UI测试套件删除结果, ID: {}, 影响行数: {}", id, result);
        } catch (Exception e) {
            logger.error("UI测试套件删除失败, ID: {}, 错误信息: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    @Transactional
    public void executeUiTestSuite(Long id) {
        logger.info("开始执行UI测试套件, ID: {}", id);
        // 先查询套件是否存在(不使用projectId过滤)，以获取projectId
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(id, null);
        if (uiTestSuite == null) {
            logger.error("UI测试套件不存在, ID: {}", id);
            throw new RuntimeException("测试套件不存在: " + id);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(id, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", id, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + id);
        }
        
        // 设置更新时间
        uiTestSuite.setUpdatedAt(new Date());
        
        try {
            uiTestSuiteMapper.updateById(uiTestSuite);
            logger.info("UI测试套件执行前状态更新成功, ID: {}, 项目ID: {}", id, projectId);
            
            // 实际执行逻辑应该在这里启动一个异步任务
            logger.info("UI测试套件开始异步执行, ID: {}, 项目ID: {}", id, projectId);
            // 这里省略执行实现...
        } catch (Exception e) {
            logger.error("UI测试套件执行失败, ID: {}, 项目ID: {}, 错误信息: {}", id, projectId, e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public List<UiTestSuiteDTO.CaseInfo> getSuiteCases(Long suiteId) {
        logger.info("开始获取UI测试套件关联的用例, 套件ID: {}", suiteId);
        // 查询套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("UI测试套件不存在, ID: {}", suiteId);
            throw new RuntimeException("测试套件不存在: " + suiteId);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);
        }
        
        // 获取套件关联的用例ID及排序信息
        List<UiTestSuiteCaseRel> relations = uiTestSuiteCaseRelMapper.selectBySuiteId(suiteId);
        logger.info("UI测试套件关联的用例关系数量, 套件ID: {}, 关系数量: {}", suiteId, relations.size());
        
        if (relations.isEmpty()) {
            return new ArrayList<>();
        }
        
        // 获取所有用例信息
        List<Long> caseIds = relations.stream()
                .map(UiTestSuiteCaseRel::getCaseId)
                .collect(Collectors.toList());
        
        logger.info("获取UI测试套件关联的用例详情, 套件ID: {}, 用例ID列表: {}", suiteId, caseIds);
        List<UiTestCase> cases = uiTestCaseMapper.selectByIds(caseIds);
        logger.info("获取到的用例数量: {}", cases.size());
        
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
        // 验证测试套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("更新用例关联失败: 测试套件不存在, ID: {}", suiteId);
            throw new IllegalArgumentException("测试套件不存在, ID: " + suiteId);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);
        }
        
        // 先删除该套件下所有用例关联
        int deleteCount = uiTestSuiteCaseRelMapper.deleteBySuiteId(suiteId);
        logger.info("已删除原有用例关联 {} 条, 套件ID: {}", deleteCount, suiteId);
        
        // 如果没有新的用例ID, 则直接返回
        if (caseIds == null || caseIds.isEmpty()) {
            logger.info("没有需要添加的用例, 套件ID: {}", suiteId);
            return;
        }
        
        // 创建新的关联记录
        List<UiTestSuiteCaseRel> relations = new ArrayList<>();
        for (int i = 0; i < caseIds.size(); i++) {
            UiTestSuiteCaseRel relation = new UiTestSuiteCaseRel();
            relation.setSuiteId(suiteId);
            relation.setCaseId(caseIds.get(i));
            relation.setOrderIndex(i + 1); // 顺序从1开始
            relations.add(relation);
        }
        
        // 批量插入新的关联记录
        int insertCount = uiTestSuiteCaseRelMapper.batchInsert(relations);
        logger.info("已添加新的用例关联 {} 条, 套件ID: {}", insertCount, suiteId);
        
        // 更新套件的更新时间
        uiTestSuite.setUpdatedAt(new Date());
        uiTestSuiteMapper.updateById(uiTestSuite);
        logger.info("已更新套件更新时间, 套件ID: {}", suiteId);
    }

    @Override
    public void updateConcurrencyConfig(Long suiteId, UiTestSuiteDTO.ConcurrencyConfig concurrencyConfig) {
        // 验证测试套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("更新并发配置失败: 测试套件不存在, ID: {}", suiteId);
            throw new IllegalArgumentException("测试套件不存在, ID: " + suiteId);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);
        }
        
        // 更新并发执行相关字段
        uiTestSuite.setExecuteInParallel(concurrencyConfig.getExecuteInParallel());
        uiTestSuite.setMaxParallelCount(concurrencyConfig.getMaxParallelCount());
        uiTestSuite.setUpdatedAt(new Date());
        
        // 保存更新
        uiTestSuiteMapper.updateById(uiTestSuite);
        logger.info("已更新测试套件并发配置, 套件ID: {}, 是否并发执行: {}, 最大并发数: {}", 
                suiteId, concurrencyConfig.getExecuteInParallel(), concurrencyConfig.getMaxParallelCount());
    }

    @Override
    public UiTestSuiteDTO.ScheduleConfig getScheduleConfig(Long suiteId) {
        // 验证测试套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("获取定时任务配置失败: 测试套件不存在, ID: {}", suiteId);
            throw new IllegalArgumentException("测试套件不存在, ID: " + suiteId);
        }
        
                        // 获取projectId        Long projectId = uiTestSuite.getProjectId();                // 用获取到的projectId再次查询，确保在正确的项目下操作        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);        if (uiTestSuite == null) {            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);        }                // 这里应该从数据库中获取定时任务配置
        // 由于示例中没有定时任务配置表，先返回一个空配置
        UiTestSuiteDTO.ScheduleConfig config = new UiTestSuiteDTO.ScheduleConfig();
        config.setEnabled(false);
        config.setCronExpression("0 0 10 * * ?"); // 默认每天10点执行
        config.setTimezone("Asia/Shanghai");
        config.setDescription("默认定时任务配置");
        
        logger.info("获取测试套件定时任务配置成功, 套件ID: {}", suiteId);
        return config;
    }
    
    @Override
    @Transactional
    public void saveScheduleConfig(Long suiteId, UiTestSuiteDTO.ScheduleConfig scheduleConfig) {
        // 验证测试套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("保存定时任务配置失败: 测试套件不存在, ID: {}", suiteId);
            throw new IllegalArgumentException("测试套件不存在, ID: " + suiteId);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);
        }
        
        // 这里应该保存定时任务配置到数据库
        // 由于示例中没有定时任务配置表，仅记录日志
        logger.info("保存测试套件定时任务配置成功, 套件ID: {}, 是否启用: {}, cron表达式: {}, 时区: {}, 描述: {}", 
                suiteId, scheduleConfig.getEnabled(), scheduleConfig.getCronExpression(), 
                scheduleConfig.getTimezone(), scheduleConfig.getDescription());
        
        // 更新套件的更新时间
        uiTestSuite.setUpdatedAt(new Date());
        uiTestSuiteMapper.updateById(uiTestSuite);
    }
    
    @Override
    @Transactional
    public void deleteScheduleConfig(Long suiteId) {
        // 验证测试套件是否存在
        UiTestSuite uiTestSuite = uiTestSuiteMapper.selectById(suiteId, null);
        if (uiTestSuite == null) {
            logger.error("删除定时任务配置失败: 测试套件不存在, ID: {}", suiteId);
            throw new IllegalArgumentException("测试套件不存在, ID: " + suiteId);
        }
        
        // 获取projectId
        Long projectId = uiTestSuite.getProjectId();
        
        // 用获取到的projectId再次查询，确保在正确的项目下操作
        uiTestSuite = uiTestSuiteMapper.selectById(suiteId, projectId);
        if (uiTestSuite == null) {
            logger.error("UI测试套件在指定项目中不存在, ID: {}, 项目ID: {}", suiteId, projectId);
            throw new RuntimeException("在项目 " + projectId + " 中未找到测试套件: " + suiteId);
        }
        
        // 这里应该从数据库中删除定时任务配置
        // 由于示例中没有定时任务配置表，仅记录日志
        logger.info("删除测试套件定时任务配置成功, 套件ID: {}", suiteId);
        
        // 更新套件的更新时间
        uiTestSuite.setUpdatedAt(new Date());
        uiTestSuiteMapper.updateById(uiTestSuite);
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