package com.example.verityx.entity;

public class ApiQueryParams {

    private Integer paramId;       // 查询参数ID
    private Integer apiId;         // 关联接口ID
    private String parameter;      // 查询参数名称
    private String paramType;      // 参数类型
    private String description;    // 参数说明

    // Getters and Setters
    public Integer getParamId() {
        return paramId;
    }

    public void setParamId(Integer paramId) {
        this.paramId = paramId;
    }

    public Integer getApiId() {
        return apiId;
    }

    public void setApiId(Integer apiId) {
        this.apiId = apiId;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getParamType() {
        return paramType;
    }

    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
