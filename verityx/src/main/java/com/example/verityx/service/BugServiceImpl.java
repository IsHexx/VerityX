package com.example.verityx.service;

import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import com.example.verityx.mapper.BugMapper;
import com.example.verityx.service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BugServiceImpl implements BugService {

    @Autowired
    private BugMapper bugMapper;

    @Override
    public Bug createBug(Bug bug) {
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
    public boolean updateBug(Bug bug) {
        return bugMapper.updateBug(bug) > 0;
    }

    @Override
    public boolean deleteBug(int bugId) {
        return bugMapper.deleteBug(bugId) > 0;
    }
}
