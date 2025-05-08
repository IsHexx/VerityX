package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.SysAbout;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统基本信息Mapper接口
 */
@Mapper
public interface SysAboutMapper extends BaseMapper<SysAbout> {
    
    /**
     * 获取系统基本信息
     * 
     * @return 系统基本信息
     */
    SysAbout selectSysAbout();
}