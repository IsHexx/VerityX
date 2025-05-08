package com.example.verityx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDateTime;

/**
 * 系统版本信息实体类
 */
@TableName("sys_version_info")
public class SysVersionInfo {
    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 系统版本号
     */
    @TableField("version")
    private String version;
    
    /**
     * 发布日期
     */
    @TableField("release_date")
    private LocalDateTime releaseDate;
    
    /**
     * 版本说明
     */
    @TableField("release_notes")
    private String releaseNotes;
    
    /**
     * 是否为当前版本
     */
    @TableField("is_current")
    private Boolean isCurrent;
    
    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;
    
    /**
     * 更新时间
     */
    @TableField("update_time")
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
        return "SysVersionInfo{" +
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