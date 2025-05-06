package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestParameter;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试参数Mapper接口
 */
@Mapper
public interface UiTestParameterMapper {
    /**
     * 创建UI测试参数
     *
     * @param parameter UI测试参数对象
     * @return 影响行数
     */
    int insert(UiTestParameter parameter);
    
    /**
     * 批量创建UI测试参数
     *
     * @param parameters UI测试参数列表
     * @return 影响行数
     */
    int batchInsert(@Param("list") List<UiTestParameter> parameters);
    
    /**
     * 更新UI测试参数
     *
     * @param parameter UI测试参数对象
     * @return 影响行数
     */
    int update(UiTestParameter parameter);
    
    /**
     * 删除UI测试参数
     *
     * @param id 参数ID
     * @return 影响行数
     */
    int delete(@Param("id") Integer id);
    
    /**
     * 删除指定用例的所有参数
     *
     * @param caseId 用例ID
     * @return 影响行数
     */
    int deleteByCaseId(@Param("caseId") String caseId);
    
    /**
     * 查询指定用例的所有参数
     *
     * @param caseId 用例ID
     * @return UI测试参数列表
     */
    List<UiTestParameter> selectByCaseId(@Param("caseId") String caseId);
} 