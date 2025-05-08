package com.example.verityx.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.verityx.util.Result;
import com.example.verityx.dto.SysBackupDTO;
import com.example.verityx.dto.request.SysBackupRequest;
import com.example.verityx.service.SysBackupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 系统备份控制器
 */
@RestController
@RequestMapping("/api/system/backup")
public class SysBackupController {
    
    @Autowired
    private SysBackupService sysBackupService;
    
    /**
     * 分页查询备份记录
     */
    @GetMapping("/list")
    @PreAuthorize("hasAuthority('system:backup:view')")
    public Result<IPage<SysBackupDTO>> getBackupPage(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String backupName,
            @RequestParam(required = false) String backupType,
            @RequestParam(required = false) String status) {
        return Result.success(sysBackupService.getBackupPage(page, size, backupName, backupType, status));
    }
    
    /**
     * 获取备份记录详情
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('system:backup:view')")
    public Result<SysBackupDTO> getBackupById(@PathVariable Long id) {
        return Result.success(sysBackupService.getBackupById(id));
    }
    
    /**
     * 创建备份
     */
    @PostMapping
    @PreAuthorize("hasAuthority('system:backup:create')")
    public Result<Long> createBackup(@RequestBody @Valid SysBackupRequest request) {
        // 实际应用中应该从SecurityContext获取当前用户ID
        Long operatorId = 1L; // 假设当前用户ID为1
        return Result.success(sysBackupService.createBackup(request, operatorId));
    }
    
    /**
     * 恢复备份
     */
    @PostMapping("/{id}/restore")
    @PreAuthorize("hasAuthority('system:backup:restore')")
    public Result<Boolean> restoreBackup(@PathVariable Long id) {
        // 实际应用中应该从SecurityContext获取当前用户ID
        Long operatorId = 1L; // 假设当前用户ID为1
        return Result.success(sysBackupService.restoreBackup(id, operatorId));
    }
    
    /**
     * 删除备份
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('system:backup:delete')")
    public Result<Boolean> deleteBackup(@PathVariable Long id) {
        // 实际应用中应该从SecurityContext获取当前用户ID
        Long operatorId = 1L; // 假设当前用户ID为1
        return Result.success(sysBackupService.deleteBackup(id, operatorId));
    }
    
    /**
     * 批量删除备份
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasAuthority('system:backup:delete')")
    public Result<Boolean> batchDeleteBackup(@RequestBody Long[] ids) {
        // 实际应用中应该从SecurityContext获取当前用户ID
        Long operatorId = 1L; // 假设当前用户ID为1
        return Result.success(sysBackupService.batchDeleteBackup(ids, operatorId));
    }
    
    /**
     * 下载备份文件
     */
    @GetMapping("/{id}/download")
    @PreAuthorize("hasAuthority('system:backup:download')")
    public ResponseEntity<Resource> downloadBackup(@PathVariable Long id) throws IOException {
        String filePath = sysBackupService.downloadBackup(id);
        File file = new File(filePath);
        
        if (!file.exists()) {
            throw new RuntimeException("备份文件不存在");
        }
        
        FileSystemResource resource = new FileSystemResource(file);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, 
                "attachment; filename*=UTF-8''" + URLEncoder.encode(file.getName(), StandardCharsets.UTF_8.toString()));
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE);
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(file.length()));
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
} 