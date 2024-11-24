package com.example.verityx.mapper;

import com.example.verityx.entity.ApiRequestBody;
import java.util.List;

public interface ApiRequestBodyMapper {

    int insertBody(ApiRequestBody body);

    ApiRequestBody selectBodyById(int bodyId);

    List<ApiRequestBody> selectBodiesByApiId(int apiId);

    int updateBody(ApiRequestBody body);

    int deleteBody(int bodyId);
}
