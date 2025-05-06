package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestSuiteCaseRel;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试套件与测试用例关联关系Mapper接口
 */
@Mapper
public interface UiTestSuiteCaseRelMapper {
    
    /**
     * 根据套件ID查询关联关系
     *
     * @param suiteId 套件ID
     * @return 关联关系列表
     */
    List<UiTestSuiteCaseRel> selectBySuiteId(@Param("suiteId") Long suiteId);
    
    /**
     * 根据用例ID查询关联关系
     *
     * @param caseId 用例ID
     * @return 关联关系列表
     */
    List<UiTestSuiteCaseRel> selectByCaseId(@Param("caseId") Long caseId);
    
    /**
     * 批量插入关联关系
     *
     * @param relations 关联关系列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UiTestSuiteCaseRel> relations);
    
    /**
     * 根据套件ID删除关联关系
     *
     * @param suiteId 套件ID
     * @return 影响行数
     */
    int deleteBySuiteId(@Param("suiteId") Long suiteId);
    
    /**
     * 根据用例ID删除关联关系
     *
     * @param caseId 用例ID
     * @return 影响行数
     */
    int deleteByCaseId(@Param("caseId") Long caseId);
} 