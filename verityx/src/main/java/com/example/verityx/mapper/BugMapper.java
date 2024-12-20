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

    List<Bug> selectBugsWithPagination(@Param("pageSize") int limit, @Param("offset") int offset, @Param("status") String  status);

    // 新增获取总记录数方法
    int selectBugCount(String  status);

    // 更新缺陷
    int updateBug(Bug bug);

    // 删除缺陷
    int deleteBug(int bugId);
}
