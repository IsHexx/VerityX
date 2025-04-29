package com.example.verityx.mapper;

import com.example.verityx.entity.ApiEnvironment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ApiEnvironmentMapper {

    int insertEnvironment(ApiEnvironment environment);
    
    ApiEnvironment selectEnvironmentById(Integer id);
    
    List<ApiEnvironment> selectAllEnvironments();
    
    List<ApiEnvironment> selectEnvironmentsByPage(@Param("keyword") String keyword, 
                                                 @Param("offset") int offset, 
                                                 @Param("pageSize") int pageSize);
    
    int countEnvironments(@Param("keyword") String keyword);
    
    int updateEnvironment(ApiEnvironment environment);
    
    int deleteEnvironment(Integer id);
    
    int batchDeleteEnvironments(@Param("ids") List<Integer> ids);
    
    ApiEnvironment selectDefaultEnvironment();
    
    int clearDefaultStatus();
    
    int setDefaultEnvironment(Integer id);
} 