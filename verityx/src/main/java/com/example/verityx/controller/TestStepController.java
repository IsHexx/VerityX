package com.example.verityx.controller;

import com.example.verityx.entity.TestStep;
import com.example.verityx.service.TestStepService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teststeps")
public class TestStepController {

    @Autowired
    private TestStepService testStepService;

    // 创建测试步骤
    @PostMapping
    public TestStep createTestStep(@RequestBody TestStep testStep) {
        return testStepService.createTestStep(testStep);
    }

    // 查询单个测试步骤
    @GetMapping("/{id}")
    public TestStep getTestStepById(@PathVariable int id) {
        return testStepService.getTestStepById(id);
    }

    // 查询某个用例的所有测试步骤
    @GetMapping("/case/{caseId}")
    public List<TestStep> getTestStepsByCaseId(@PathVariable int caseId) {
        return testStepService.getTestStepsByCaseId(caseId);
    }

    // 更新测试步骤
    @PutMapping("/{id}")
    public boolean updateTestStep(@PathVariable int id, @RequestBody TestStep testStep) {
        testStep.setStepId(id);
        return testStepService.updateTestStep(testStep);
    }

    // 删除测试步骤
    @DeleteMapping("/{id}")
    public boolean deleteTestStep(@PathVariable int id) {
        return testStepService.deleteTestStep(id);
    }
}
