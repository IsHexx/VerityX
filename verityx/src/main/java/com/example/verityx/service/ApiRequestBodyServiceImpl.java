package com.example.verityx.service;

import com.example.verityx.entity.ApiRequestBody;
import com.example.verityx.mapper.ApiRequestBodyMapper;
import com.example.verityx.service.ApiRequestBodyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApiRequestBodyServiceImpl implements ApiRequestBodyService {

    @Autowired
    private ApiRequestBodyMapper apiRequestBodyMapper;

    @Override
    public ApiRequestBody createBody(ApiRequestBody body) {
        apiRequestBodyMapper.insertBody(body);
        return body;
    }

    @Override
    public ApiRequestBody getBodyById(int bodyId) {
        return apiRequestBodyMapper.selectBodyById(bodyId);
    }

    @Override
    public List<ApiRequestBody> getBodiesByApiId(int apiId) {
        return apiRequestBodyMapper.selectBodiesByApiId(apiId);
    }

    @Override
    public boolean updateBody(ApiRequestBody body) {
        return apiRequestBodyMapper.updateBody(body) > 0;
    }

    @Override
    public boolean deleteBody(int bodyId) {
        return apiRequestBodyMapper.deleteBody(bodyId) > 0;
    }
}
