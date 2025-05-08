package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.SysVersionInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统版本信息Mapper接口
 */
@Mapper
public interface SysVersionInfoMapper extends BaseMapper<SysVersionInfo> {
    
    /**
     * 获取当前版本信息
     * 
     * @return 当前版本信息
     */
    SysVersionInfo selectCurrentVersion();
    
    /**
     * 将所有版本设置为非当前版本
     * 
     * @return 影响行数
     */
    int resetCurrentVersion();
    
    /**
     * 设置指定版本为当前版本
     * 
     * @param id 版本ID
     * @return 影响行数
     */
    int setCurrentVersion(@Param("id") Long id);
} 