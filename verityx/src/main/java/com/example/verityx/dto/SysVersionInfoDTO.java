package com.example.verityx.dto;

import java.time.LocalDateTime;

/**
 * 系统版本信息DTO类
 */
public class SysVersionInfoDTO {
    /**
     * 主键ID
     */
    private Long id;
    
    /**
     * 系统版本号
     */
    private String version;
    
    /**
     * 发布日期
     */
    private LocalDateTime releaseDate;
    
    /**
     * 版本说明
     */
    private String releaseNotes;
    
    /**
     * 是否为当前版本
     */
    private Boolean isCurrent;
    
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public LocalDateTime getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDateTime releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getReleaseNotes() {
        return releaseNotes;
    }
    
    public void setReleaseNotes(String releaseNotes) {
        this.releaseNotes = releaseNotes;
    }
    
    public Boolean getIsCurrent() {
        return isCurrent;
    }
    
    public void setIsCurrent(Boolean isCurrent) {
        this.isCurrent = isCurrent;
    }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }
    
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
    
    @Override
    public String toString() {
        return "SysVersionInfoDTO{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", releaseDate=" + releaseDate +
                ", releaseNotes='" + releaseNotes + '\'' +
                ", isCurrent=" + isCurrent +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
} 