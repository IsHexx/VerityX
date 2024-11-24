package com.example.verityx.service;

import com.example.verityx.entity.ApiRequestBody;
import java.util.List;

public interface ApiRequestBodyService {

    ApiRequestBody createBody(ApiRequestBody body);

    ApiRequestBody getBodyById(int bodyId);

    List<ApiRequestBody> getBodiesByApiId(int apiId);

    boolean updateBody(ApiRequestBody body);

    boolean deleteBody(int bodyId);
}
