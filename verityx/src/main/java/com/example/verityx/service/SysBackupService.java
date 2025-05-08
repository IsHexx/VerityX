package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysBackupDTO;
import com.example.verityx.dto.request.SysBackupRequest;

/**
 * 系统备份服务接口
 */
public interface SysBackupService {
    
    /**
     * 分页查询备份记录
     *
     * @param page       页码
     * @param size       每页大小
     * @param backupName 备份名称
     * @param backupType 备份类型
     * @param status     状态
     * @return 备份记录分页结果
     */
    IPage<SysBackupDTO> getBackupPage(int page, int size, String backupName, String backupType, String status);
    
    /**
     * 获取备份记录详情
     *
     * @param id 备份ID
     * @return 备份记录详情
     */
    SysBackupDTO getBackupById(Long id);
    
    /**
     * 获取备份详细信息，作为getBackupById的别名
     *
     * @param id 备份ID
     * @return 备份记录详情
     */
    default SysBackupDTO getBackupDetail(Long id) {
        return getBackupById(id);
    }
    
    /**
     * 创建备份
     *
     * @param request    备份请求
     * @param operatorId 操作人ID
     * @return 备份ID
     */
    Long createBackup(SysBackupRequest request, Long operatorId);
    
    /**
     * 恢复备份
     *
     * @param id         备份ID
     * @param operatorId 操作人ID
     * @return 恢复结果
     */
    boolean restoreBackup(Long id, Long operatorId);
    
    /**
     * 删除备份
     *
     * @param id         备份ID
     * @param operatorId 操作人ID
     * @return 删除结果
     */
    boolean deleteBackup(Long id, Long operatorId);
    
    /**
     * 批量删除备份
     *
     * @param ids        备份ID列表
     * @param operatorId 操作人ID
     * @return 删除结果
     */
    boolean batchDeleteBackup(Long[] ids, Long operatorId);
    
    /**
     * 下载备份文件
     *
     * @param id 备份ID
     * @return 文件下载地址
     */
    String downloadBackup(Long id);
} 