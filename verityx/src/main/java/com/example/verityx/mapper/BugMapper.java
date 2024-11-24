package com.example.verityx.mapper;

import com.example.verityx.entity.Bug;

import java.util.List;

public interface BugMapper {

    // 插入缺陷
    int insertBug(Bug bug);

    // 根据 bugId 查询缺陷
    Bug selectBugById(int bugId);

    // 查询所有缺陷
    List<Bug> selectAllBugs();

    // 更新缺陷
    int updateBug(Bug bug);

    // 删除缺陷
    int deleteBug(int bugId);
}
