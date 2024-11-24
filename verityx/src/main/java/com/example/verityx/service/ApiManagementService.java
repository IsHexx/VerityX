package com.example.verityx.service;

import com.example.verityx.entity.ApiManagement;
import java.util.List;

public interface ApiManagementService {

    ApiManagement createApi(ApiManagement api);

    ApiManagement getApiById(int apiId);

    List<ApiManagement> getAllApis();

    boolean updateApi(ApiManagement api);

    boolean deleteApi(int apiId);
}
