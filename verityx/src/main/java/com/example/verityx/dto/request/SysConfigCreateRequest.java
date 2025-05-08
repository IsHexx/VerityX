package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * 系统配置创建请求
 */
@Schema(description = "系统配置创建请求")
public class SysConfigCreateRequest {
    
    @Schema(description = "参数名称", required = true)
    @NotBlank(message = "参数名称不能为空")
    @Size(max = 100, message = "参数名称长度不能超过100个字符")
    private String configName;
    
    @Schema(description = "参数键名", required = true)
    @NotBlank(message = "参数键名不能为空")
    @Size(max = 100, message = "参数键名长度不能超过100个字符")
    private String configKey;
    
    @Schema(description = "参数键值", required = true)
    @NotBlank(message = "参数键值不能为空")
    @Size(max = 500, message = "参数键值长度不能超过500个字符")
    private String configValue;
    
    @Schema(description = "配置类型", defaultValue = "text")
    private String configType = "text";
    
    @Schema(description = "是否系统内置（0否 1是）", defaultValue = "false")
    private Boolean isSystem = false;
    
    @Schema(description = "状态（0停用 1正常）", defaultValue = "true")
    private Boolean status = true;
    
    @Schema(description = "备注")
    private String remark;
    
    // Getters and Setters
    
    public String getConfigName() {
        return configName;
    }
    
    public void setConfigName(String configName) {
        this.configName = configName;
    }
    
    public String getConfigKey() {
        return configKey;
    }
    
    public void setConfigKey(String configKey) {
        this.configKey = configKey;
    }
    
    public String getConfigValue() {
        return configValue;
    }
    
    public void setConfigValue(String configValue) {
        this.configValue = configValue;
    }
    
    public String getConfigType() {
        return configType;
    }
    
    public void setConfigType(String configType) {
        this.configType = configType;
    }
    
    public Boolean getIsSystem() {
        return isSystem;
    }
    
    public void setIsSystem(Boolean isSystem) {
        this.isSystem = isSystem;
    }
    
    public Boolean getStatus() {
        return status;
    }
    
    public void setStatus(Boolean status) {
        this.status = status;
    }
    
    public String getRemark() {
        return remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
} 