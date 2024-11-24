package com.example.verityx.service;

import com.example.verityx.entity.Project;
import com.example.verityx.mapper.ProjectMapper;
import com.example.verityx.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private ProjectMapper projectMapper;

    @Override
    public Project createProject(Project project) {
        System.out.println("传递的参数是"+project);
        projectMapper.insertProject(project);
        return project;
    }

    @Override
    public Project getProjectById(int projectId) {
        return projectMapper.selectProjectById(projectId);
    }

    @Override
    public List<Project> getAllProjects() {
        return projectMapper.selectAllProjects();
    }

    @Override
    public boolean updateProject(Project project) {
        System.out.println("传递的参数是"+project);
        return projectMapper.updateProject(project) > 0;
    }

    @Override
    public boolean deleteProject(int projectId) {
        return projectMapper.deleteProject(projectId) > 0;
    }
}
