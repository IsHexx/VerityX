package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * 更新用户请求
 */
@Schema(description = "更新用户请求参数")
public class SysUserUpdateRequest {
    
    @Schema(description = "用户ID", required = true)
    @NotNull(message = "用户ID不能为空")
    private Long id;
    
    @Schema(description = "姓名")
    private String realName;
    
    @Schema(description = "用户昵称")
    private String nickname;
    
    @Schema(description = "性别（0男 1女 2未知）")
    private Integer gender;
    
    @Schema(description = "用户邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "手机号码")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号码格式不正确")
    private String phone;
    
    @Schema(description = "头像地址")
    private String avatar;
    
    @Schema(description = "部门ID")
    private Long deptId;
    
    @Schema(description = "备注")
    private String remark;
    
    @Schema(description = "角色ID列表")
    private Long[] roleIds;
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getRealName() {
        return realName;
    }
    
    public void setRealName(String realName) {
        this.realName = realName;
    }
    
    public String getNickname() {
        return nickname;
    }
    
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    
    public Integer getGender() {
        return gender;
    }
    
    public void setGender(Integer gender) {
        this.gender = gender;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    public String getAvatar() {
        return avatar;
    }
    
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    public Long getDeptId() {
        return deptId;
    }
    
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public Long[] getRoleIds() {
        return roleIds;
    }
    
    public void setRoleIds(Long[] roleIds) {
        this.roleIds = roleIds;
    }
} 