package com.example.verityx.service.impl;

import com.example.verityx.entity.ApiQueryParams;
import com.example.verityx.mapper.ApiQueryParamsMapper;
import com.example.verityx.service.ApiQueryParamsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiQueryParamsServiceImpl implements ApiQueryParamsService {

    @Autowired
    private ApiQueryParamsMapper apiQueryParamsMapper;

    @Override
    public ApiQueryParams createParam(ApiQueryParams param) {
        apiQueryParamsMapper.insertParam(param);
        return param;
    }

    @Override
    public ApiQueryParams getParamById(int paramId) {
        return apiQueryParamsMapper.selectParamById(paramId);
    }

    @Override
    public List<ApiQueryParams> getParamsByApiId(int apiId) {
        return apiQueryParamsMapper.selectParamsByApiId(apiId);
    }

    @Override
    public boolean updateParam(ApiQueryParams param) {
        return apiQueryParamsMapper.updateParam(param) > 0;
    }

    @Override
    public boolean deleteParam(int paramId) {
        return apiQueryParamsMapper.deleteParam(paramId) > 0;
    }
}
