package com.example.verityx.controller;

import com.example.verityx.entity.ApiQueryParams;
import com.example.verityx.service.ApiQueryParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/query-params")
public class ApiQueryParamsController {

    @Autowired
    private ApiQueryParamsService apiQueryParamsService;

    // 创建查询参数
    @PostMapping
    public ApiQueryParams createParam(@RequestBody ApiQueryParams param) {
        return apiQueryParamsService.createParam(param);
    }

    // 根据ID查询参数
    @GetMapping("/{id}")
    public ApiQueryParams getParamById(@PathVariable int id) {
        return apiQueryParamsService.getParamById(id);
    }

    // 根据接口ID查询所有参数
    @GetMapping("/by-api/{apiId}")
    public List<ApiQueryParams> getParamsByApiId(@PathVariable int apiId) {
        return apiQueryParamsService.getParamsByApiId(apiId);
    }

    // 更新查询参数
    @PutMapping("/{id}")
    public boolean updateParam(@PathVariable int id, @RequestBody ApiQueryParams param) {
        param.setParamId(id);
        return apiQueryParamsService.updateParam(param);
    }

    // 删除查询参数
    @DeleteMapping("/{id}")
    public boolean deleteParam(@PathVariable int id) {
        return apiQueryParamsService.deleteParam(id);
    }
}
