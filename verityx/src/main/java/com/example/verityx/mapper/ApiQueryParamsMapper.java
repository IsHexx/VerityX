package com.example.verityx.mapper;

import com.example.verityx.entity.ApiQueryParams;
import java.util.List;

public interface ApiQueryParamsMapper {

    int insertParam(ApiQueryParams param);

    ApiQueryParams selectParamById(int paramId);

    List<ApiQueryParams> selectParamsByApiId(int apiId);

    int updateParam(ApiQueryParams param);

    int deleteParam(int paramId);
}
