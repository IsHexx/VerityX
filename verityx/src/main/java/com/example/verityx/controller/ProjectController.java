package com.example.verityx.controller;

import com.example.verityx.entity.Project;
import com.example.verityx.service.ProjectService;
import com.example.verityx.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.responses.ApiResponse;
//import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
@Tag(name = "项目管理", description = "项目管理")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @Operation(summary = "创建项目", description = "创建一个新的项目")
    @PostMapping
    public ApiResponse<Project> createProject(@RequestBody Project project) {
        Project createdProject = projectService.createProject(project);
        return ApiResponse.success(createdProject);
    }

    @Operation(summary = "查询单个项目", description = "按照项目id获取项目信息")
    @GetMapping("/{id}")
    public ApiResponse<Project> getProjectById(@PathVariable int id) {
        Project project = projectService.getProjectById(id);
        if (project != null) {
            return ApiResponse.success(project);
        }
        return ApiResponse.error(404, "项目未找到");
    }

    @Operation(summary = "查询所有项目", description = "获取所有项目")
    @GetMapping
    public ApiResponse<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return ApiResponse.success(projects);
    }

    @Operation(summary = "更新项目", description = "根据项目信息，更新项目信息")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateProject(@PathVariable int id, @RequestBody Project project) {
        project.setProjectId(id);
        boolean updated = projectService.updateProject(project);
        if (updated) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "更新失败");
    }

    @Operation(summary = "删除项目", description = "根据项目id删除项目")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteProject(@PathVariable int id) {
        boolean deleted = projectService.deleteProject(id);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }
}
