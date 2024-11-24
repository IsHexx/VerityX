package com.example.verityx.service;

import com.example.verityx.entity.ApiQueryParams;
import java.util.List;

public interface ApiQueryParamsService {

    ApiQueryParams createParam(ApiQueryParams param);

    ApiQueryParams getParamById(int paramId);

    List<ApiQueryParams> getParamsByApiId(int apiId);

    boolean updateParam(ApiQueryParams param);

    boolean deleteParam(int paramId);
}
