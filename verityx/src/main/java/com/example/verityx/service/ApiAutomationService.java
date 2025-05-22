package com.example.verityx.service;

import com.example.verityx.entity.ApiAutomation;
import java.util.List;

public interface ApiAutomationService {

    ApiAutomation createScene(ApiAutomation scene);

    ApiAutomation getSceneById(int sceneId);

    List<ApiAutomation> getAllScenes(String projectId);

    boolean updateScene(ApiAutomation scene);

    boolean deleteScene(int sceneId);
}
