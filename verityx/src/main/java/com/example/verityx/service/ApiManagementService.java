package com.example.verityx.service;

import com.example.verityx.entity.ApiManagement;
import java.util.List;

public interface ApiManagementService {

    // 通用方法
    ApiManagement createApi(ApiManagement api);

    ApiManagement getApiById(int apiId);

    List<ApiManagement> getAllApis(String projectId);

    List<ApiManagement> getApisWithPagination(int pageSize, int offset, String keyword, String projectId);

    int getApiCount(String keyword, String projectId);

    boolean updateApi(ApiManagement api);

    boolean deleteApi(int apiId);

    // 新增：目录管理
    ApiManagement createDirectory(String directoryName, String projectId, String description);

    boolean deleteDirectory(int directoryId);

    List<ApiManagement> getDirectories(String projectId);

    // 新增：层级查询
    List<ApiManagement> getItemsByType(String itemType, String projectId);

    List<ApiManagement> getItemsByParentId(Integer parentId, String projectId);

    // 新增：树形结构查询
    List<ApiManagement> getTreeStructure(String projectId);
}
