package com.example.verityx.mapper;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface BugMapper {

    // 插入缺陷
    int insertBug(Bug bug);

    // 根据 bugId 查询缺陷
    Bug selectBugById(int bugId);

    // 查询所有缺陷
    List<Bug> selectAllBugs();

    List<Bug> selectBugsWithPagination(
        @Param("pageSize") int limit,
        @Param("offset") int offset,
        @Param("status") String status,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    // 新增获取总记录数方法
    int selectBugCount(
        @Param("status") String status,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    // 更新缺陷
    int updateBug(Bug bug);

    // 删除缺陷
    int deleteBug(int bugId);

    // 根据测试计划ID查询缺陷
    List<Bug> selectBugsByPlanId(@Param("planId") Integer planId);

    // 根据测试用例ID查询缺陷
    List<Bug> selectBugsByCaseId(@Param("caseId") Integer caseId);
}
