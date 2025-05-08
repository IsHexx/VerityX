package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.verityx.entity.SysLicense;
import org.apache.ibatis.annotations.Mapper;

/**
 * 系统许可证信息Mapper接口
 */
@Mapper
public interface SysLicenseMapper extends BaseMapper<SysLicense> {
    
    /**
     * 获取当前生效的许可证信息
     * 
     * @return 当前生效的许可证信息
     */
    SysLicense selectActiveLicense();
} 