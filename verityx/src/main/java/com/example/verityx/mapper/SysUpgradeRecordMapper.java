package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.entity.SysUpgradeRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统升级记录Mapper接口
 */
@Mapper
public interface SysUpgradeRecordMapper extends BaseMapper<SysUpgradeRecord> {
    
    /**
     * 分页查询升级记录
     * 
     * @param page 分页参数
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysUpgradeRecord> selectUpgradeRecordPage(Page<?> page, @Param("status") String status);
    
    /**
     * 获取最近一次升级记录
     * 
     * @return 最近一次升级记录
     */
    SysUpgradeRecord selectLatestUpgradeRecord();
}