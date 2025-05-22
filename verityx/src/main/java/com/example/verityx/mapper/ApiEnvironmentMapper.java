package com.example.verityx.mapper;

import com.example.verityx.entity.ApiEnvironment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiEnvironmentMapper {

    int insertEnvironment(ApiEnvironment environment);
    
    ApiEnvironment selectEnvironmentById(Integer id);
    
    List<ApiEnvironment> selectAllEnvironments(@Param("projectId") String projectId);
    
    List<ApiEnvironment> selectEnvironmentsByPage(@Param("keyword") String keyword, 
                                                 @Param("projectId") String projectId,
                                                 @Param("offset") int offset, 
                                                 @Param("pageSize") int pageSize);
    
    int countEnvironments(@Param("keyword") String keyword, 
                          @Param("projectId") String projectId);
    
    int updateEnvironment(ApiEnvironment environment);
    
    int deleteEnvironment(Integer id);
    
    int batchDeleteEnvironments(@Param("ids") List<Integer> ids);
    
    ApiEnvironment selectDefaultEnvironment(@Param("projectId") String projectId);
    
    int clearDefaultStatus(@Param("projectId") String projectId);
    
    int setDefaultEnvironment(Integer id);
} 