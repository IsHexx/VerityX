package com.example.verityx.mapper;

import com.example.verityx.entity.UiElementGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI元素分组Mapper接口
 */
@Mapper
public interface UiElementGroupMapper {
    
    /**
     * 插入UI元素分组
     *
     * @param uiElementGroup UI元素分组对象
     * @return 影响的行数
     */
    int insert(UiElementGroup uiElementGroup);
    
    /**
     * 根据ID更新UI元素分组
     *
     * @param uiElementGroup UI元素分组对象
     * @return 影响的行数
     */
    int updateById(UiElementGroup uiElementGroup);
    
    /**
     * 根据ID删除UI元素分组
     *
     * @param id UI元素分组ID
     * @param projectId 项目ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id, @Param("projectId") Integer projectId);
    
    /**
     * 根据ID查询UI元素分组
     *
     * @param id UI元素分组ID
     * @param projectId 项目ID
     * @return UI元素分组对象
     */
    UiElementGroup selectById(@Param("id") Long id, @Param("projectId") Integer projectId);
    
    /**
     * 查询所有UI元素分组
     *
     * @param projectId 项目ID
     * @return UI元素分组列表
     */
    List<UiElementGroup> selectAll(@Param("projectId") Integer projectId);
    
    /**
     * 更新元素数量
     *
     * @param id 分组ID
     * @param count 元素数量
     * @return 影响的行数
     */
    int updateElementCount(@Param("id") Long id, @Param("count") int count);
} 