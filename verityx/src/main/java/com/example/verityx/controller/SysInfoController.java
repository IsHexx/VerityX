package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.dto.SysVersionInfoDTO;
import com.example.verityx.dto.SysLicenseDTO;
import com.example.verityx.dto.SysUpgradeRecordDTO;
import com.example.verityx.dto.SysAboutDTO;
import com.example.verityx.util.Result;
import com.example.verityx.dto.request.SysVersionInfoRequest;
import com.example.verityx.dto.request.SysLicenseRequest;
import com.example.verityx.dto.request.SysUpgradeRequest;
import com.example.verityx.dto.request.SysAboutRequest;
import com.example.verityx.service.SysInfoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

/**
 * 系统信息Controller
 */
@RestController
@RequestMapping("/api/system/info")
@Tag(name = "系统信息管理", description = "系统信息相关接口，包括版本信息、许可证、升级记录和关于信息")
public class SysInfoController {
    
    @Autowired
    private SysInfoService sysInfoService;
    
    /**
     * 获取当前版本信息
     */
    @GetMapping("/version/current")
    @Operation(summary = "获取当前版本信息", description = "获取当前正在使用的系统版本信息")
    public Result<SysVersionInfoDTO> getCurrentVersion() {
        SysVersionInfoDTO versionInfo = sysInfoService.getCurrentVersion();
        return Result.success(versionInfo);
    }
    
    /**
     * 获取版本历史记录
     */
    @GetMapping("/version/history")
    @Operation(summary = "获取版本历史记录", description = "获取系统所有版本的历史记录")
    @PreAuthorize("hasAuthority('system:info:view')")
    public Result<List<SysVersionInfoDTO>> getVersionHistory() {
        List<SysVersionInfoDTO> versionList = sysInfoService.getVersionHistory();
        return Result.success(versionList);
    }
    
    /**
     * 添加新版本信息
     */
    @PostMapping("/version")
    @Operation(summary = "添加新版本信息", description = "添加新的系统版本信息")
    @PreAuthorize("hasAuthority('system:info:add')")
    public Result<Long> addVersionInfo(@RequestBody @Valid SysVersionInfoRequest request) {
        Long id = sysInfoService.addVersionInfo(request);
        return Result.success(id);
    }
    
    /**
     * 更新版本信息
     */
    @PutMapping("/version")
    @Operation(summary = "更新版本信息", description = "更新已有的系统版本信息")
    @PreAuthorize("hasAuthority('system:info:edit')")
    public Result<Void> updateVersionInfo(@RequestBody @Valid SysVersionInfoRequest request) {
        boolean success = sysInfoService.updateVersionInfo(request);
        return success ? Result.success() : Result.failed("更新版本信息失败");
    }
    
    /**
     * 设置当前版本
     */
    @PutMapping("/version/{id}/current")
    @Operation(summary = "设置当前版本", description = "将指定版本设置为当前使用的版本")
    @PreAuthorize("hasAuthority('system:info:edit')")
    public Result<Void> setCurrentVersion(@PathVariable("id") Long id) {
        boolean success = sysInfoService.setCurrentVersion(id);
        return success ? Result.success() : Result.failed("设置当前版本失败");
    }
    
    /**
     * 获取当前许可证信息
     */
    @GetMapping("/license/current")
    @Operation(summary = "获取当前许可证信息", description = "获取当前生效的系统许可证信息")
    public Result<SysLicenseDTO> getCurrentLicense() {
        SysLicenseDTO license = sysInfoService.getCurrentLicense();
        return Result.success(license);
    }
    
    /**
     * 获取许可证列表
     */
    @GetMapping("/license/list")
    @Operation(summary = "获取许可证列表", description = "获取系统所有许可证的列表")
    @PreAuthorize("hasAuthority('system:info:view')")
    public Result<List<SysLicenseDTO>> getLicenseList() {
        List<SysLicenseDTO> licenseList = sysInfoService.getLicenseList();
        return Result.success(licenseList);
    }
    
    /**
     * 添加许可证信息
     */
    @PostMapping("/license")
    @Operation(summary = "添加许可证信息", description = "添加新的系统许可证信息")
    @PreAuthorize("hasAuthority('system:info:add')")
    public Result<Long> addLicense(@RequestBody @Valid SysLicenseRequest request) {
        Long id = sysInfoService.addLicense(request);
        return Result.success(id);
    }
    
    /**
     * 更新许可证信息
     */
    @PutMapping("/license")
    @Operation(summary = "更新许可证信息", description = "更新已有的系统许可证信息")
    @PreAuthorize("hasAuthority('system:info:edit')")
    public Result<Void> updateLicense(@RequestBody @Valid SysLicenseRequest request) {
        boolean success = sysInfoService.updateLicense(request);
        return success ? Result.success() : Result.failed("更新许可证信息失败");
    }
    
    /**
     * 验证许可证密钥
     */
    @PostMapping("/license/validate")
    @Operation(summary = "验证许可证密钥", description = "验证指定的许可证密钥是否有效")
    public Result<Boolean> validateLicenseKey(@RequestParam String licenseKey) {
        boolean valid = sysInfoService.validateLicenseKey(licenseKey);
        return Result.success(valid);
    }
    
    /**
     * 获取升级记录列表
     */
    @GetMapping("/upgrade/list")
    @Operation(summary = "获取升级记录列表", description = "分页获取系统升级记录列表")
    @PreAuthorize("hasAuthority('system:info:view')")
    public Result<IPage<SysUpgradeRecordDTO>> getUpgradeRecordList(
            @Parameter(description = "页码") @RequestParam(value = "page", defaultValue = "1") Integer page,
            @Parameter(description = "每页大小") @RequestParam(value = "size", defaultValue = "10") Integer size,
            @Parameter(description = "状态") @RequestParam(value = "status", required = false) String status) {
        IPage<SysUpgradeRecordDTO> recordPage = sysInfoService.getUpgradeRecordPage(page, size, status);
        return Result.success(recordPage);
    }
    
    /**
     * 获取升级记录详情
     */
    @GetMapping("/upgrade/{id}")
    @Operation(summary = "获取升级记录详情", description = "获取指定升级记录的详细信息")
    @PreAuthorize("hasAuthority('system:info:view')")
    public Result<SysUpgradeRecordDTO> getUpgradeRecordDetail(@PathVariable("id") Long id) {
        SysUpgradeRecordDTO record = sysInfoService.getUpgradeRecordDetail(id);
        return Result.success(record);
    }
    
    /**
     * 执行系统升级
     */
    @PostMapping("/upgrade")
    @Operation(summary = "执行系统升级", description = "执行系统升级操作")
    @PreAuthorize("hasAuthority('system:info:upgrade')")
    public Result<Long> executeUpgrade(@RequestBody @Valid SysUpgradeRequest request) {
        Long id = sysInfoService.executeUpgrade(request);
        return Result.success(id);
    }
    
    /**
     * 获取最近一次升级记录
     */
    @GetMapping("/upgrade/latest")
    @Operation(summary = "获取最近一次升级记录", description = "获取系统最近一次的升级记录")
    public Result<SysUpgradeRecordDTO> getLatestUpgradeRecord() {
        SysUpgradeRecordDTO record = sysInfoService.getLatestUpgradeRecord();
        return Result.success(record);
    }
    
    /**
     * 获取系统基本信息
     */
    @GetMapping("/about")
    @Operation(summary = "获取系统基本信息", description = "获取关于系统的基本信息")
    public Result<SysAboutDTO> getSystemInfo() {
        SysAboutDTO systemInfo = sysInfoService.getSystemInfo();
        return Result.success(systemInfo);
    }
    
    /**
     * 更新系统基本信息
     */
    @PutMapping("/about")
    @Operation(summary = "更新系统基本信息", description = "更新关于系统的基本信息")
    @PreAuthorize("hasAuthority('system:info:edit')")
    public Result<Void> updateSystemInfo(@RequestBody @Valid SysAboutRequest request) {
        boolean success = sysInfoService.updateSystemInfo(request);
        return success ? Result.success() : Result.failed("更新系统信息失败");
    }
    
    /**
     * 获取服务条款
     */
    @GetMapping("/about/terms")
    @Operation(summary = "获取服务条款", description = "获取系统的服务条款")
    public Result<String> getTermsOfService() {
        String terms = sysInfoService.getTermsOfService();
        return Result.success(terms);
    }
    
    /**
     * 获取隐私政策
     */
    @GetMapping("/about/privacy")
    @Operation(summary = "获取隐私政策", description = "获取系统的隐私政策")
    public Result<String> getPrivacyPolicy() {
        String privacy = sysInfoService.getPrivacyPolicy();
        return Result.success(privacy);
    }
} 