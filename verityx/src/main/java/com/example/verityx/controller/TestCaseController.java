package com.example.verityx.controller;

import com.example.verityx.entity.TestCase;
import com.example.verityx.service.TestCaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/testcases")
@Tag(name = "测试用例", description = "测试用例管理")
public class TestCaseController {

    @Autowired
    private TestCaseService testCaseService;

    // 创建测试用例
    @Operation(summary = "创建测试用例", description = "创建一个新的测试用例")
    @PostMapping
    public TestCase createTestCase(@RequestBody TestCase testCase) {
        return testCaseService.createTestCase(testCase);
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

    // 更新测试用例
    @Operation(summary = "分页查询测试用例", description = "根据分页参数查询测试用例")
    @PutMapping("/{id}")
    public boolean updateTestCase(@PathVariable int id, @RequestBody TestCase testCase) {
        testCase.setCaseId(id);
        return testCaseService.updateTestCase(testCase);
    }

    // 删除测试用例
    @DeleteMapping("/{id}")
    public boolean deleteTestCase(@PathVariable int id) {
        return testCaseService.deleteTestCase(id);
    }
}
