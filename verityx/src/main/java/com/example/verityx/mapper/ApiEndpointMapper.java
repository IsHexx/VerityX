package com.example.verityx.mapper;

import com.example.verityx.entity.ApiEndpoint;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口信息Mapper接口
 */
public interface ApiEndpointMapper {

    /**
     * 插入接口
     */
    int insertEndpoint(ApiEndpoint endpoint);

    /**
     * 根据ID查询接口
     */
    ApiEndpoint selectEndpointById(int id);

    /**
     * 查询模块下的所有接口
     */
    List<ApiEndpoint> selectEndpointsByModuleId(@Param("moduleId") int moduleId);

    /**
     * 查询项目下的所有接口
     */
    List<ApiEndpoint> selectEndpointsByProjectId(@Param("projectId") String projectId);

    /**
     * 分页查询接口
     */
    List<ApiEndpoint> selectEndpointsWithPagination(
        @Param("pageSize") int limit, 
        @Param("offset") int offset, 
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 统计接口数量
     */
    int selectEndpointCount(
        @Param("keyword") String keyword,
        @Param("projectId") String projectId
    );

    /**
     * 更新接口
     */
    int updateEndpoint(ApiEndpoint endpoint);

    /**
     * 删除接口
     */
    int deleteEndpoint(int id);

    /**
     * 删除模块下的所有接口
     */
    int deleteEndpointsByModuleId(@Param("moduleId") int moduleId);
}
