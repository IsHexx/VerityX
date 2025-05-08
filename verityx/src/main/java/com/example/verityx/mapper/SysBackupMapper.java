package com.example.verityx.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysBackupDTO;
import com.example.verityx.entity.SysBackup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 系统备份记录Mapper接口
 */
@Mapper
public interface SysBackupMapper extends BaseMapper<SysBackup> {
    
    /**
     * 分页查询备份记录
     *
     * @param page       分页参数
     * @param backupName 备份名称
     * @param backupType 备份类型
     * @param status     状态
     * @return 分页结果
     */
    IPage<SysBackupDTO> selectBackupPage(Page<SysBackupDTO> page,
                                        @Param("backupName") String backupName,
                                        @Param("backupType") String backupType,
                                        @Param("status") String status);
    
    /**
     * 根据ID查询备份记录详情
     *
     * @param id 备份ID
     * @return 备份记录详情
     */
    SysBackupDTO selectBackupById(@Param("id") Long id);
} 