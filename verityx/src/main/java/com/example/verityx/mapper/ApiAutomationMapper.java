package com.example.verityx.mapper;

import com.example.verityx.entity.ApiAutomation;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface ApiAutomationMapper {

    int insertScene(ApiAutomation scene);

    ApiAutomation selectSceneById(int sceneId);

    List<ApiAutomation> selectAllScenes(@Param("projectId") String projectId);

    int updateScene(ApiAutomation scene);

    int deleteScene(int sceneId);
}
