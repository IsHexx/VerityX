package com.example.verityx.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.verityx.dto.SysVersionInfoDTO;
import com.example.verityx.dto.SysLicenseDTO;
import com.example.verityx.dto.SysUpgradeRecordDTO;
import com.example.verityx.dto.SysAboutDTO;
import com.example.verityx.dto.SysBackupDTO;
import com.example.verityx.dto.request.SysVersionInfoRequest;
import com.example.verityx.dto.request.SysLicenseRequest;
import com.example.verityx.dto.request.SysUpgradeRequest;
import com.example.verityx.dto.request.SysAboutRequest;
import com.example.verityx.dto.request.SysBackupRequest;
import com.example.verityx.entity.SysVersionInfo;
import com.example.verityx.entity.SysLicense;
import com.example.verityx.entity.SysUpgradeRecord;
import com.example.verityx.entity.SysAbout;
import com.example.verityx.mapper.SysVersionInfoMapper;
import com.example.verityx.mapper.SysLicenseMapper;
import com.example.verityx.mapper.SysUpgradeRecordMapper;
import com.example.verityx.mapper.SysAboutMapper;
import com.example.verityx.service.SysInfoService;
import com.example.verityx.service.SysBackupService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 系统信息Service实现类
 */
@Service
public class SysInfoServiceImpl implements SysInfoService {
    
    @Autowired
    private SysVersionInfoMapper sysVersionInfoMapper;
    
    @Autowired
    private SysLicenseMapper sysLicenseMapper;
    
    @Autowired
    private SysUpgradeRecordMapper sysUpgradeRecordMapper;
    
    @Autowired
    private SysAboutMapper sysAboutMapper;
    
    @Autowired
    private SysBackupService sysBackupService;
    
    // 版本信息相关方法实现
    
    @Override
    public SysVersionInfoDTO getCurrentVersion() {
        SysVersionInfo versionInfo = sysVersionInfoMapper.selectCurrentVersion();
        if (versionInfo == null) {
            return null;
        }
        return convertToVersionInfoDTO(versionInfo);
    }
    
    @Override
    public List<SysVersionInfoDTO> getVersionHistory() {
        List<SysVersionInfo> versionInfoList = sysVersionInfoMapper.selectList(
                new LambdaQueryWrapper<SysVersionInfo>()
                        .orderByDesc(SysVersionInfo::getReleaseDate)
        );
        return versionInfoList.stream()
                .map(this::convertToVersionInfoDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Long addVersionInfo(SysVersionInfoRequest request) {
        SysVersionInfo versionInfo = new SysVersionInfo();
        BeanUtils.copyProperties(request, versionInfo);
        versionInfo.setCreateTime(LocalDateTime.now());
        
        // 如果设置为当前版本，需要将其他版本设置为非当前版本
        if (Boolean.TRUE.equals(request.getIsCurrent())) {
            sysVersionInfoMapper.resetCurrentVersion();
        }
        
        sysVersionInfoMapper.insert(versionInfo);
        return versionInfo.getId();
    }
    
    @Override
    @Transactional
    public boolean updateVersionInfo(SysVersionInfoRequest request) {
        if (request.getId() == null) {
            return false;
        }
        
        SysVersionInfo versionInfo = sysVersionInfoMapper.selectById(request.getId());
        if (versionInfo == null) {
            return false;
        }
        
        BeanUtils.copyProperties(request, versionInfo);
        versionInfo.setUpdateTime(LocalDateTime.now());
        
        // 如果设置为当前版本，需要将其他版本设置为非当前版本
        if (Boolean.TRUE.equals(request.getIsCurrent()) && !Boolean.TRUE.equals(versionInfo.getIsCurrent())) {
            sysVersionInfoMapper.resetCurrentVersion();
        }
        
        return sysVersionInfoMapper.updateById(versionInfo) > 0;
    }
    
    @Override
    @Transactional
    public boolean setCurrentVersion(Long id) {
        SysVersionInfo versionInfo = sysVersionInfoMapper.selectById(id);
        if (versionInfo == null) {
            return false;
        }
        
        // 重置所有版本为非当前版本
        sysVersionInfoMapper.resetCurrentVersion();
        
        // 设置指定版本为当前版本
        return sysVersionInfoMapper.setCurrentVersion(id) > 0;
    }
    
    // 许可证信息相关方法实现
    
    @Override
    public SysLicenseDTO getCurrentLicense() {
        SysLicense license = sysLicenseMapper.selectActiveLicense();
        if (license == null) {
            return null;
        }
        return convertToLicenseDTO(license);
    }
    
    @Override
    public List<SysLicenseDTO> getLicenseList() {
        List<SysLicense> licenseList = sysLicenseMapper.selectList(
                new LambdaQueryWrapper<SysLicense>()
                        .orderByDesc(SysLicense::getCreateTime)
        );
        return licenseList.stream()
                .map(this::convertToLicenseDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional
    public Long addLicense(SysLicenseRequest request) {
        SysLicense license = new SysLicense();
        BeanUtils.copyProperties(request, license);
        license.setCreateTime(LocalDateTime.now());
        
        sysLicenseMapper.insert(license);
        return license.getId();
    }
    
    @Override
    @Transactional
    public boolean updateLicense(SysLicenseRequest request) {
        if (request.getId() == null) {
            return false;
        }
        
        SysLicense license = sysLicenseMapper.selectById(request.getId());
        if (license == null) {
            return false;
        }
        
        BeanUtils.copyProperties(request, license);
        license.setUpdateTime(LocalDateTime.now());
        
        return sysLicenseMapper.updateById(license) > 0;
    }
    
    @Override
    public boolean validateLicenseKey(String licenseKey) {
        // 简单实现，实际应该进行更复杂的许可证验证
        SysLicense license = sysLicenseMapper.selectOne(
                new LambdaQueryWrapper<SysLicense>()
                        .eq(SysLicense::getLicenseKey, licenseKey)
                        .eq(SysLicense::getStatus, "有效")
        );
        return license != null;
    }
    
    // 系统升级相关方法实现
    
    @Override
    public IPage<SysUpgradeRecordDTO> getUpgradeRecordPage(int page, int size, String status) {
        Page<SysUpgradeRecord> pageParam = new Page<>(page, size);
        IPage<SysUpgradeRecord> recordPage = sysUpgradeRecordMapper.selectUpgradeRecordPage(pageParam, status);
        
        return recordPage.convert(this::convertToUpgradeRecordDTO);
    }
    
    @Override
    public SysUpgradeRecordDTO getUpgradeRecordDetail(Long id) {
        SysUpgradeRecord record = sysUpgradeRecordMapper.selectById(id);
        if (record == null) {
            return null;
        }
        
        SysUpgradeRecordDTO dto = convertToUpgradeRecordDTO(record);
        
        // 如果有关联的备份ID，获取备份信息
        if (record.getBackupId() != null) {
            SysBackupDTO backupDTO = sysBackupService.getBackupDetail(record.getBackupId());
            dto.setBackup(backupDTO);
        }
        
        return dto;
    }
    
    @Override
    @Transactional
    public Long executeUpgrade(SysUpgradeRequest request) {
        // 获取当前版本
        SysVersionInfo currentVersion = sysVersionInfoMapper.selectCurrentVersion();
        if (currentVersion == null) {
            // 如果没有当前版本，使用默认版本
            currentVersion = new SysVersionInfo();
            currentVersion.setVersion("1.0.0");
        }
        
        // 创建升级记录
        SysUpgradeRecord record = new SysUpgradeRecord();
        record.setFromVersion(currentVersion.getVersion());
        record.setToVersion(request.getToVersion());
        record.setUpgradeTime(LocalDateTime.now());
        record.setUpgradeUser(request.getUpgradeUser());
        record.setStatus("进行中");
        record.setRemarks(request.getRemarks());
        record.setCreateTime(LocalDateTime.now());
        
        // 如果需要创建备份
        if (Boolean.TRUE.equals(request.getCreateBackup())) {
            try {
                // 创建备份
                SysBackupRequest backupRequest = new SysBackupRequest();
                backupRequest.setBackupName("升级备份_" + currentVersion.getVersion() + "_to_" + request.getToVersion());
                backupRequest.setBackupType("升级前备份");
                backupRequest.setDescription("系统升级前自动创建的备份，从版本 " + currentVersion.getVersion() + " 升级到 " + request.getToVersion());
                backupRequest.setRemark(request.getBackupRemark());
                // 使用默认系统操作人ID (1L代表系统管理员)
                Long backupId = sysBackupService.createBackup(backupRequest, 1L);
                record.setBackupId(backupId);
            } catch (Exception e) {
                // 备份失败，记录错误信息
                record.setStatus("失败");
                record.setErrorMessage("备份失败: " + e.getMessage());
                sysUpgradeRecordMapper.insert(record);
                return record.getId();
            }
        }
        
        try {
            // 模拟升级过程，实际应该执行真正的升级过程
            Thread.sleep(2000);
            
            // 创建新版本信息
            SysVersionInfoRequest versionRequest = new SysVersionInfoRequest();
            versionRequest.setVersion(request.getToVersion());
            versionRequest.setReleaseDate(LocalDateTime.now());
            versionRequest.setReleaseNotes("系统自动升级创建的版本，从版本 " + currentVersion.getVersion() + " 升级而来。");
            versionRequest.setIsCurrent(true);
            addVersionInfo(versionRequest);
            
            // 更新升级记录状态
            record.setStatus("成功");
            record.setDuration(2); // 模拟耗时2秒
            sysUpgradeRecordMapper.insert(record);
            
            return record.getId();
        } catch (Exception e) {
            // 升级失败，记录错误信息
            record.setStatus("失败");
            record.setErrorMessage("升级失败: " + e.getMessage());
            sysUpgradeRecordMapper.insert(record);
            return record.getId();
        }
    }
    
    @Override
    public SysUpgradeRecordDTO getLatestUpgradeRecord() {
        SysUpgradeRecord record = sysUpgradeRecordMapper.selectLatestUpgradeRecord();
        if (record == null) {
            return null;
        }
        return convertToUpgradeRecordDTO(record);
    }
    
    // 关于系统相关方法实现
    
    @Override
    public SysAboutDTO getSystemInfo() {
        SysAbout about = sysAboutMapper.selectSysAbout();
        if (about == null) {
            return null;
        }
        return convertToAboutDTO(about);
    }
    
    @Override
    @Transactional
    public boolean updateSystemInfo(SysAboutRequest request) {
        SysAbout about = sysAboutMapper.selectSysAbout();
        if (about == null) {
            // 如果不存在，创建一个新的记录
            about = new SysAbout();
            BeanUtils.copyProperties(request, about);
            about.setCreateTime(LocalDateTime.now());
            about.setUpdateTime(LocalDateTime.now());
            return sysAboutMapper.insert(about) > 0;
        } else {
            // 如果存在，更新记录
            BeanUtils.copyProperties(request, about);
            about.setUpdateTime(LocalDateTime.now());
            return sysAboutMapper.updateById(about) > 0;
        }
    }
    
    @Override
    public String getTermsOfService() {
        SysAbout about = sysAboutMapper.selectSysAbout();
        return about != null ? about.getTermsOfService() : "";
    }
    
    @Override
    public String getPrivacyPolicy() {
        SysAbout about = sysAboutMapper.selectSysAbout();
        return about != null ? about.getPrivacyPolicy() : "";
    }
    
    // 转换方法
    
    private SysVersionInfoDTO convertToVersionInfoDTO(SysVersionInfo entity) {
        if (entity == null) {
            return null;
        }
        SysVersionInfoDTO dto = new SysVersionInfoDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private SysLicenseDTO convertToLicenseDTO(SysLicense entity) {
        if (entity == null) {
            return null;
        }
        SysLicenseDTO dto = new SysLicenseDTO();
        BeanUtils.copyProperties(entity, dto);
        
        // 计算是否过期和剩余天数
        if (entity.getExpirationDate() != null) {
            LocalDateTime now = LocalDateTime.now();
            dto.setExpired(now.isAfter(entity.getExpirationDate()));
            
            // 计算剩余天数
            if (now.isBefore(entity.getExpirationDate())) {
                dto.setRemainingDays((int) ChronoUnit.DAYS.between(now, entity.getExpirationDate()));
            } else {
                dto.setRemainingDays(0);
            }
        } else {
            // 永久许可证
            dto.setExpired(false);
            dto.setRemainingDays(Integer.MAX_VALUE);
        }
        
        return dto;
    }
    
    private SysUpgradeRecordDTO convertToUpgradeRecordDTO(SysUpgradeRecord entity) {
        if (entity == null) {
            return null;
        }
        SysUpgradeRecordDTO dto = new SysUpgradeRecordDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
    
    private SysAboutDTO convertToAboutDTO(SysAbout entity) {
        if (entity == null) {
            return null;
        }
        SysAboutDTO dto = new SysAboutDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }
} 