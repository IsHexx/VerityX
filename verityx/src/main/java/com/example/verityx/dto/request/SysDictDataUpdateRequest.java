package com.example.verityx.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 字典数据更新请求
 */
@Schema(description = "字典数据更新请求")
public class SysDictDataUpdateRequest {
    
    @Schema(description = "字典数据ID", required = true)
    @NotNull(message = "字典数据ID不能为空")
    private Long id;
    
    @Schema(description = "字典类型", required = true)
    @NotBlank(message = "字典类型不能为空")
    @Size(max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;
    
    @Schema(description = "字典标签", required = true)
    @NotBlank(message = "字典标签不能为空")
    @Size(max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;
    
    @Schema(description = "字典键值", required = true)
    @NotBlank(message = "字典键值不能为空")
    @Size(max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;
    
    @Schema(description = "字典排序")
    private Integer dictSort;
    
    @Schema(description = "样式属性")
    private String cssClass;
    
    @Schema(description = "表格回显样式")
    private String listClass;
    
    @Schema(description = "是否默认（0否 1是）")
    private Boolean isDefault;
    
    @Schema(description = "状态（0停用 1正常）")
    private Boolean status;
    
    @Schema(description = "备注")
    private String remark;
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDictType() {
        return dictType;
    }
    
    public void setDictType(String dictType) {
        this.dictType = dictType;
    }
    
    public String getDictLabel() {
        return dictLabel;
    }
    
    public void setDictLabel(String dictLabel) {
        this.dictLabel = dictLabel;
    }
    
    public String getDictValue() {
        return dictValue;
    }
    
    public void setDictValue(String dictValue) {
        this.dictValue = dictValue;
    }
    
    public Integer getDictSort() {
        return dictSort;
    }
    
    public void setDictSort(Integer dictSort) {
        this.dictSort = dictSort;
    }
    
    public String getCssClass() {
        return cssClass;
    }
    
    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
    }
    
    public String getListClass() {
        return listClass;
    }
    
    public void setListClass(String listClass) {
        this.listClass = listClass;
    }
    
    public Boolean getIsDefault() {
        return isDefault;
    }
    
    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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