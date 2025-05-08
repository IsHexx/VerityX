package com.example.verityx.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysVersionInfoDTO;
import com.example.verityx.dto.SysLicenseDTO;
import com.example.verityx.dto.SysUpgradeRecordDTO;
import com.example.verityx.dto.SysAboutDTO;
import com.example.verityx.dto.request.SysVersionInfoRequest;
import com.example.verityx.dto.request.SysLicenseRequest;
import com.example.verityx.dto.request.SysUpgradeRequest;
import com.example.verityx.dto.request.SysAboutRequest;

import java.util.List;

/**
 * 系统信息Service接口
 */
public interface SysInfoService {
    
    // 版本信息相关方法
    
    /**
     * 获取当前版本信息
     * 
     * @return 当前版本信息
     */
    SysVersionInfoDTO getCurrentVersion();
    
    /**
     * 获取版本历史记录
     * 
     * @return 版本历史记录列表
     */
    List<SysVersionInfoDTO> getVersionHistory();
    
    /**
     * 添加新版本信息
     * 
     * @param request 版本信息请求
     * @return 新增的版本信息ID
     */
    Long addVersionInfo(SysVersionInfoRequest request);
    
    /**
     * 更新版本信息
     * 
     * @param request 版本信息请求
     * @return 是否更新成功
     */
    boolean updateVersionInfo(SysVersionInfoRequest request);
    
    /**
     * 设置当前版本
     * 
     * @param id 版本ID
     * @return 是否设置成功
     */
    boolean setCurrentVersion(Long id);
    
    // 许可证信息相关方法
    
    /**
     * 获取当前许可证信息
     * 
     * @return 当前许可证信息
     */
    SysLicenseDTO getCurrentLicense();
    
    /**
     * 获取许可证列表
     * 
     * @return 许可证列表
     */
    List<SysLicenseDTO> getLicenseList();
    
    /**
     * 添加许可证信息
     * 
     * @param request 许可证信息请求
     * @return 新增的许可证信息ID
     */
    Long addLicense(SysLicenseRequest request);
    
    /**
     * 更新许可证信息
     * 
     * @param request 许可证信息请求
     * @return 是否更新成功
     */
    boolean updateLicense(SysLicenseRequest request);
    
    /**
     * 验证许可证密钥
     * 
     * @param licenseKey 许可证密钥
     * @return 验证结果
     */
    boolean validateLicenseKey(String licenseKey);
    
    // 系统升级相关方法
    
    /**
     * 获取升级记录列表（分页）
     * 
     * @param page 页码
     * @param size 每页大小
     * @param status 状态
     * @return 分页结果
     */
    IPage<SysUpgradeRecordDTO> getUpgradeRecordPage(int page, int size, String status);
    
    /**
     * 获取升级记录详情
     * 
     * @param id 升级记录ID
     * @return 升级记录详情
     */
    SysUpgradeRecordDTO getUpgradeRecordDetail(Long id);
    
    /**
     * 执行系统升级
     * 
     * @param request 升级请求
     * @return 升级记录ID
     */
    Long executeUpgrade(SysUpgradeRequest request);
    
    /**
     * 获取最近一次升级记录
     * 
     * @return 最近一次升级记录
     */
    SysUpgradeRecordDTO getLatestUpgradeRecord();
    
    // 关于系统相关方法
    
    /**
     * 获取系统基本信息
     * 
     * @return 系统基本信息
     */
    SysAboutDTO getSystemInfo();
    
    /**
     * 更新系统基本信息
     * 
     * @param request 系统基本信息请求
     * @return 是否更新成功
     */
    boolean updateSystemInfo(SysAboutRequest request);
    
    /**
     * 获取服务条款
     * 
     * @return 服务条款
     */
    String getTermsOfService();
    
    /**
     * 获取隐私政策
     * 
     * @return 隐私政策
     */
    String getPrivacyPolicy();
}