package com.example.verityx.dto;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 系统权限DTO
 */
@Schema(description = "系统权限信息")
public class SysPermissionDTO {

    @Schema(description = "权限ID")
    private Long id;
    
    @Schema(description = "权限名称")
    private String permissionName;
    
    @Schema(description = "权限编码")
    private String permissionCode;
    
    @Schema(description = "权限类型：menu菜单、button按钮、api接口")
    private String permissionType;
    
    @Schema(description = "父级权限ID")
    private Long parentId;
    
    @Schema(description = "权限路径")
    private String permissionPath;
    
    @Schema(description = "权限组件")
    private String component;
    
    @Schema(description = "权限图标")
    private String icon;
    
    @Schema(description = "显示顺序")
    private Integer sort;
    
    @Schema(description = "状态：0禁用、1启用")
    private Boolean isActive;
    
    @Schema(description = "是否显示：0隐藏、1显示")
    private Boolean isVisible;
    
    @Schema(description = "是否系统内置")
    private Boolean isSystem;
    
    @Schema(description = "权限描述")
    private String description;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "子权限列表")
    private List<SysPermissionDTO> children;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPermissionName() {
        return permissionName;
    }
    
    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }
    
    public String getPermissionCode() {
        return permissionCode;
    }
    
    public void setPermissionCode(String permissionCode) {
        this.permissionCode = permissionCode;
    }
    
    public String getPermissionType() {
        return permissionType;
    }
    
    public void setPermissionType(String permissionType) {
        this.permissionType = permissionType;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public String getPermissionPath() {
        return permissionPath;
    }
    
    public void setPermissionPath(String permissionPath) {
        this.permissionPath = permissionPath;
    }
    
    public String getComponent() {
        return component;
    }
    
    public void setComponent(String component) {
        this.component = component;
    }
    
    public String getIcon() {
        return icon;
    }
    
    public void setIcon(String icon) {
        this.icon = icon;
    }
    
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    public Boolean getIsVisible() {
        return isVisible;
    }
    
    public void setIsVisible(Boolean isVisible) {
        this.isVisible = isVisible;
    }
    
    public Boolean getIsSystem() {
        return isSystem;
    }
    
    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    public List<SysPermissionDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<SysPermissionDTO> children) {
        this.children = children;
    }
} 