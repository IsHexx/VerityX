package com.example.verityx.mapper;

import com.example.verityx.entity.ApiTestReport;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ApiTestReportMapper {

    int insertReport(ApiTestReport report);

    ApiTestReport selectReportById(int id);

    List<ApiTestReport> selectReportsByPage(@Param("keyword") String keyword, 
                                            @Param("status") String status,
                                            @Param("offset") int offset, 
                                            @Param("pageSize") int pageSize);

    int countReports(@Param("keyword") String keyword, @Param("status") String status);

    int updateReport(ApiTestReport report);

    int deleteReport(int id);
    
    int batchDeleteReports(@Param("ids") List<Integer> ids);
} 