package com.example.verityx.service;

import com.example.verityx.entity.ApiManagement;
import com.example.verityx.mapper.ApiManagementMapper;
import com.example.verityx.service.ApiManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ApiManagementServiceImpl implements ApiManagementService {

    @Autowired
    private ApiManagementMapper apiManagementMapper;

    @Override
    public ApiManagement createApi(ApiManagement api) {
        // 设置创建时间和更新时间
        LocalDateTime now = LocalDateTime.now();
        api.setCreatedAt(now);
        api.setUpdatedAt(now);

        // 确保必要字段不为空
        if (api.getProjectId() == null) {
            throw new RuntimeException("项目ID不能为空");
        }
        if (api.getApiDirectory() == null || api.getApiDirectory().trim().isEmpty()) {
            throw new RuntimeException("接口目录不能为空");
        }
        if (api.getApiName() == null || api.getApiName().trim().isEmpty()) {
            throw new RuntimeException("接口名称不能为空");
        }
        if (api.getRequestMethod() == null || api.getRequestMethod().trim().isEmpty()) {
            throw new RuntimeException("请求方法不能为空");
        }
        if (api.getApiPath() == null || api.getApiPath().trim().isEmpty()) {
            throw new RuntimeException("接口路径不能为空");
        }
        if (api.getStatus() == null || api.getStatus().trim().isEmpty()) {
            throw new RuntimeException("接口状态不能为空");
        }

        apiManagementMapper.insertApi(api);
        return api;
    }

    @Override
    public ApiManagement getApiById(int apiId) {
        return apiManagementMapper.selectApiById(apiId);
    }

    @Override
    public List<ApiManagement> getAllApis(String projectId) {
        return apiManagementMapper.selectAllApis(projectId);
    }
    
    @Override
    public List<ApiManagement> getApisWithPagination(int pageSize, int offset, String keyword, String projectId) {
        System.out.println("====== 分页查询接口 ======");
        return apiManagementMapper.selectApisWithPagination(pageSize, offset, keyword, projectId);
    }

    @Override
    public int getApiCount(String keyword, String projectId) {
        return apiManagementMapper.selectApiCount(keyword, projectId);
    }

    @Override
    public boolean updateApi(ApiManagement api) {
        // 设置更新时间
        api.setUpdatedAt(LocalDateTime.now());

        System.out.println("====== 更新接口Service ======");
        System.out.println("接口ID: " + api.getApiId());
        System.out.println("接口名称: " + api.getApiName());
        System.out.println("更新时间: " + api.getUpdatedAt());

        return apiManagementMapper.updateApi(api) > 0;
    }

    @Override
    public boolean deleteApi(int apiId) {
        return apiManagementMapper.deleteApi(apiId) > 0;
    }

    @Override
    public ApiManagement createDirectory(String directoryName, String projectId, String description) {
        ApiManagement directory = new ApiManagement();
        directory.setItemType("directory");
        directory.setParentId(null); // 目录的父级为null
        directory.setApiName(directoryName);
        directory.setStatus("active");
        directory.setProjectId(Integer.parseInt(projectId));
        directory.setDescription(description);
        directory.setCreatedAt(LocalDateTime.now());
        directory.setUpdatedAt(LocalDateTime.now());

        int result = apiManagementMapper.insertDirectory(directory);
        return result > 0 ? directory : null;
    }

    @Override
    public boolean deleteDirectory(int directoryId) {
        // 级联删除：先删除目录下的所有子项，再删除目录本身

        // 1. 删除目录下的所有子项（接口和用例）
        int deletedChildren = apiManagementMapper.deleteByParentId(directoryId);
        System.out.println("删除了 " + deletedChildren + " 个子项");

        // 2. 删除目录本身
        boolean directoryDeleted = apiManagementMapper.deleteApi(directoryId) > 0;

        return directoryDeleted;
    }

    @Override
    public List<ApiManagement> getDirectories(String projectId) {
        return apiManagementMapper.selectByType("directory", projectId);
    }

    @Override
    public List<ApiManagement> getItemsByType(String itemType, String projectId) {
        return apiManagementMapper.selectByType(itemType, projectId);
    }

    @Override
    public List<ApiManagement> getItemsByParentId(Integer parentId, String projectId) {
        return apiManagementMapper.selectByParentId(parentId, projectId);
    }

    @Override
    public List<ApiManagement> getTreeStructure(String projectId) {
        // 获取所有项目
        List<ApiManagement> allItems = apiManagementMapper.selectAllApis(projectId);

        // 这里可以在Service层构建树形结构，或者直接返回所有项目让前端处理
        return allItems;
    }
}
