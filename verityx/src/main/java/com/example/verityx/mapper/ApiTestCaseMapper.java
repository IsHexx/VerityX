package com.example.verityx.mapper;

import com.example.verityx.entity.ApiTestCase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口测试用例Mapper接口
 */
public interface ApiTestCaseMapper {

    /**
     * 插入测试用例
     */
    int insertTestCase(ApiTestCase testCase);

    /**
     * 根据ID查询测试用例
     */
    ApiTestCase selectTestCaseById(int id);

    /**
     * 查询接口下的所有测试用例
     */
    List<ApiTestCase> selectTestCasesByApiId(@Param("apiId") int apiId);

    /**
     * 更新测试用例
     */
    int updateTestCase(ApiTestCase testCase);

    /**
     * 删除测试用例
     */
    int deleteTestCase(int id);

    /**
     * 删除接口下的所有测试用例
     */
    int deleteTestCasesByApiId(@Param("apiId") int apiId);
}
