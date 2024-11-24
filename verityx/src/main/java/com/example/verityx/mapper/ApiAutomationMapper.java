package com.example.verityx.mapper;

import com.example.verityx.entity.ApiAutomation;
import java.util.List;

public interface ApiAutomationMapper {

    int insertScene(ApiAutomation scene);

    ApiAutomation selectSceneById(int sceneId);

    List<ApiAutomation> selectAllScenes();

    int updateScene(ApiAutomation scene);

    int deleteScene(int sceneId);
}
