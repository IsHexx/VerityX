package com.example.verityx.mapper;

import com.example.verityx.entity.ApiManagement;
import java.util.List;

public interface ApiManagementMapper {

    int insertApi(ApiManagement api);

    ApiManagement selectApiById(int apiId);

    List<ApiManagement> selectAllApis();

    int updateApi(ApiManagement api);

    int deleteApi(int apiId);
}
