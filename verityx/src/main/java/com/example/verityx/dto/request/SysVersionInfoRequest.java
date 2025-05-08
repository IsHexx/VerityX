package com.example.verityx.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 系统版本信息请求DTO类
 */
public class SysVersionInfoRequest {
    /**
     * 主键ID（更新时使用）
     */
    private Long id;
    
    /**
     * 系统版本号
     */
    @NotBlank(message = "系统版本号不能为空")
    private String version;
    
    /**
     * 发布日期
     */
    @NotNull(message = "发布日期不能为空")
    private LocalDateTime releaseDate;
    
    /**
     * 版本说明
     */
    private String releaseNotes;
    
    /**
     * 是否为当前版本
     */
    private Boolean isCurrent;
    
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
    
    @Override
    public String toString() {
        return "SysVersionInfoRequest{" +
                "id=" + id +
                ", version='" + version + '\'' +
                ", releaseDate=" + releaseDate +
                ", releaseNotes='" + releaseNotes + '\'' +
                ", isCurrent=" + isCurrent +
                '}';
    }
}