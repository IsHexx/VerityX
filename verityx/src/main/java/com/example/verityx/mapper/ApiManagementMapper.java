package com.example.verityx.mapper;

import com.example.verityx.entity.ApiManagement;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ApiManagementMapper {

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
}
