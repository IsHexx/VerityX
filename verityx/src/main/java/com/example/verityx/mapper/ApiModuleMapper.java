package com.example.verityx.mapper;

import com.example.verityx.entity.ApiModule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 接口模块Mapper接口
 */
public interface ApiModuleMapper {

    /**
     * 插入模块
     */
    int insertModule(ApiModule module);

    /**
     * 根据ID查询模块
     */
    ApiModule selectModuleById(int id);

    /**
     * 查询项目下的所有模块
     */
    List<ApiModule> selectModulesByProjectId(@Param("projectId") String projectId);

    /**
     * 更新模块
     */
    int updateModule(ApiModule module);

    /**
     * 删除模块
     */
    int deleteModule(int id);

    /**
     * 检查模块名是否存在
     */
    int countByNameAndProjectId(@Param("name") String name, @Param("projectId") String projectId);

    /**
     * 获取模块下的接口数量
     */
    int countApisByModuleId(@Param("moduleId") int moduleId);
}
