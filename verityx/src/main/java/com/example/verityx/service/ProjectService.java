package com.example.verityx.service;

import com.example.verityx.entity.Project;

import java.util.List;

public interface ProjectService {

    Project createProject(Project project);

    Project getProjectById(int projectId);

    List<Project> getAllProjects();

    boolean updateProject(Project project);

    boolean deleteProject(int projectId);
}
