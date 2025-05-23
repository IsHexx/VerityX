package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;

import java.util.List;

public interface BugService {

    Bug createBug(Bug bug);

    Bug getBugById(int bugId);

    List<Bug> getAllBugs();

    List<Bug> getBugsWithPagination(int pageSize, int offset, String status, String keyword, String projectId);

    int getBugCount(String status, String keyword, String projectId);

    boolean updateBug(Bug bug);

    boolean deleteBug(int bugId);

    /**
     * 根据测试计划ID查询缺陷
     *
     * @param planId 测试计划ID
     * @return 缺陷列表
     */
    List<Bug> getBugsByPlanId(Integer planId);

    /**
     * 根据测试用例ID查询缺陷
     *
     * @param caseId 测试用例ID
     * @return 缺陷列表
     */
    List<Bug> getBugsByCaseId(Integer caseId);
}
