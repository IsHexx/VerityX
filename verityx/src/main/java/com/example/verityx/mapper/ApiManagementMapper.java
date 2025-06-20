package com.example.verityx.mapper;

import com.example.verityx.entity.ApiManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiManagementMapper {

    // 通用方法
    int insertApi(ApiManagement api);

    ApiManagement selectApiById(int apiId);

    List<ApiManagement> selectAllApis(@Param("projectId") String projectId);

    List<ApiManagement> selectApisWithPagination(
        @Param("pageSize") int limit,
        @Param("offset") int offset,
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    int selectApiCount(
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    int updateApi(ApiManagement api);

    int deleteApi(int apiId);

    // 新增：按类型查询
    List<ApiManagement> selectByType(@Param("itemType") String itemType, @Param("projectId") String projectId);

    // 新增：按父级ID查询
    List<ApiManagement> selectByParentId(@Param("parentId") Integer parentId, @Param("projectId") String projectId);

    // 新增：查询目录下是否有子项
    int countChildrenByParentId(@Param("parentId") Integer parentId);

    // 新增：创建目录
    int insertDirectory(ApiManagement directory);

    // 新增：删除目录及其所有子项
    int deleteByParentId(@Param("parentId") Integer parentId);
}
