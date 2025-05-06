package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestStep;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试步骤Mapper接口
 */
@Mapper
public interface UiTestStepMapper {
    /**
     * 创建UI测试步骤
     *
     * @param uiTestStep UI测试步骤对象
     * @return 影响行数
     */
    int insert(UiTestStep uiTestStep);
    
    /**
     * 批量创建UI测试步骤
     *
     * @param steps UI测试步骤列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UiTestStep> steps);
    
    /**
     * 更新UI测试步骤
     *
     * @param uiTestStep UI测试步骤对象
     * @return 影响行数
     */
    int update(UiTestStep uiTestStep);
    
    /**
     * 删除UI测试步骤
     *
     * @param id 步骤ID
     * @return 影响行数
     */
    int delete(@Param("id") Integer id);
    
    /**
     * 删除指定用例的所有步骤
     *
     * @param caseId 用例ID
     * @return 影响行数
     */
    int deleteByCaseId(@Param("caseId") String caseId);
    
    /**
     * 查询指定用例的所有步骤
     *
     * @param caseId 用例ID
     * @return UI测试步骤列表
     */
    List<UiTestStep> selectByCaseId(@Param("caseId") String caseId);
    
    /**
     * 获取指定用例的步骤数量
     *
     * @param caseId 用例ID
     * @return 步骤数量
     */
    int countStepsByCaseId(@Param("caseId") String caseId);
} 