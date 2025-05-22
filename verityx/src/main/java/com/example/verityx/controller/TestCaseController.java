package com.example.verityx.controller;

import com.example.verityx.dto.ApiResponse;
import com.example.verityx.entity.TestCase;
import com.example.verityx.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/testcases")
@Tag(name = "测试用例", description = "测试用例管理")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    // 创建测试用例
    @Operation(summary = "创建测试用例", description = "创建一个新的测试用例")
    @PostMapping
    public ApiResponse<TestCase> createTestCase(@RequestBody TestCase testCase) {
        TestCase createdTestCase = testCaseService.createTestCase(testCase);
        return ApiResponse.success(createdTestCase);
    }

    // 查询单个测试用例
    @Operation(summary = "查询单个测试用例", description = "按照测试用例id获取测试用例信息")
    @GetMapping("/{id}")
    public TestCase getTestCaseById(@PathVariable int id) {
        return testCaseService.getTestCaseById(id);
    }

    // 查询所有测试用例
    @Operation(summary = "查询所有测试用例", description = "获取所有测试用例")
    @GetMapping
    public List<TestCase> getAllTestCases() {
        return testCaseService.getAllTestCases();
    }

    // 分页获取测试计划
    @Operation(summary = "分页查询测试用例", description = "根据分页参数查询测试用例")
    @GetMapping("/list")
    public ApiResponse<Map<String, Object>> getTestCasesWithPagination(
            @RequestParam int page, 
            @RequestParam int pageSize, 
            @RequestParam(required = false) String caseStatus,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String projectId) {

        int offset = (page - 1) * pageSize;
        System.out.println("pageSize是:" + pageSize);
        System.out.println("offset:" + offset);
        System.out.println("projectId:" + projectId);
        
        List<TestCase> testCase = testCaseService.getTestCasesWithPagination(pageSize, offset, caseStatus, keyword, projectId);
        int total = testCaseService.getTestCaseCount(caseStatus, keyword, projectId); // 获取总记录数
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", testCase);
        response.put("total", total);
        response.put("page", page);
        response.put("pageSize", pageSize);

        return ApiResponse.success(response);
    }

    // 更新测试用例
    @Operation(summary = "更新测试用例", description = "更新测试用例")
    @PutMapping("/{id}")
    public ApiResponse<Boolean> updateTestCase(@PathVariable int id, @RequestBody TestCase testCase) {
        testCase.setCaseId(id);
        boolean updated = testCaseService.updateTestCase(testCase);
        if (updated) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "更新失败");
    }

    // 删除测试用例
    @Operation(summary = "删除测试用例", description = "删除测试用例")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteTestCase(@PathVariable int id) {
        boolean deleted = testCaseService.deleteTestCase(id);
        if (deleted) {
            return ApiResponse.success(true);
        }
        return ApiResponse.error(400, "删除失败");
    }
}
