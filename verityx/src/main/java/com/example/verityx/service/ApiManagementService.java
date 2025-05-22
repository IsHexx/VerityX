package com.example.verityx.service;

import com.example.verityx.entity.ApiManagement;
import java.util.List;

public interface ApiManagementService {

    ApiManagement createApi(ApiManagement api);

    ApiManagement getApiById(int apiId);

    List<ApiManagement> getAllApis(String projectId);

    List<ApiManagement> getApisWithPagination(int pageSize, int offset, String keyword, String projectId);

    int getApiCount(String keyword, String projectId);

    boolean updateApi(ApiManagement api);

    boolean deleteApi(int apiId);
}
