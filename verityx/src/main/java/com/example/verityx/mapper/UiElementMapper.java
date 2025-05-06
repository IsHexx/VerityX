package com.example.verityx.mapper;

import com.example.verityx.entity.UiElement;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UI元素Mapper接口
 */
@Mapper
public interface UiElementMapper {
    
    /**
     * 插入UI元素
     *
     * @param uiElement UI元素对象
     * @return 影响的行数
     */
    int insert(UiElement uiElement);
    
    /**
     * 根据ID更新UI元素
     *
     * @param uiElement UI元素对象
     * @return 影响的行数
     */
    int updateById(UiElement uiElement);
    
    /**
     * 根据ID删除UI元素
     *
     * @param id UI元素ID
     * @return 影响的行数
     */
    int deleteById(@Param("id") Long id);
    
    /**
     * 根据ID查询UI元素
     *
     * @param id UI元素ID
     * @return UI元素对象
     */
    UiElement selectById(@Param("id") Long id);
    
    /**
     * 查询UI元素总数
     *
     * @param keyword 关键词
     * @param groupId 分组ID
     * @param locatorType 定位器类型
     * @return UI元素总数
     */
    int countElements(@Param("keyword") String keyword, 
                      @Param("groupId") Long groupId,
                      @Param("locatorType") String locatorType);
    
    /**
     * 分页查询UI元素
     *
     * @param keyword 关键词
     * @param groupId 分组ID
     * @param locatorType 定位器类型
     * @param offset 偏移量
     * @param pageSize 页大小
     * @return UI元素列表
     */
    List<UiElement> selectByPage(@Param("keyword") String keyword, 
                                 @Param("groupId") Long groupId,
                                 @Param("locatorType") String locatorType,
                                 @Param("offset") int offset, 
                                 @Param("pageSize") int pageSize);
    
    /**
     * 根据分组ID查询元素数量
     *
     * @param groupId 分组ID
     * @return 元素数量
     */
    int countByGroupId(@Param("groupId") Long groupId);
    
    /**
     * 更新元素截图路径
     *
     * @param id 元素ID
     * @param screenshotPath 截图路径
     * @return 影响的行数
     */
    int updateScreenshotPath(@Param("id") Long id, @Param("screenshotPath") String screenshotPath);
} 