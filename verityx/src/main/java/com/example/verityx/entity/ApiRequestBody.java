package com.example.verityx.entity;

public class ApiRequestBody {

    private Integer bodyId;        // 请求Body ID
    private Integer apiId;         // 关联接口ID
    private String parameter;      // 请求Body参数名称
    private String description;    // 参数说明

    // Getters and Setters
    public Integer getBodyId() {
        return bodyId;
    }

    public void setBodyId(Integer bodyId) {
        this.bodyId = bodyId;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
