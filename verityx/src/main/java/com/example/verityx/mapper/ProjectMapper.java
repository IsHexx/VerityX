package com.example.verityx.mapper;

import com.example.verityx.entity.Project;

import java.util.List;

public interface ProjectMapper {

    int insertProject(Project project);

    Project selectProjectById(int projectId);

    List<Project> selectAllProjects();

    int updateProject(Project project);

    int deleteProject(int projectId);
}
