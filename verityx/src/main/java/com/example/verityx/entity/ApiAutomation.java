package com.example.verityx.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "用户实体")
public class ApiAutomation {
    @Schema(description = "用户ID")
    private Integer sceneId;       // 场景ID
    private String directory;      // 所属目录
    private String sceneName;      // 场景名称
    private String stepMethod;     // 步骤方法
    private String stepName;       // 步骤名称
    private String remark;         // 备注

    // Getters and Setters
    public Integer getSceneId() {
        return sceneId;
    }

    public void setSceneId(Integer sceneId) {
        this.sceneId = sceneId;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getSceneName() {
        return sceneName;
    }

    public void setSceneName(String sceneName) {
        this.sceneName = sceneName;
    }

    public String getStepMethod() {
        return stepMethod;
    }

    public void setStepMethod(String stepMethod) {
        this.stepMethod = stepMethod;
    }

    public String getStepName() {
        return stepName;
    }

    public void setStepName(String stepName) {
        this.stepName = stepName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
