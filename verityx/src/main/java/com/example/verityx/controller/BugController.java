package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.Bug;
import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.service.BugService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bugs")
@Tag(name = "缺陷管理", description = "缺陷管理")
public class BugController {

    @Autowired
    private BugService bugService;

    @Operation(summary = "提Bug", description = "提交一个新的缺陷")
    // 创建缺陷
    @PostMapping
    public ApiResponse<Bug> createBug(@RequestBody Bug bug) {
        System.out.println("请求参数是:" + bug);
        Bug createdBug= bugService.createBug(bug);
        return ApiResponse.success(createdBug);
    }

    // 查询单个缺陷
    @GetMapping("/{id}")
    @Operation(summary = "查询单个缺陷详情", description = "按照缺陷id获取缺陷详情")
    public Bug getBugById(@PathVariable int id) {
        return bugService.getBugById(id);
    }

    // 分页获取测试计划
    @Operation(summary = "分页查询Bug", description = "根据分页参数查询Bug")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getBugsWithPagination(
            @RequestParam int page, 
            @RequestParam int pageSize, 
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);
        
        List<Bug> bug = bugService.getBugsWithPagination(pageSize, offset, status, keyword, projectId);
        int total = bugService.getBugCount(status, keyword, projectId); // 获取总记录数
        Map<String, Object> response = new HashMap<>();
        response.put("data", bug);
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    // 查询所有缺陷
    @Operation(summary = "查询所有缺陷", description = "获取所有缺陷")
    @GetMapping
    public ApiResponse<List<Bug>> getAllBugs() {
        List<Bug> bugs= bugService.getAllBugs();
        return ApiResponse.success(bugs);
    }

    // 更新缺陷
    @Operation(summary = "修改缺陷", description = "根据缺陷信息,修改缺陷")
    @PutMapping("/{id}")
    public ApiResponse<Bug> updateBug(@PathVariable int id, @RequestBody Bug bug) {
        // 设置Bug ID
        bug.setBugId(id);
        boolean updated = bugService.updateBug(bug);
        return updated ? ApiResponse.success(bug) : ApiResponse.error(400, "更新失败");
    }

    // 删除缺陷
    @Operation(summary = "删除缺陷", description = "根据缺陷id删除缺陷")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteBug(@PathVariable int id) {
        boolean deleted = bugService.deleteBug(id);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }
}
