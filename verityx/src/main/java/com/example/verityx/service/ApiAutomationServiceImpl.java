package com.example.verityx.service;

import com.example.verityx.entity.ApiAutomation;
import com.example.verityx.mapper.ApiAutomationMapper;
import com.example.verityx.service.ApiAutomationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiAutomationServiceImpl implements ApiAutomationService {

    @Autowired
    private ApiAutomationMapper apiAutomationMapper;

    @Override
    public ApiAutomation createScene(ApiAutomation scene) {
        apiAutomationMapper.insertScene(scene);
        return scene;
    }

    @Override
    public ApiAutomation getSceneById(int sceneId) {
        return apiAutomationMapper.selectSceneById(sceneId);
    }

    @Override
    public List<ApiAutomation> getAllScenes() {
        return apiAutomationMapper.selectAllScenes();
    }

    @Override
    public boolean updateScene(ApiAutomation scene) {
        return apiAutomationMapper.updateScene(scene) > 0;
    }

    @Override
    public boolean deleteScene(int sceneId) {
        return apiAutomationMapper.deleteScene(sceneId) > 0;
    }
}
