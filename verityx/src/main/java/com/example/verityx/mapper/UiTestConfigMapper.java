package com.example.verityx.mapper;

import com.example.verityx.entity.UiTestConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI测试配置Mapper接口
 */
@Mapper
public interface UiTestConfigMapper {

    /**
     * 创建UI测试配置
     * @param config UI测试配置对象
     * @return 影响的行数
     */
    int insert(UiTestConfig config);

    /**
     * 更新UI测试配置
     * @param config UI测试配置对象
     * @return 影响的行数
     */
    int update(UiTestConfig config);

    /**
     * 根据ID查询UI测试配置
     * @param id 配置ID
     * @return UI测试配置对象
     */
    UiTestConfig selectById(@Param("id") Long id);

    /**
     * 获取配置列表
     * @param configType 配置类型
     * @param configName 配置名称（模糊查询）
     * @param projectId 项目ID
     * @return UI测试配置列表
     */
    List<UiTestConfig> selectList(@Param("configType") String configType,
                                 @Param("configName") String configName,
                                 @Param("projectId") Integer projectId);

    /**
     * 根据ID删除UI测试配置
     * @param id 配置ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);

    /**
     * 获取默认配置
     * @param configType 配置类型
     * @param projectId 项目ID（可选）
     * @return 默认配置对象
     */
    UiTestConfig selectDefaultConfig(@Param("configType") String configType, @Param("projectId") Integer projectId);

    /**
     * 根据配置类型统计数量
     * @param configType 配置类型
     * @return 配置数量
     */
    int countByType(@Param("configType") String configType);

    /**
     * 取消所有默认配置
     * @param configType 配置类型
     * @return 影响的行数
     */
    int resetDefaultConfig(@Param("configType") String configType);
}