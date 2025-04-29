package com.example.verityx.service;

import com.example.verityx.entity.ApiTestReport;
import java.util.List;
import java.util.Map;

public interface ApiTestReportService {

    /**
     * 创建API测试报告
     */
    ApiTestReport createReport(ApiTestReport report);

    /**
     * 获取指定ID的API测试报告
     */
    ApiTestReport getReportById(int reportId);

    /**
     * 分页查询API测试报告
     */
    Map<String, Object> getReportsByPage(String keyword, String status, int page, int pageSize);

    /**
     * 更新API测试报告
     */
    boolean updateReport(ApiTestReport report);

    /**
     * 删除API测试报告
     */
    boolean deleteReport(int reportId);
    
    /**
     * 批量删除API测试报告
     */
    boolean batchDeleteReports(List<Integer> ids);
} 