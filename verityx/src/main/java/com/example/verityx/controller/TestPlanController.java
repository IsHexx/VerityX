package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.TestCase;
import com.example.verityx.entity.TestPlan;
import com.example.verityx.entity.TestPlanCaseRel;
import com.example.verityx.service.TestPlanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testplans")
@Tag(name = "测试计划", description = "测试计划管理")
public class TestPlanController {

    @Autowired
    private TestPlanService testPlanService;

    // 创建测试计划
    @Operation(summary = "创建测试计划", description = "创建一个新的测试计划")
    @PostMapping
    public ApiResponse<TestPlan> createTestPlan(@RequestBody TestPlan testPlan) {
        System.out.println("请求参数是:" + testPlan);
        TestPlan createdTestPlan = testPlanService.createTestPlan(testPlan);
        return ApiResponse.success(createdTestPlan);
    }

    // 获取单个测试计划
    @Operation(summary = "查询单个测试计划", description = "按照测试计划id获取测试计划信息")
    @GetMapping("/{planId}")
    public ApiResponse<TestPlan> getTestPlan(@PathVariable Integer planId, @RequestParam(required = false) Integer projectId) {
        TestPlan testPlan = testPlanService.getTestPlanById(planId);
        // 如果提供了项目ID，验证测试计划是否属于该项目
        if (testPlan != null && projectId != null && !projectId.equals(testPlan.getProjectId())) {
            return ApiResponse.error(404, "在指定项目中未找到该测试计划");
        }
        if (testPlan != null) {
            return ApiResponse.success(testPlan);
        }
        return ApiResponse.error(404, "测试计划未找到");
    }

    // 获取所有测试计划
    @Operation(summary = "查询所有测试计划", description = "获取所有测试计划，可以按项目ID筛选")
    @GetMapping
    public ApiResponse<List<TestPlan>> getAllTestPlans(@RequestParam(required = false) Integer projectId) {
        if (projectId != null) {
            List<TestPlan> testPlans = testPlanService.getTestPlansByProjectId(projectId);
            return ApiResponse.success(testPlans);
        } else {
            List<TestPlan> testPlans = testPlanService.getAllTestPlans();
            return ApiResponse.success(testPlans);
        }
    }

    // 分页获取测试计划
    @Operation(summary = "分页查询测试计划", description = "根据分页参数查询测试计划，可以按项目ID和状态筛选")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getTestPlansWithPagination(
            @RequestParam int page,
            @RequestParam int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Integer projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);

        List<TestPlan> testPlans = testPlanService.getTestPlansWithPagination(pageSize, offset, status, projectId);
        int total = testPlanService.getTestPlanCount(status, projectId); // 获取总记录数

        Map<String, Object> response = new HashMap<>();
        response.put("list", testPlans); // 改为list以保持前端一致性
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    // 更新测试计划
    @Operation(summary = "更新测试计划", description = "根据测试计划信息，更新测试计划信息")
    @PutMapping("/{planId}")
    public ApiResponse<TestPlan> updateTestPlan(@PathVariable Integer planId, @RequestBody TestPlan testPlan) {
        testPlan.setPlanId(planId); // 保证 planId 与路径中的一致
        boolean updated = testPlanService.updateTestPlan(testPlan);
        return updated ? ApiResponse.success(testPlan) : ApiResponse.error(400, "更新失败");
    }

    // 删除测试计划
    @Operation(summary = "删除测试计划", description = "根据测试计划id删除测试计划")
    @DeleteMapping("/{planId}")
    public ApiResponse<Boolean> deleteTestPlan(@PathVariable Integer planId, @RequestParam(required = false) Integer projectId) {
        // 如果提供了项目ID，验证测试计划是否属于该项目
        if (projectId != null) {
            TestPlan testPlan = testPlanService.getTestPlanById(planId);
            if (testPlan != null && !projectId.equals(testPlan.getProjectId())) {
                return ApiResponse.error(400, "无权删除其他项目的测试计划");
            }
        }

        boolean deleted = testPlanService.deleteTestPlan(planId);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }

    // 关联测试用例到测试计划
    @Operation(summary = "关联测试用例", description = "关联测试用例到测试计划")
    @PostMapping("/{planId}/cases")
    public ApiResponse<Boolean> associateTestCases(
            @PathVariable Integer planId,
            @RequestBody Map<String, List<Integer>> requestBody,
            @RequestParam(required = false) Integer projectId) {

        List<Integer> caseIds = requestBody.get("caseIds");
        if (caseIds == null || caseIds.isEmpty()) {
            return ApiResponse.error(400, "测试用例ID列表不能为空");
        }

        // 如果提供了项目ID，验证测试计划是否属于该项目
        if (projectId != null) {
            TestPlan testPlan = testPlanService.getTestPlanById(planId);
            if (testPlan != null && !projectId.equals(testPlan.getProjectId())) {
                return ApiResponse.error(400, "无权操作其他项目的测试计划");
            }
        }

        boolean associated = testPlanService.associateTestCases(planId, caseIds);
        if (associated) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "关联测试用例失败");
    }

    // 从测试计划中移除测试用例
    @Operation(summary = "移除测试用例", description = "从测试计划中移除测试用例")
    @DeleteMapping("/{planId}/cases/{caseId}")
    public ApiResponse<Boolean> removeTestCase(
            @PathVariable Integer planId,
            @PathVariable Integer caseId,
            @RequestParam(required = false) Integer projectId) {

        // 如果提供了项目ID，验证测试计划是否属于该项目
        if (projectId != null) {
            TestPlan testPlan = testPlanService.getTestPlanById(planId);
            if (testPlan != null && !projectId.equals(testPlan.getProjectId())) {
                return ApiResponse.error(400, "无权操作其他项目的测试计划");
            }
        }

        boolean removed = testPlanService.removeTestCase(planId, caseId);
        if (removed) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "移除测试用例失败");
    }

    // 获取测试计划关联的测试用例
    @Operation(summary = "获取关联的测试用例", description = "获取测试计划关联的测试用例列表")
    @GetMapping("/{planId}/cases")
    public ApiResponse<List<TestCase>> getAssociatedTestCases(
            @PathVariable Integer planId,
            @RequestParam(required = false) Integer projectId) {

        // 如果提供了项目ID，验证测试计划是否属于该项目
        if (projectId != null) {
            TestPlan testPlan = testPlanService.getTestPlanById(planId);
            if (testPlan != null && !projectId.equals(testPlan.getProjectId())) {
                return ApiResponse.error(400, "无权查看其他项目的测试计划");
            }
        }

        List<TestCase> testCases = testPlanService.getAssociatedTestCases(planId);
        return ApiResponse.success(testCases);
    }
}
