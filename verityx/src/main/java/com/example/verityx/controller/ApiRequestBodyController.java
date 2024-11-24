package com.example.verityx.controller;

import com.example.verityx.entity.ApiRequestBody;
import com.example.verityx.service.ApiRequestBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/request-body")
public class ApiRequestBodyController {

    @Autowired
    private ApiRequestBodyService apiRequestBodyService;

    // 创建请求Body
    @PostMapping
    public ApiRequestBody createBody(@RequestBody ApiRequestBody body) {
        return apiRequestBodyService.createBody(body);
    }

    // 根据ID查询请求Body
    @GetMapping("/{id}")
    public ApiRequestBody getBodyById(@PathVariable int id) {
        return apiRequestBodyService.getBodyById(id);
    }

    // 根据接口ID查询所有请求Body
    @GetMapping("/by-api/{apiId}")
    public List<ApiRequestBody> getBodiesByApiId(@PathVariable int apiId) {
        return apiRequestBodyService.getBodiesByApiId(apiId);
    }

    // 更新请求Body
    @PutMapping("/{id}")
    public boolean updateBody(@PathVariable int id, @RequestBody ApiRequestBody body) {
        body.setBodyId(id);
        return apiRequestBodyService.updateBody(body);
    }

    // 删除请求Body
    @DeleteMapping("/{id}")
    public boolean deleteBody(@PathVariable int id) {
        return apiRequestBodyService.deleteBody(id);
    }
}
