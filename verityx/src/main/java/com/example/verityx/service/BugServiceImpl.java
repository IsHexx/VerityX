package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import com.example.verityx.mapper.BugMapper;
import com.example.verityx.mapper.TestCaseMapper;
import com.example.verityx.mapper.TestPlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private BugMapper bugMapper;

    @Autowired
    private TestCaseMapper testCaseMapper;

    @Autowired
    private TestPlanMapper testPlanMapper;

    @Override
    @Transactional
    public Bug createBug(Bug bug) {
        // 验证关联的测试计划和测试用例是否存在
        if (bug.getPlanId() != null) {
            if (testPlanMapper.selectTestPlanById(bug.getPlanId()) == null) {
                throw new RuntimeException("关联的测试计划不存在");
            }
        }

        if (bug.getCaseId() != null) {
            if (testCaseMapper.selectTestCaseById(bug.getCaseId()) == null) {
                throw new RuntimeException("关联的测试用例不存在");
            }
        }

        bugMapper.insertBug(bug);
        return bug;
    }

    @Override
    public Bug getBugById(int bugId) {
        return bugMapper.selectBugById(bugId);
    }

    @Override
    public List<Bug> getAllBugs() {
        return bugMapper.selectAllBugs();
    }

    @Override
    public List<Bug> getBugsWithPagination(int pageSize, int offset, String status, String keyword, String projectId) {
        System.out.println("====== 分页查询缺陷 ======");
        return bugMapper.selectBugsWithPagination(pageSize, offset, status, keyword, projectId);
    }

    @Override
    public int getBugCount(String status, String keyword, String projectId) {
        return bugMapper.selectBugCount(status, keyword, projectId);
    }

    @Override
    @Transactional
    public boolean updateBug(Bug bug) {
        // 验证关联的测试计划和测试用例是否存在
        if (bug.getPlanId() != null) {
            if (testPlanMapper.selectTestPlanById(bug.getPlanId()) == null) {
                throw new RuntimeException("关联的测试计划不存在");
            }
        }

        if (bug.getCaseId() != null) {
            if (testCaseMapper.selectTestCaseById(bug.getCaseId()) == null) {
                throw new RuntimeException("关联的测试用例不存在");
            }
        }

        return bugMapper.updateBug(bug) > 0;
    }

    @Override
    public boolean deleteBug(int bugId) {
        return bugMapper.deleteBug(bugId) > 0;
    }

    @Override
    public List<Bug> getBugsByPlanId(Integer planId) {
        if (planId == null) {
            return new ArrayList<>();
        }
        return bugMapper.selectBugsByPlanId(planId);
    }

    @Override
    public List<Bug> getBugsByCaseId(Integer caseId) {
        if (caseId == null) {
            return new ArrayList<>();
        }
        return bugMapper.selectBugsByCaseId(caseId);
    }
}
