package com.example.verityx.service;

import com.example.verityx.entity.ApiManagement;
import com.example.verityx.mapper.ApiManagementMapper;
import com.example.verityx.service.ApiManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiManagementServiceImpl implements ApiManagementService {

    @Autowired
    private ApiManagementMapper apiManagementMapper;

    @Override
    public ApiManagement createApi(ApiManagement api) {
        apiManagementMapper.insertApi(api);
        return api;
    }

    @Override
    public ApiManagement getApiById(int apiId) {
        return apiManagementMapper.selectApiById(apiId);
    }

    @Override
    public List<ApiManagement> getAllApis() {
        return apiManagementMapper.selectAllApis();
    }

    @Override
    public boolean updateApi(ApiManagement api) {
        return apiManagementMapper.updateApi(api) > 0;
    }

    @Override
    public boolean deleteApi(int apiId) {
        return apiManagementMapper.deleteApi(apiId) > 0;
    }
}
