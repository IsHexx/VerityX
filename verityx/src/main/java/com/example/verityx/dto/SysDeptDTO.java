package com.example.verityx.dto;

import java.time.LocalDateTime;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 系统部门DTO
 */
@Schema(description = "系统部门信息")
public class SysDeptDTO {

    @Schema(description = "部门ID")
    private Long id;
    
    @Schema(description = "部门名称")
    private String deptName;
    
    @Schema(description = "部门编码")
    private String deptCode;
    
    @Schema(description = "父部门ID")
    private Long parentId;
    
    @Schema(description = "显示顺序")
    private Integer sort;
    
    @Schema(description = "负责人")
    private String leader;
    
    @Schema(description = "联系电话")
    private String phone;
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "部门状态（0停用 1正常）")
    private Boolean isActive;
    
    @Schema(description = "部门描述")
    private String description;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
    
    @Schema(description = "子部门列表")
    private List<SysDeptDTO> children;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDeptName() {
        return deptName;
    }
    
    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }
    
    public String getDeptCode() {
        return deptCode;
    }
    
    public void setDeptCode(String deptCode) {
        this.deptCode = deptCode;
    }
    
    public Long getParentId() {
        return parentId;
    }
    
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    public Integer getSort() {
        return sort;
    }
    
    public void setSort(Integer sort) {
        this.sort = sort;
    }
    
    public String getLeader() {
        return leader;
    }
    
    public void setLeader(String leader) {
        this.leader = leader;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public Boolean getIsActive() {
        return isActive;
    }
    
    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
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
    
    public List<SysDeptDTO> getChildren() {
        return children;
    }
    
    public void setChildren(List<SysDeptDTO> children) {
        this.children = children;
    }
} 